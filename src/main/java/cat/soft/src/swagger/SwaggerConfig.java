package cat.soft.src.swagger;


import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	info = @Info(
		title = "ParKING 서비스 API 명세서",
		description = "\uD83D\uDD25주차지옥 해결 솔루션: 내 주차장의 현황을 알려주는 서비스, ParKING\uD83D\uDD25",
		version = "v1"
	)
)
@Configuration
public class SwaggerConfig {

}
