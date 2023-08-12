package cat.soft.src.parking.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.config.BaseResponse;
import cat.soft.config.BaseResponseStatus;
import cat.soft.src.parking.model.user.GetUserInfoReq;
import cat.soft.src.parking.model.user.GetUserInfoRes;
import cat.soft.src.parking.model.user.PutUserInfoReq;
import cat.soft.src.parking.model.user.PutUserInfoRes;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String testRes() {
		return "hi";
	}

	@PutMapping("/{id}")
	public BaseResponse<PutUserInfoRes> putUserInfo(@PathVariable Integer id, @RequestBody PutUserInfoReq userInfoReq) {
		if (userInfoReq.getCar() == null || userInfoReq.getAddress() == null || userInfoReq.getPhone() == null
			|| userInfoReq.getKakao() == null) {
			return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR); // 입력정보 없음
		}
		PutUserInfoRes putUserInfoRes = userService.updateUserInfo(id, userInfoReq);
		if (putUserInfoRes.getIdx() == 0) {
			return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // 해당 유저 없음
		}
		return new BaseResponse<>(putUserInfoRes);
	}

	@GetMapping("/{id}")
	public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable Integer id, @RequestBody GetUserInfoReq req) {
		GetUserInfoRes getUserInfoRes = userService.getUserInfo(id, req);
		return new BaseResponse<>(getUserInfoRes);
	}
}