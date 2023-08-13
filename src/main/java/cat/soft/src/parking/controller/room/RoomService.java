package cat.soft.src.parking.controller.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.parking.model.Room;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.UserInfo;
import cat.soft.src.parking.model.room.GetQrCheckReq;
import cat.soft.src.parking.model.room.GetQrCheckRes;
import cat.soft.src.parking.model.room.GetUserListByAdminReq;
import cat.soft.src.parking.model.room.GetUserListByAdminRes;
import cat.soft.src.parking.model.room.PostCreateRoomReq;
import cat.soft.src.parking.model.room.PostCreateRoomRes;
import cat.soft.src.parking.model.room.PutJoinRoomReq;
import cat.soft.src.parking.model.room.PutJoinRoomRes;
import cat.soft.src.parking.model.room.PutUserApproveReq;
import cat.soft.src.parking.model.room.PutUserApproveRes;
import cat.soft.src.parking.repository.ReportRepository;
import cat.soft.src.parking.repository.RoomRepository;
import cat.soft.src.parking.repository.UserInfoRepository;
import cat.soft.src.parking.repository.UserRepository;

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

	public PostCreateRoomRes createRoom(PostCreateRoomReq req) {
		User user = userRepository.findById(req.getIdx()).orElse(null);
		if (user == null) {
			return new PostCreateRoomRes(0);
		}
		if (user.getRoomIdx() != 0) {
			return new PostCreateRoomRes(0);
		}
		Room room = roomRepository.save(req.toEntity());
		user.setRoomIdx(room.getIdx());
		user.setRole(2L);
		userRepository.save(user);
		return new PostCreateRoomRes(room.getIdx());
	}

	public PutJoinRoomRes joinRoom(Integer roomId, PutJoinRoomReq req) {
		User user = userRepository.findById(req.getUserIdx()).orElse(null);
		Room room = roomRepository.findById(roomId).orElse(null);
		if (user == null) {
			return new PutJoinRoomRes(0);
		}
		if (user.getRoomIdx() != 0) {
			return new PutJoinRoomRes(0);
		}
		if (room == null) {
			return new PutJoinRoomRes(0);
		}
		user.setRoomIdx(roomId);
		userRepository.save(user);
		return new PutJoinRoomRes(user.getRoomIdx());
	}

	public GetQrCheckRes joinRoom(GetQrCheckReq req) {
		User user = userRepository.findById(req.getUserIdx()).orElse(null);
		if (user == null) {
			return new GetQrCheckRes(0);
		}
		if (user.getRoomIdx() == 0) {
			return new GetQrCheckRes(0);
		}
		return new GetQrCheckRes(user.getRoomIdx());
	}

	public GetUserListByAdminRes userListByAdmin(Integer roomId, GetUserListByAdminReq req) {
		User admin = userRepository.findById(req.getUserIdx()).orElse(null);
		if (admin == null) {
			return new GetUserListByAdminRes(null, null);
		}
		if (!Objects.equals(admin.getRoomIdx(), roomId) || admin.getRole() != 2) {
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

	public PutUserApproveRes approveUser(Integer roomId, PutUserApproveReq req) {
		Room room = roomRepository.findById(roomId).orElse(null);
		User admin = userRepository.findById(req.getUserIdx()).orElse(null);
		User user = userRepository.findById(req.getAdminIdx()).orElse(null);
		if (room == null || admin == null || user == null) {
			return new PutUserApproveRes(null);
		}
		if (!Objects.equals(room.getAdminIdx(), admin.getIdx())) {
			return new PutUserApproveRes(null);
		}
		if (Objects.equals(user.getIdx(), admin.getIdx())) { // 셀프 추방 금지
			return new PutUserApproveRes(null);
		}
		if (!Objects.equals(user.getRoomIdx(), admin.getRoomIdx())) { // 신청하지 않은 유저 변경 금지
			return new PutUserApproveRes(null);
		}
		if (req.getRole() == 0) { // 거절
			user.setRole(req.getRole());
			user.setRoomIdx(0);
		} else if (req.getRole() == 1) { // 승인
			user.setRole(req.getRole());
		} else {
			return new PutUserApproveRes(null);
		}
		userRepository.save(user);
		return new PutUserApproveRes(user.getIdx());
	}
}
