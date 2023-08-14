package cat.soft.src.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import cat.soft.src.oauth.auth.AuthService;
import cat.soft.src.oauth.auth.PrincipalOAuth2DetailsService;
import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.auth.jwt.config.CustomAccessDeniedHandler;
import cat.soft.src.oauth.auth.jwt.config.CustomAuthenticationEntryPoint;
import cat.soft.src.oauth.auth.jwt.config.OAuth2SuccessHandler;

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

			.cors()
			.and()

			.authorizeHttpRequests()
			.requestMatchers("/oauth2/authorization/google").permitAll()
			.requestMatchers("/api/user/**").permitAll()
			.requestMatchers("/api/room/**").permitAll()
			.requestMatchers("/api/parking/**").permitAll()
			.anyRequest().denyAll()

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


