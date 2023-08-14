package cat.soft.src.parking.controller.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.util.BaseResponse;
import cat.soft.src.oauth.util.BaseResponseStatus;
import cat.soft.src.parking.model.room.GetQrCheckRes;
import cat.soft.src.parking.model.room.GetUserListByAdminRes;
import cat.soft.src.parking.model.room.PostCreateRoomRes;
import cat.soft.src.parking.model.room.PutJoinRoomRes;
import cat.soft.src.parking.model.room.PutUserApproveReq;
import cat.soft.src.parking.model.room.PutUserApproveRes;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/room")
public class RoomController {

	@Autowired
	private RoomService roomService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public BaseResponse<MethodArgumentNotValidException> handleValidationExceptions(
		MethodArgumentNotValidException ex) {

		return new BaseResponse<>(ex);
	}

	@PostMapping("")
	public BaseResponse<PostCreateRoomRes> createRoom(@RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		PostCreateRoomRes postCreateRoomRes = roomService.createRoom(token);
		if (postCreateRoomRes.getRoom_idx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR); // 해당 유저 없음
		}
		return new BaseResponse<>(postCreateRoomRes);
	}

	@PutMapping("/{roomId}")
	public BaseResponse<PutJoinRoomRes> joinRoom(@PathVariable Integer roomId,
		@RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		PutJoinRoomRes putJoinRoomRes = roomService.joinRoom(roomId, token);
		if (putJoinRoomRes.getRoomIdx() == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN);
		}
		if (putJoinRoomRes.getRoomIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.ALREADY_ALLOWED);
		}
		if (putJoinRoomRes.getRoomIdx() == -1) {
			return new BaseResponse<>(BaseResponseStatus.ALLOW_WAITING);
		}
		if (putJoinRoomRes.getRoomIdx() == -2) {
			return new BaseResponse<>(BaseResponseStatus.ALLOW_DENIED);
		}
		return new BaseResponse<>(putJoinRoomRes);
	}

	@GetMapping("/qr")
	public BaseResponse<GetQrCheckRes> qrCheck(@RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		GetQrCheckRes getQrCheckRes = roomService.joinRoom(token);
		if (getQrCheckRes.getRoomIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 방정보 없음
		}
		return new BaseResponse<>(getQrCheckRes);
	}

	@GetMapping("/{roomId}/admin")
	public BaseResponse<GetUserListByAdminRes> userListByAdmin(@PathVariable Integer roomId,
		@RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		GetUserListByAdminRes getUserListByAdminRes = roomService.userListByAdmin(roomId, token);
		return new BaseResponse<>(getUserListByAdminRes);
	}

	@PutMapping("/{roomId}/admin")
	public BaseResponse<PutUserApproveRes> approveUser(@PathVariable Integer roomId,
		@Valid @RequestBody PutUserApproveReq req, @RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		PutUserApproveRes putUserApproveRes = roomService.approveUser(roomId, req, token);
		if (putUserApproveRes.getUserIdx() == null) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 방정보 없음
		}
		return new BaseResponse<>(putUserApproveRes);
	}
}
