package cat.soft.src.parking.controller.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.oauth.util.BaseResponse;
import cat.soft.oauth.util.BaseResponseStatus;
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

@RestController
@RequestMapping("/api/room")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@PostMapping("/")
	public BaseResponse<PostCreateRoomRes> createRoom(@RequestBody PostCreateRoomReq postCreateRoomReq) {
		PostCreateRoomRes postCreateRoomRes = roomService.createRoom(postCreateRoomReq);
		if (postCreateRoomRes.getRoom_idx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 해당 유저 없음
		}
		return new BaseResponse<>(postCreateRoomRes);
	}

	@PutMapping("/{roomId}")
	public BaseResponse<PutJoinRoomRes> joinRoom(@PathVariable Integer roomId, @RequestBody PutJoinRoomReq req) {
		PutJoinRoomRes putJoinRoomRes = roomService.joinRoom(roomId, req);
		if (putJoinRoomRes.getRoomIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 해당 유저 없음
		}
		return new BaseResponse<>(putJoinRoomRes);
	}

	@GetMapping("/qr")
	public BaseResponse<GetQrCheckRes> qrCheck(@RequestBody GetQrCheckReq req) {
		GetQrCheckRes getQrCheckRes = roomService.joinRoom(req);
		if (getQrCheckRes.getRoomIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 방정보 없음
		}
		return new BaseResponse<>(getQrCheckRes);
	}

	@GetMapping("/{roomId}/admin")
	public BaseResponse<GetUserListByAdminRes> userListByAdmin(@PathVariable Integer roomId,
		@RequestBody GetUserListByAdminReq req) {
		GetUserListByAdminRes getUserListByAdminRes = roomService.userListByAdmin(roomId, req);
		return new BaseResponse<>(getUserListByAdminRes);
	}

	@PutMapping("/{roomId}/admin")
	public BaseResponse<PutUserApproveRes> approveUser(@PathVariable Integer roomId,
		@RequestBody PutUserApproveReq req) {
		PutUserApproveRes putUserApproveRes = roomService.approveUser(roomId, req);
		if (putUserApproveRes.getUserIdx() == null) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 방정보 없음
		}
		return new BaseResponse<>(putUserApproveRes);
	}
}