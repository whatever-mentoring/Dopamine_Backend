package dopamine.backend.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

// 토큰이 만료되었거나 토큰이 비었을경우 Exception 발생
@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            throw new BusinessLogicException(ExceptionCode.TOKEN_NOT_VALID);
        } catch (AuthenticationException e) {
            throw new BusinessLogicException(ExceptionCode.TOKEN_NOT_VALID);
        }
    }


    // Send Error Message to Client
    private void sendErrorMessage(HttpServletResponse response, Map<String, Object> errorDetails) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), errorDetails);
    }
}