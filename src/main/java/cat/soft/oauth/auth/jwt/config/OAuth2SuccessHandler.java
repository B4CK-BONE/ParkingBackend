package cat.soft.oauth.auth.jwt.config;


import cat.soft.oauth.auth.AuthService;
import cat.soft.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.oauth.auth.model.PrincipalDetails;
import jakarta.servlet.ServletException;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();
        String targetUrl;
        String accessToken =  jwtTokenProvider.createAccessToken(oAuth2User.getUser().getEmail());
        String refreshToken =  jwtTokenProvider.createRefreshToken(oAuth2User.getUser().getEmail());

        authService.registerRefreshToken(oAuth2User.getUser().getEmail(),refreshToken);

        targetUrl = UriComponentsBuilder.fromUriString("https://backboneproject-okwcj.run.goorm.site/login")
                .queryParam("accessToken",accessToken)
                .queryParam("refreshToken",refreshToken)
                .build().toUriString();
        System.out.println("===========================================================================================================================================================================");
        System.out.println("access: "+accessToken);
        System.out.println("refresh: "+refreshToken);
        System.out.println("===========================================================================================================================================================================");
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}