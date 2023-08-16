package cat.soft.src.oauth.auth.jwt.config;

import cat.soft.src.oauth.auth.AuthService;
import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.auth.model.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthService authService;
	private static final String email = "email";
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication)
		throws IOException, ServletException {
		PrincipalDetails oAuth2User = (PrincipalDetails)authentication.getPrincipal();
		String targetUrl;
		String accessToken = jwtTokenProvider.createAccessToken(oAuth2User.getAttribute(email));
		String refreshToken = jwtTokenProvider.createRefreshToken(oAuth2User.getAttribute(email));

		authService.registerRefreshToken(oAuth2User.getAttribute(email), refreshToken);

		targetUrl = UriComponentsBuilder.fromUriString("https://backboneproject-okwcj.run.goorm.site/login")
			.queryParam("accessToken", accessToken)
			.queryParam("refreshToken", refreshToken)
			.build().toUriString();
		System.out.println(
			"===========================================================================================================================================================================");
		System.out.println("access: " + accessToken);
		System.out.println("refresh: " + refreshToken);
		System.out.println(
			"===========================================================================================================================================================================");
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}