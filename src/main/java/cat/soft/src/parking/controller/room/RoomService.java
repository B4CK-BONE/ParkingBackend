package cat.soft.src.parking.controller.room;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.parking.model.Room;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.room.PostCreateRoomReq;
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
		userRepository.save(user);
		return room.getIdx();
	}
}
