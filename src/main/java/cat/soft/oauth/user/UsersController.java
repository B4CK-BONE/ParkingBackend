package cat.soft.oauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.oauth.auth.dto.PostUserAuthRes;
import cat.soft.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.oauth.user.dto.GetUserRes;
import cat.soft.oauth.user.model.User;
import cat.soft.oauth.util.BaseException;
import cat.soft.oauth.util.BaseResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UsersController {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserProvider userProvider;

	@Autowired
	public UsersController(JwtTokenProvider jwtTokenProvider, UserProvider userProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userProvider = userProvider;
	}

	@GetMapping("")
	public BaseResponse<GetUserRes> getProfile(HttpServletRequest request) throws BaseException {
		String user_email = (String)request.getAttribute("user_email");
		User user = userProvider.retrieveByEmail(user_email);
		GetUserRes getUserRes = new GetUserRes(user.getEmail());
		return new BaseResponse<>(getUserRes);
	}

	@GetMapping("/auth")
	public BaseResponse<PostUserAuthRes> UserAuth(@RequestHeader("Authorization") String token) throws BaseException {
		Claims claims = jwtTokenProvider.getJwtContents(token);

		PostUserAuthRes postUserAuthRes = userProvider.UserInfoProvider(String.valueOf(claims.get("email")));

		return new BaseResponse<>(postUserAuthRes);
	}
}
