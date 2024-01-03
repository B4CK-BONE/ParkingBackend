package cat.soft.src.parking.controller.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/room")
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

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public BaseResponse<HttpMessageNotReadableException> handleNotReadableException(
		HttpMessageNotReadableException ex) {
		return new BaseResponse<>(ex);
	}

	@ExceptionHandler(MySQLTransactionRollbackException.class)
	public BaseResponse<MySQLTransactionRollbackException> handleNotReadableException(
		MySQLTransactionRollbackException ex) {
		return new BaseResponse<>(ex);
	}

	@Operation(summary = "방(주차장) 생성 기능", description = "빌라, 아파트 등 입주민들의 주차장을 생성합니다.", tags = { "주차장 생성 및 입장" })
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
		@ApiResponse(responseCode = "404", description = "NOT FOUND"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@PostMapping("")
	public BaseResponse<PostCreateRoomRes> createRoom(@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws
		BaseException {
		jwtTokenProvider.verifySignature(token);
		PostCreateRoomRes postCreateRoomRes = roomService.createRoom(token);
		if (postCreateRoomRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN22);
		}
		if (postCreateRoomRes.getRoom_idx() == -1) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN23);
		}
		if (postCreateRoomRes.getRoom_idx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN15);
		}
		return new BaseResponse<>(postCreateRoomRes);
	}

	@Operation(summary = "방(주차장) 입장 기능", description = "빌라, 아파트 등 입주민들의 주차장을 입장합니다.", tags = { "주차장 생성 및 입장" })
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
		@ApiResponse(responseCode = "404", description = "NOT FOUND"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@GetMapping("")
	public BaseResponse<GetJoinRoomRes> joinRoom(@Valid @ModelAttribute GetJoinRoomReq req,
		@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetJoinRoomRes getJoinRoomRes = roomService.joinRoom(req.getRoom_id(), token);
		if (getJoinRoomRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN24);
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
		if (getJoinRoomRes.getRoomIdx() == -3) {
			return new BaseResponse<>(BaseResponseStatus.NO_ROOM);
		}
		return new BaseResponse<>(getJoinRoomRes);
	}

	@Operation(summary = "방(주차장) 초대 기능", description = "빌라, 아파트 등 입주민들의 주차장을 초대합니다.", tags = { "주차장 생성 및 입장" })
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
		@ApiResponse(responseCode = "404", description = "NOT FOUND"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@GetMapping("/qr")
	public BaseResponse<GetQrCheckRes> qrCheck(@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetQrCheckRes getQrCheckRes = roomService.joinRoom(token);
		if (getQrCheckRes.getRoomIdx() == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN16);
		}
		return new BaseResponse<>(getQrCheckRes);
	}

	@Operation(summary = "방(주차장) 생성자 정보 조회 기능", description = "빌라, 아파트 등 입주민들의 주차장을 생성한 사람의 정보를 조회합니다.", tags = { "주차장 생성 및 입장" })
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
		@ApiResponse(responseCode = "404", description = "NOT FOUND"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@GetMapping("/{roomId}/admin")
	public BaseResponse<GetUserListByAdminRes> userListByAdmin(@PathVariable Long roomId,
		@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetUserListByAdminRes getUserListByAdminRes = roomService.userListByAdmin(roomId, token);
		if (getUserListByAdminRes.getNewUser() == null && getUserListByAdminRes.getOldUser() == null) {
			return new BaseResponse<>(BaseResponseStatus.NO_PAGE_AUTH);
		}
		return new BaseResponse<>(getUserListByAdminRes);
	}

	@Operation(summary = "방(주차장) 입장 승인 기능", description = "빌라, 아파트 등 입주민들의 주차장 입장을 승인합니다.", tags = { "주차장 생성 및 입장" })
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
		@ApiResponse(responseCode = "404", description = "NOT FOUND"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@PutMapping("/{roomId}/admin")
	public BaseResponse<PutUserApproveRes> approveUser(@PathVariable Long roomId,
		@Valid @RequestBody PutUserApproveReq req, @Parameter(hidden = true) @RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		PutUserApproveRes putUserApproveRes = roomService.approveUser(roomId, req, token);
		if (putUserApproveRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN18);
		}
		if (putUserApproveRes.getUserIdx() == -1) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN19);
		}
		if (putUserApproveRes.getUserIdx() == -2) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN20);
		}
		if (putUserApproveRes.getUserIdx() == -3) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN21);
		}
		return new BaseResponse<>(putUserApproveRes);
	}
}
