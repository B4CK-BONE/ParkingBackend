package cat.soft.src.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//TODO: CORS 해제 테스트용
@Configuration // 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {
	private final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 모든 경로에 대해
		registry.addMapping("/**")
			// Origin이 http:localhost:3000에 대해
			.allowedOrigins("https://backboneproject-okwcj.run.goorm.site/")
			// GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(MAX_AGE_SECS);
	}
}