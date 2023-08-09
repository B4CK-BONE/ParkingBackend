package cat.soft.src.parking.controller.room;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.parking.model.Room;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.room.GetQrCheckReq;
import cat.soft.src.parking.model.room.GetUserListByAdminReq;
import cat.soft.src.parking.model.room.GetUserListByAdminRes;
import cat.soft.src.parking.model.room.PostCreateRoomReq;
import cat.soft.src.parking.model.room.PutJoinRoomReq;
import cat.soft.src.parking.repository.RoomRepository;
import cat.soft.src.parking.repository.UserRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private UserRepository userRepository;

	public Integer createRoom(PostCreateRoomReq req) {
		User user;
		try {
			user = userRepository.findById(req.getIdx()).get();
		} catch (NoSuchElementException e) {
			return 0;
		}
		if (user.getRoomIdx() != 0) {
			return 0;
		}
		Room room = roomRepository.save(req.toEntity());
		user.setRoomIdx(room.getIdx());
		user.setRole(2L);
		userRepository.save(user);
		return room.getIdx();
	}

	public Integer joinRoom(Integer roomId, PutJoinRoomReq req) {
		User user;
		try {
			user = userRepository.findById(req.getUserIdx()).get();
		} catch (NoSuchElementException e) {
			return 0;
		}
		if (user.getRoomIdx() != 0) {
			return 0;
		}
		Room room;
		try {
			room = roomRepository.findById(roomId).get();
		} catch (NoSuchElementException e) {
			return 0;
		}
		user.setRoomIdx(roomId);
		userRepository.save(user);
		return userRepository.findById(req.getUserIdx()).get().getRoomIdx();
	}

	public Integer joinRoom(GetQrCheckReq req) {
		User user;
		try {
			user = userRepository.findById(req.getUserIdx()).get();
		} catch (NoSuchElementException e) {
			return 0;
		}
		if (user.getRoomIdx() == 0) {
			return 0;
		}
		return user.getRoomIdx();
	}

	public GetUserListByAdminRes userListByAdmin(Integer roomId, GetUserListByAdminReq req) {
		User user;
		try {
			user = userRepository.findById(req.getUserIdx()).get();
		} catch (NoSuchElementException e) {
			return new GetUserListByAdminRes(null, null);
		}
		if (!Objects.equals(user.getRoomIdx(), roomId) || user.getRole() != 2) {
			return new GetUserListByAdminRes(null, null);
		}
		List<User> newUser = userRepository.findUsersByRoomIdxAndRole(user.getRoomIdx(), 0L);
		List<User> oldUser = userRepository.findUsersByRoomIdxAndRole(user.getRoomIdx(), 1L);
		oldUser.add(user);
		return new GetUserListByAdminRes(newUser, oldUser);
	}
}
