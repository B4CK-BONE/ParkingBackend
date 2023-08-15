package cat.soft.src.parking.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
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

	@PutMapping("/{id}")
	public BaseResponse<PutUserInfoRes> putUserInfo(@PathVariable Long id,
		@Valid @RequestBody PutUserInfoReq userInfoReq, @RequestHeader("Authorization") String token) throws
		BaseException {
		jwtTokenProvider.verifySignature(token);
		PutUserInfoRes putUserInfoRes = userService.updateUserInfo(id, userInfoReq, token);
		if (putUserInfoRes.getIdx() == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN11); // 해당 유저 없음
		}
		if (putUserInfoRes.getIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN12); // 해당 유저 없음
		}
		return new BaseResponse<>(putUserInfoRes);
	}

	@GetMapping("/{id}")
	public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable Long id,
		@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		GetUserInfoRes getUserInfoRes = userService.getUserInfo(id, token);
		if (getUserInfoRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN13); // 해당 유저 없음
		}
		if (getUserInfoRes.getPhone() == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN14);
		}
		return new BaseResponse<>(getUserInfoRes);
	}
}