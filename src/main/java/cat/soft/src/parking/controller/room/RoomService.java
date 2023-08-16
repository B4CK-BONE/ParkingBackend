package cat.soft.src.parking.controller.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.util.ParkingLotCreator;
import cat.soft.src.parking.model.Room;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.UserInfo;
import cat.soft.src.parking.model.room.GetJoinRoomRes;
import cat.soft.src.parking.model.room.GetQrCheckRes;
import cat.soft.src.parking.model.room.GetUserListByAdminRes;
import cat.soft.src.parking.model.room.PostCreateRoomRes;
import cat.soft.src.parking.model.room.PutUserApproveReq;
import cat.soft.src.parking.model.room.PutUserApproveRes;
import cat.soft.src.parking.repository.ParkingLotRepository;
import cat.soft.src.parking.repository.ReportRepository;
import cat.soft.src.parking.repository.RoomRepository;
import cat.soft.src.parking.repository.UserInfoRepository;
import cat.soft.src.parking.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private ParkingLotRepository parkingLotRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Transactional
	public PostCreateRoomRes createRoom(String token) {
		User user = userRepository.findByEmail(jwtTokenProvider.getEmail(token));
		if (user == null) {
			return null;
		}
		if (user.getRoomIdx() != 0) {
			return new PostCreateRoomRes(0L);
		}
		if (user.getRole() != 0) {
			return new PostCreateRoomRes(-1L);
		}
		Room room = roomRepository.save(Room.builder().idx(user.getIdx()).build());
		user.setRoomIdx(room.getIdx());
		user.setRole(2L);
		userRepository.save(user);
		parkingLotRepository.saveAll(ParkingLotCreator.getParkingLotCreator(room.getIdx()));
		return new PostCreateRoomRes(user.getIdx());
	}

	public GetJoinRoomRes joinRoom(Long roomId, String token) {
		User user = userRepository.findUserByEmail(jwtTokenProvider.getEmail(token));
		if (user == null) {
			return null; // 5000 불가능한 유저
		}
		if (user.getRole() != 0) {
			return new GetJoinRoomRes(0L); // [5001] 승인된 유저입니다.
		}
		if (user.getRoomIdx() != 0) {
			return new GetJoinRoomRes(-1L); // [5002] 승인 대기중 입니다.
		}
		if (roomId == null) {
			return new GetJoinRoomRes(-2L); // [5003] 존재하지 않는 방 입니다.
		}
		Room room = roomRepository.findById(roomId).orElse(null);
		if (room == null) {
			return new GetJoinRoomRes(-3L); // [5003] 존재하지 않는 방 입니다.
		}
		user.setRoomIdx(roomId);
		userRepository.save(user);
		return new GetJoinRoomRes(user.getRoomIdx());
	}

	public GetQrCheckRes joinRoom(String token) {
		User user = userRepository.findUserByEmail(jwtTokenProvider.getEmail(token));
		if (user == null) {
			return null;
		}
		if (user.getRoomIdx() == 0) {
			return null;
		}
		return new GetQrCheckRes(user.getRoomIdx());
	}

	public GetUserListByAdminRes userListByAdmin(Long roomId, String token) {
		User admin = userRepository.findUserByEmail(jwtTokenProvider.getEmail(token));
		Room room = roomRepository.findById(roomId).orElse(null);
		if (admin == null || room == null) {
			return new GetUserListByAdminRes(null, null);
		}
		if (!Objects.equals(admin.getRoomIdx(), room.getIdx()) || !Objects.equals(admin.getIdx(), room.getAdminIdx())
			|| admin.getRole() != 2) {
			return new GetUserListByAdminRes(null, null);
		}
		List<User> newUser = userRepository.findUsersByRoomIdxAndRole(admin.getRoomIdx(), 0L);
		List<UserInfo> newUserInfo = new ArrayList<>();
		for (User findUser : newUser) {
			UserInfo userInfo = userInfoRepository.findById(findUser.getIdx()).get();
			userInfo.setReportCount(reportRepository.countBysuspect(findUser.getIdx()));
			newUserInfo.add(userInfo);
		}
		List<User> oldUser = userRepository.findUsersByRoomIdxAndRole(admin.getRoomIdx(), 1L);
		List<UserInfo> oldUserInfo = new ArrayList<>();

		UserInfo adminInfo = userInfoRepository.findById(admin.getIdx()).get();
		adminInfo.setReportCount(reportRepository.countBysuspect(admin.getIdx()));
		oldUserInfo.add(adminInfo); // 방장 추가
		for (User findUser : oldUser) {
			UserInfo userInfo = userInfoRepository.findById(findUser.getIdx()).get();
			userInfo.setReportCount(reportRepository.countBysuspect(findUser.getIdx()));
			oldUserInfo.add(userInfo);
		}
		return new GetUserListByAdminRes(newUserInfo, oldUserInfo);
	}

	public PutUserApproveRes approveUser(Long roomId, PutUserApproveReq req, String token) {
		Room room = roomRepository.findById(roomId).orElse(null);
		User admin = userRepository.findUserByEmail(jwtTokenProvider.getEmail(token));
		User user = userRepository.findById(req.getUserIdx()).orElse(null);
		if (room == null || admin == null || user == null) {
			return null;
		}
		if (!Objects.equals(room.getAdminIdx(), admin.getIdx()) || !Objects.equals(room.getIdx(), admin.getRoomIdx())
			|| admin.getRole() != 2) {
			return null;
		}
		if (Objects.equals(user.getIdx(), admin.getIdx())) { // 셀프 추방 금지
			return new PutUserApproveRes(-1L);
		}
		if (!Objects.equals(user.getRoomIdx(), admin.getRoomIdx())) { // 신청하지 않은 유저 변경 금지
			return new PutUserApproveRes(-2L);
		}
		if (req.getRole() == 0) { // 거절
			user.setRole(req.getRole());
			user.setRoomIdx(0L);
		} else if (req.getRole() == 1) { // 승인
			user.setRole(req.getRole());
		} else {
			return new PutUserApproveRes(-3L);
		}
		userRepository.save(user);
		return new PutUserApproveRes(user.getIdx());
	}
}
