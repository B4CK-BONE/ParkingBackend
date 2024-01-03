package cat.soft.src.parking.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.BaseResponse;
import cat.soft.src.oauth.util.BaseResponseStatus;
import cat.soft.src.parking.model.user.GetUserInfoRes;
import cat.soft.src.parking.model.user.PutUserInfoReq;
import cat.soft.src.parking.model.user.PutUserInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserService userService;
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

	@Operation(summary = "유저 정보 입력 기능", description = "유저의 정보를 입력합니다.", tags = { "유저 정보" })
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
		@ApiResponse(responseCode = "404", description = "NOT FOUND"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@PutMapping("/{id}")
	public BaseResponse<PutUserInfoRes> putUserInfo(@PathVariable Long id,
		@Valid @RequestBody PutUserInfoReq userInfoReq, @Parameter(hidden = true) @RequestHeader("Authorization") String token) throws
		BaseException {
		jwtTokenProvider.verifySignature(token);
		PutUserInfoRes putUserInfoRes = userService.updateUserInfo(id, userInfoReq, token);
		if (putUserInfoRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN11);
		}
		if (putUserInfoRes.getIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN12);
		}
		return new BaseResponse<>(putUserInfoRes);
	}

	@Operation(summary = "유저 정보 조회 기능", description = "유저의 정보를 조희합니다.", tags = { "유저 정보" })
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
		@ApiResponse(responseCode = "404", description = "NOT FOUND"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@GetMapping("/{id}")
	public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable Long id,
		@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetUserInfoRes getUserInfoRes = userService.getUserInfo(id, token);
		if (getUserInfoRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN13);
		}
		if (getUserInfoRes.getPhone() == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN14);
		}
		return new BaseResponse<>(getUserInfoRes);
	}
}
