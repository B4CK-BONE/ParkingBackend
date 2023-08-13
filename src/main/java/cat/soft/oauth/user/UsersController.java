package cat.soft.oauth.user;

import cat.soft.oauth.auth.dto.PostSigninRes;
import cat.soft.oauth.auth.dto.PostUserAuthRes;
import cat.soft.oauth.auth.dto.RefreshTokenRes;
import cat.soft.oauth.auth.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

import static cat.soft.oauth.util.BaseResponseStatus.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UsersController {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserProvider userProvider;
	private final UserDao userDao;
	@Autowired
	public UsersController(JwtTokenProvider jwtTokenProvider, UserProvider userProvider, UserDao userDao) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userProvider = userProvider;
		this.userDao = userDao;
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

	@GetMapping("/refresh")
	public ResponseEntity<RefreshTokenRes> refRefreshToken(@CookieValue(name = "refreshtoken") String token) throws BaseException{
		Claims claims = jwtTokenProvider.getJwtContents(token);
		String email = String.valueOf(claims.get("email"));
		if (userProvider.checkEmail(email) == 0)
			throw new BaseException(USERS_EMPTY_USER_EMAIL);
		try {
			String beforeRefToken = userDao.tokenByEmail(email);
			if(beforeRefToken != token)
				throw new BaseException(INVALID_JWT);
			else {
				String accessToken = jwtTokenProvider.createAccessToken(email);
				String refreshToken = jwtTokenProvider.createRefreshToken(email);

				HttpHeaders headers = new HttpHeaders();
				headers.set("Set-Cookie","refreshToken="+refreshToken+"; HttpOnly");

				RefreshTokenRes refreshTokenRes = new RefreshTokenRes();
				refreshTokenRes.setAccessToken(accessToken);

				return ResponseEntity.ok().headers(headers).body(refreshTokenRes);
			}
		} catch (Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
}
