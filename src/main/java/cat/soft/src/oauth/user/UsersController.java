package cat.soft.src.oauth.user;

import static cat.soft.src.oauth.util.BaseResponseStatus.*;

import cat.soft.src.oauth.user.dto.GetSurveyReq;
import cat.soft.src.oauth.util.BaseResponseStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import cat.soft.src.oauth.auth.AuthDao;
import cat.soft.src.oauth.auth.dto.PostUserAuthRes;
import cat.soft.src.oauth.auth.dto.RefreshTokenRes;
import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.user.dto.GetUserRes;
import cat.soft.src.oauth.user.dto.LogoutRes;
import cat.soft.src.oauth.user.model.User;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.BaseResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UsersController {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserProvider userProvider;
	private final UserDao userDao;

	private final AuthDao authDao;

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public BaseResponse<MethodArgumentNotValidException> handleValidationExceptions(
//			MethodArgumentNotValidException ex) {
//
//		return new BaseResponse<>(ex);
//	}

	@Autowired
	public UsersController(JwtTokenProvider jwtTokenProvider, UserProvider userProvider, UserDao userDao,
		AuthDao authDao) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userProvider = userProvider;
		this.userDao = userDao;
		this.authDao = authDao;
	}

	@GetMapping("/auth")
	public BaseResponse<PostUserAuthRes> UserAuth(@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);

		Claims claims = jwtTokenProvider.getJwtContents(token);

		PostUserAuthRes postUserAuthRes = userProvider.UserInfoProvider(String.valueOf(claims.get("email")));

		return new BaseResponse<>(postUserAuthRes);
	}

	@GetMapping("/logout")
	public ResponseEntity<LogoutRes> UserLogout(@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		Claims claims = jwtTokenProvider.getJwtContents(token);
		String email = String.valueOf(claims.get("email"));
		if (userProvider.checkEmail(email) == 0)
			throw new BaseException(USERS_EMPTY_USER_EMAIL);
		try {
			if (token.equals("undefined")){
				throw new BaseException(EMPTY_JWT);
			} else {
				userDao.LogoutUser(email);

				LogoutRes logoutRes = new LogoutRes();
				logoutRes.setIsSuccess("True");

				return ResponseEntity.ok().body(logoutRes);
			}
		} catch (Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	@GetMapping("/refresh")
	public ResponseEntity<RefreshTokenRes> refRefreshToken(@RequestHeader("Authorization") String token) throws
		BaseException {
		Claims claims = jwtTokenProvider.getJwtContents(token);
		String email = String.valueOf(claims.get("email"));
		if (userProvider.checkEmail(email) == 0)
			throw new BaseException(USERS_EMPTY_USER_EMAIL);
		try {
			String beforeRefToken = userDao.tokenByEmail(email);
			if (token == "undefined") {
				throw new BaseException(EMPTY_JWT);
			} else if (!beforeRefToken.equals(token)) {
				throw new BaseException(INVALID_JWT);
			} else {
				String accessToken = jwtTokenProvider.createAccessToken(email);
				String refreshToken = jwtTokenProvider.createRefreshToken(email);

				authDao.updateRefreshToken(email, refreshToken);

				//				HttpHeaders headers = new HttpHeaders();
				//				headers.set("Set-Cookie", "refreshToken=" + refreshToken + "; HttpOnly");

				RefreshTokenRes refreshTokenRes = new RefreshTokenRes();
				refreshTokenRes.setAccessToken(accessToken);
				refreshTokenRes.setRefreshToken((refreshToken));

				ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
					.httpOnly(true)
					.secure(true)
					.path("/")
					.maxAge(60 * 60 * 24 * 60)
					.build();

				return ResponseEntity.ok()
					.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
					.body(refreshTokenRes);
			}
		} catch (Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	@PostMapping("/survey")
	public BaseResponse<BaseResponseStatus> insertSurvey(@RequestHeader("Authorization") String token, @Valid @RequestBody GetSurveyReq contents) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		Claims claims = jwtTokenProvider.getJwtContents(token);
		String email = String.valueOf(claims.get("email"));

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String context = String.valueOf(contents.getContents());
		String img = String.valueOf(contents.getImg());

		userDao.insertSurvey(context, img, email);
		return new BaseResponse<>(SUCCESS);
	}
}
