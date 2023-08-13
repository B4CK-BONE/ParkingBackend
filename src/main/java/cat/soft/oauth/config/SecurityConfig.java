package cat.soft.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import cat.soft.oauth.auth.AuthService;
import cat.soft.oauth.auth.PrincipalOAuth2DetailsService;
import cat.soft.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.oauth.auth.jwt.config.CustomAccessDeniedHandler;
import cat.soft.oauth.auth.jwt.config.CustomAuthenticationEntryPoint;
import cat.soft.oauth.auth.jwt.config.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

	private final PrincipalOAuth2DetailsService principalOAuth2DetailsService;
	private final AuthService authService;
	private final JwtTokenProvider jwtTokenProvider;

	@Autowired
	public SecurityConfig(PrincipalOAuth2DetailsService principalOAuth2DetailsService, AuthService authService,
		JwtTokenProvider jwtTokenProvider, AuthenticationConfiguration authenticationConfiguration) {
		this.principalOAuth2DetailsService = principalOAuth2DetailsService;
		this.authService = authService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()

			.authorizeHttpRequests()
			.requestMatchers("/api/users/*").permitAll()
			.anyRequest().authenticated()

			.and()

			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			.accessDeniedHandler(new CustomAccessDeniedHandler())

			.and()

			.oauth2Login()
			.userInfoEndpoint()
			.userService(principalOAuth2DetailsService)

			.and()

			.successHandler(new OAuth2SuccessHandler(jwtTokenProvider, authService));
		return http.build();
	}
}


