package cat.soft.src.parking.controller.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.BaseResponse;
import cat.soft.src.oauth.util.BaseResponseStatus;
import cat.soft.src.parking.model.room.GetJoinRoomReq;
import cat.soft.src.parking.model.room.GetJoinRoomRes;
import cat.soft.src.parking.model.room.GetQrCheckRes;
import cat.soft.src.parking.model.room.GetUserListByAdminRes;
import cat.soft.src.parking.model.room.PostCreateRoomRes;
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
	public BaseResponse<PostCreateRoomRes> createRoom(@RequestHeader("Authorization") String token) throws
		BaseException {
		jwtTokenProvider.verifySignature(token);
		PostCreateRoomRes postCreateRoomRes = roomService.createRoom(token);
		if (postCreateRoomRes.getRoom_idx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN15);
		}
		return new BaseResponse<>(postCreateRoomRes);
	}

	@GetMapping("")
	public BaseResponse<GetJoinRoomRes> joinRoom(@Valid @ModelAttribute GetJoinRoomReq req,
		@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetJoinRoomRes getJoinRoomRes = roomService.joinRoom(req.getRoom_id(), token);
		if (getJoinRoomRes.getRoomIdx() == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN);
		}
		if (getJoinRoomRes.getRoomIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.ALREADY_ALLOWED);
		}
		if (getJoinRoomRes.getRoomIdx() == -1) {
			return new BaseResponse<>(BaseResponseStatus.ALLOW_WAITING);
		}
		if (getJoinRoomRes.getRoomIdx() == -2) {
			return new BaseResponse<>(BaseResponseStatus.ALLOW_DENIED);
		}
		return new BaseResponse<>(getJoinRoomRes);
	}

	@GetMapping("/qr")
	public BaseResponse<GetQrCheckRes> qrCheck(@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetQrCheckRes getQrCheckRes = roomService.joinRoom(token);
		if (getQrCheckRes.getRoomIdx() == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN16); // 방정보 없음
		}
		return new BaseResponse<>(getQrCheckRes);
	}

	@GetMapping("/{roomId}/admin")
	public BaseResponse<GetUserListByAdminRes> userListByAdmin(@PathVariable Integer roomId,
		@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetUserListByAdminRes getUserListByAdminRes = roomService.userListByAdmin(roomId, token);
		if (getUserListByAdminRes.getNewUser() == null && getUserListByAdminRes.getOldUser() == null) {
			return new BaseResponse<>(BaseResponseStatus.NO_PAGE_AUTH);
		}
		return new BaseResponse<>(getUserListByAdminRes);
	}

	@PutMapping("/{roomId}/admin")
	public BaseResponse<PutUserApproveRes> approveUser(@PathVariable Integer roomId,
		@Valid @RequestBody PutUserApproveReq req, @RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		PutUserApproveRes putUserApproveRes = roomService.approveUser(roomId, req, token);
		if (putUserApproveRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN18); // 방정보 없음
		}
		if (putUserApproveRes.getUserIdx() == -1) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN19); // 방정보 없음
		}
		if (putUserApproveRes.getUserIdx() == -2) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN20); // 방정보 없음
		}
		if (putUserApproveRes.getUserIdx() == -3) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN21); // 방정보 없음
		}
		return new BaseResponse<>(putUserApproveRes);
	}
}
