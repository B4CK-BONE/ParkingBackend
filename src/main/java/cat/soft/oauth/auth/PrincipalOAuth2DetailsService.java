package cat.soft.oauth.auth;

import cat.soft.oauth.auth.model.PrincipalDetails;
import cat.soft.oauth.user.UserProvider;
import cat.soft.oauth.user.UserService;
import cat.soft.oauth.user.model.User;
import cat.soft.oauth.util.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import static cat.soft.oauth.util.EncryptionUtils.encryptSHA256;

@Slf4j
@Component
public class PrincipalOAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserProvider userProvider;
    private final UserService userService;

    @Autowired
    public PrincipalOAuth2DetailsService(UserProvider userProvider, UserService userService) {
        this.userProvider = userProvider;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);


        String email = oAuth2User.getAttributes().get("email").toString();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String provider_id = oAuth2User.getAttributes().get("sub").toString();

        User user = null;

        try {
            if ( userProvider.checkEmail(email) == 0) {
                log.info("구글 로그인이 최초입니다. 회원가입을 진행합니다.");
                user = new User(email, encryptSHA256(email), provider, provider_id);
                userService.createUser(user);
            }else {
                log.info("구글 로그인 기록이 있습니다.");
                user = userProvider.retrieveByEmail(email);
            }
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
