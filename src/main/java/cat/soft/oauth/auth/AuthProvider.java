package cat.soft.oauth.auth;


import cat.soft.oauth.util.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cat.soft.oauth.util.BaseResponseStatus.DATABASE_ERROR;
import static cat.soft.oauth.util.BaseResponseStatus.USERS_EMPTY_USER_EMAIL;

@Service
public class AuthProvider {
    private final AuthDao authDao;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public AuthProvider(AuthDao authDao, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authDao = authDao;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public boolean isRefreshTokenEqual(String token) {
        if (!authDao.checkRefreshToken(token))
            return false;

        return true;
    }

}
