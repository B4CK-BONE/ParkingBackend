package cat.soft.src.parking.controller.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.config.BaseResponse;
import cat.soft.config.BaseResponseStatus;
import cat.soft.src.parking.model.room.PostCreateRoomReq;
import cat.soft.src.parking.model.room.PostCreateRoomRes;
import cat.soft.src.parking.model.room.PutJoinRoomReq;
import cat.soft.src.parking.model.room.PutJoinRoomRes;

@RestController
@RequestMapping("/api/room")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@GetMapping("/")
	public String testRes() {
		return "hi";
	}

	@PostMapping("/")
	public BaseResponse<PostCreateRoomRes> createRoom(@RequestBody PostCreateRoomReq postCreateRoomReq) {
		PostCreateRoomRes postCreateRoomRes = new PostCreateRoomRes(roomService.createRoom(postCreateRoomReq));
		if (postCreateRoomRes.getRoom_idx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 해당 유저 없음
		}
		return new BaseResponse<>(postCreateRoomRes);
	}

	@PutMapping("/{roomId}")
	public BaseResponse<PutJoinRoomRes> joinRoom(@PathVariable Integer roomId, @RequestBody PutJoinRoomReq req) {
		PutJoinRoomRes putJoinRoomRes = new PutJoinRoomRes(roomService.joinRoom(roomId, req));
		if (putJoinRoomRes.getRoomIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 해당 유저 없음
		}
		return new BaseResponse<>(putJoinRoomRes);
	}
}
