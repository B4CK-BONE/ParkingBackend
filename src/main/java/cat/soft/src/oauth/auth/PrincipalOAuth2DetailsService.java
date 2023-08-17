package cat.soft.src.oauth.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import cat.soft.src.oauth.auth.model.PrincipalDetails;
import cat.soft.src.oauth.user.UserProvider;
import cat.soft.src.oauth.user.UsersService;
import cat.soft.src.oauth.user.model.User;
import cat.soft.src.oauth.util.BaseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PrincipalOAuth2DetailsService extends DefaultOAuth2UserService {

	private final UserProvider userProvider;
	private final UsersService usersService;

	@Autowired
	public PrincipalOAuth2DetailsService(UserProvider userProvider, UsersService usersService) {
		this.userProvider = userProvider;
		this.usersService = usersService;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println(oAuth2User.getAttributes());

		String email = oAuth2User.getAttributes().get("email").toString();
		String provider = userRequest.getClientRegistration().getRegistrationId();
		String provider_id = oAuth2User.getAttributes().get("sub").toString();

		User user = null;

		try {
			if (userProvider.checkEmail(email) == 0) {
				log.info("구글 로그인이 최초입니다. 회원가입을 진행합니다.");
				user = new User(email);
				usersService.createUser(user);
				usersService.createUserInfo(email);
			} else {
				log.info("구글 로그인 기록이 있습니다.");
				user = userProvider.retrieveByEmail(email);
			}
		} catch (BaseException e) {
			throw new RuntimeException(e);
		}

		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}
