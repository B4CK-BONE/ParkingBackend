package cat.soft.src.parking.controller.parking;

import static cat.soft.src.oauth.util.Constant.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.parking.model.ParkingLot;
import cat.soft.src.parking.model.Report;
import cat.soft.src.parking.model.Room;
import cat.soft.src.parking.model.Time;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.UserInfo;
import cat.soft.src.parking.model.parking.GetTimeRes;
import cat.soft.src.parking.model.parking.PostAddTimeReq;
import cat.soft.src.parking.model.parking.PostAddTimeRes;
import cat.soft.src.parking.model.parking.PostReportReq;
import cat.soft.src.parking.model.parking.PostReportRes;
import cat.soft.src.parking.repository.ParkingLotRepository;
import cat.soft.src.parking.repository.ReportRepository;
import cat.soft.src.parking.repository.RoomRepository;
import cat.soft.src.parking.repository.TimeRepository;
import cat.soft.src.parking.repository.UserInfoRepository;
import cat.soft.src.parking.repository.UserRepository;

@Service
public class ParkingService {

	@Autowired
	private ParkingLotRepository parkingLotRepository;
	@Autowired
	private TimeRepository timeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private UserInfoRepository userInfoRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public PostAddTimeRes addTime(PostAddTimeReq req, String token) {
		User user = userRepository.findUsersByEmail(jwtTokenProvider.getEmail(token));
		if (ZonedDateTime.now().isAfter(req.getTime())) { //
			return new PostAddTimeRes(ZonedDateTime.now(TIME_LATE));
		}
		if (user == null || user.getRole() == 0)
			return null;
		Room room = roomRepository.findById(user.getRoomIdx()).orElse(null);
		if (room == null)
			return null;
		ParkingLot parkingLot = parkingLotRepository.findBySlotAndRoomIdx(req.getSlot(), room.getIdx());
		if (parkingLot == null) {
			return new PostAddTimeRes(ZonedDateTime.now(NO_SLOT));
		}
		if (timeRepository.findTimeByParkingLotIdxAndEndAfter(req.getSlot(), ZonedDateTime.now()) != null) {
			return new PostAddTimeRes(ZonedDateTime.now(USING_SLOT));
		}
		Time time = req.toEntity();
		time.setRoomIdx(room.getIdx());
		timeRepository.save(time);
		return new PostAddTimeRes(time.getStart());
	}

	public List<GetTimeRes> getTime(String token) {
		User user = userRepository.findUsersByEmail(jwtTokenProvider.getEmail(token));
		List<GetTimeRes> getTimeRes = new ArrayList<>();
		if (user == null || user.getRole() == 0)
			return null;
		List<ParkingLot> parkingLotList = parkingLotRepository.findAllByRoomIdx(user.getRoomIdx());
		if (parkingLotList == null) {
			return getTimeRes;
		}
		for (ParkingLot parkingLot : parkingLotList) {
			Time time = timeRepository.findTimeByParkingLotIdxAndEndAfter(parkingLot.getSlot(), ZonedDateTime.now());
			UserInfo usingUser = null;
			if (time != null)
				usingUser = userInfoRepository.findById(time.getUserIdx()).orElse(null);
			getTimeRes.add(new GetTimeRes(usingUser, parkingLot, time));
		}
		return getTimeRes;
	}

	public PostReportRes report(PostReportReq req, String token) {
		User victim = userRepository.findUsersByEmail(jwtTokenProvider.getEmail(token));
		User suspcet = userRepository.findById(req.getSuspect()).orElse(null);
		if (Objects.equals(victim, suspcet))
			return new PostReportRes(ZonedDateTime.now(SAME_USER));
		if (victim == null || suspcet == null) {
			return null;
		}
		if (!Objects.equals(victim.getRoomIdx(), suspcet.getRoomIdx()))
			return new PostReportRes(ZonedDateTime.now(DIFF_ROOM));
		Time parkingTime = timeRepository.findTimeByUserIdxAndEndAfter(suspcet.getIdx(), ZonedDateTime.now());
		if (parkingTime == null)
			return new PostReportRes(ZonedDateTime.now(NO_PARKING));
		Report report = reportRepository.findReportByTimeAfter(ZonedDateTime.now().minusHours(24));
		if (report != null)
			return new PostReportRes(ZonedDateTime.now(ALREADY_REPORT));
		return new PostReportRes(reportRepository.save(req.toEntity()).getTime());
	}
}
