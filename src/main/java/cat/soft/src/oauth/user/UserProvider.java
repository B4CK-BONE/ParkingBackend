package cat.soft.src.oauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.oauth.auth.dto.PostUserAuthRes;
import cat.soft.src.oauth.user.model.User;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserProvider {

	private final UserDao userDao;

	@Autowired
	public UserProvider(UserDao userDao) {
		this.userDao = userDao;
	}

	public User retrieveByEmail(String email) throws BaseException {
		if (checkEmail(email) == 0)
			throw new BaseException(BaseResponseStatus.USERS_EMPTY_USER_EMAIL);
		try {
			return userDao.selectByEmail(email);
		} catch (Exception e) {
			throw new BaseException(BaseResponseStatus.DELETE_USER_FAIL);
		}
	}

	public int checkEmail(String email) throws BaseException {
		try {
			return userDao.checkEmail(email);
		} catch (Exception exception) {
			log.warn(exception.getMessage());
			throw new BaseException(BaseResponseStatus.DELETE_USER_FAIL);
		}
	}

	public PostUserAuthRes UserInfoProvider(String email) throws BaseException {
		if (checkEmail(email) == 0)
			throw new BaseException(BaseResponseStatus.USERS_EMPTY_USER_EMAIL);
		try {
			return userDao.userInfo(email);
		} catch (Exception e) {
			throw new BaseException(BaseResponseStatus.DELETE_USER_FAIL);
		}
	}
}
