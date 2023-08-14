package cat.soft.src.oauth.auth;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final AuthDao authDao;

	@Autowired
	public AuthService(AuthDao authDao, JwtTokenProvider jwtTokenProvider) {
		this.authDao = authDao;
	}

	public void registerRefreshToken(String useremail, String refreshToken) {
		if (authDao.checkUser(useremail))
			authDao.updateRefreshToken(useremail, refreshToken);
		else
			authDao.insertRefreshToken(useremail, refreshToken);

	}

	//    public Long modifyRefreshToken(Long userid, String refreshToken) {
	//        this.authDao.updateRefreshToken(userid,refreshToken);
	//
	//        return userid;
	//    }

}
