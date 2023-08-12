package cat.soft.oauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.oauth.user.dto.GetUserRes;
import cat.soft.oauth.user.model.User;
import cat.soft.oauth.util.BaseException;
import cat.soft.oauth.util.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {

	private final UserProvider userProvider;

	@Autowired
	public UsersController(UserProvider userProvider) {
		this.userProvider = userProvider;
	}

	@GetMapping("")
	public BaseResponse<GetUserRes> getProfile(HttpServletRequest request) throws BaseException {
		String user_email = (String)request.getAttribute("user_email");
		User user = userProvider.retrieveByEmail(user_email);
		GetUserRes getUserRes = new GetUserRes(user.getEmail());
		return new BaseResponse<>(getUserRes);
	}
}
