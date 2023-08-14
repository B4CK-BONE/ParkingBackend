package cat.soft.src.oauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.oauth.user.model.User;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsersService {

	private final UserProvider userProvider;
	private final UserDao userDao;

	@Autowired
	public UsersService(UserProvider userProvider, UserDao userDao) {
		this.userProvider = userProvider;
		this.userDao = userDao;
	}

	public User createUser(User user) throws BaseException {
		if (userProvider.checkEmail(user.getEmail()) == 1)
			throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
		try {
			return this.userDao.insertUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
		}

	}

}
