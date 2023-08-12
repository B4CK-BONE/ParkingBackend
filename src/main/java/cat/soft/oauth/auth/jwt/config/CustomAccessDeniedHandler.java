package cat.soft.oauth.auth.jwt.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;


import java.io.IOException;

import static cat.soft.oauth.util.BaseResponseStatus.INVALID_USER_JWT;


@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException,
        ServletException {
        log.info("403 ERROR");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write( "{" + "\"isSuccess\":false, "
                + "\"code\":\"" + INVALID_USER_JWT.getCode() + "\","
                + "\"message\":\"" + INVALID_USER_JWT.getMessage() + "\"}");
        response.getWriter().flush();
    }
}

