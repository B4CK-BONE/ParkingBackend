package cat.soft.src.parking.controller.parking;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import cat.soft.src.parking.model.ParkingLot;
import cat.soft.src.parking.model.Room;
import cat.soft.src.parking.model.Time;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.parking.GetTimeReq;
import cat.soft.src.parking.model.parking.GetTimeRes;
import cat.soft.src.parking.model.parking.PostAddTimeReq;
import cat.soft.src.parking.model.parking.PostAddTimeRes;
import cat.soft.src.parking.model.parking.TestRes;
import cat.soft.src.parking.repository.ParkingRepository;
import cat.soft.src.parking.repository.RoomRepository;
import cat.soft.src.parking.repository.TimeRepository;
import cat.soft.src.parking.repository.UserRepository;

@Service
public class ParkingService {

	@Autowired
	private ParkingRepository parkingRepository;
	@Autowired
	private TimeRepository timeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoomRepository roomRepository;

	public TestRes testText(@PathVariable("test") String test) {
		return new TestRes("test text" + test);
	}

	public PostAddTimeRes addTime(PostAddTimeReq req) {
		User user = userRepository.findById(req.getUserIdx()).orElse(null);
		if (ZonedDateTime.now().isAfter(req.getTime())) {
			return null;
		}
		if (user == null || user.getRole() == 0)
			return null;
		Room room = roomRepository.findById(user.getRoomIdx()).orElse(null);
		if (room == null)
			return null;
		ParkingLot parkingLot = parkingRepository.findBySlotAndRoomIdx(req.getSlot(), room.getIdx());
		if (parkingLot == null) {
			return null;
		}
		if (timeRepository.findTimeByParkingLotIdxAndEndAfter(req.getSlot(), ZonedDateTime.now()) != null) {
			return null;
		}
		Time time = req.toEntity();
		time.setRoomIdx(room.getIdx());
		timeRepository.save(time);
		return new PostAddTimeRes(time.getStart());
	}

	public List<GetTimeRes> getTime(GetTimeReq req) {
		User user = userRepository.findById(req.getUserIdx()).orElse(null);
		List<GetTimeRes> getTimeRes = new ArrayList<>();
		if (user == null || user.getRole() == 0)
			return null;
		List<ParkingLot> parkingLotList = parkingRepository.findAllByRoomIdx(user.getRoomIdx());
		if (parkingLotList == null)
			return null;
		for (ParkingLot parkingLot : parkingLotList) {
			Time time = timeRepository.findTimeByParkingLotIdxAndEndAfter(parkingLot.getSlot(), ZonedDateTime.now());
			User usingUser = null;
			if (time != null)
				usingUser = userRepository.findById(time.getUserIdx()).orElse(null);
			getTimeRes.add(new GetTimeRes(usingUser, parkingLot, time));
		}
		return getTimeRes;
	}
}
