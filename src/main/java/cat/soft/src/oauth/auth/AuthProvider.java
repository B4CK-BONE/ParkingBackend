package cat.soft.src.oauth.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.stereotype.Service;

@Service
public class AuthProvider {
	private final AuthDao authDao;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired
	public AuthProvider(AuthDao authDao, AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.authDao = authDao;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}
}
