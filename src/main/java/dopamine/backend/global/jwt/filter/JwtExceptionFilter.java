package dopamine.backend.global.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;

import dopamine.backend.global.exception.ErrorResponse;
import dopamine.backend.global.exception.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            //토큰의 유효기간 만료
            setErrorResponse(request, response, ExceptionCode.TOKEN_NOT_VALID);
        } catch (JwtException e) {
            //유효하지 않은 토큰
            setErrorResponse(request, response, ExceptionCode.TOKEN_NOT_VALID);
        } catch (IllegalStateException e) {
            setErrorResponse(request, response, ExceptionCode.LOGOUT_MEMBER);
        } catch (AuthenticationException e) {
            setErrorResponse(request, response, ExceptionCode.AUTHORIZATION_HEADER_NOT_VALID);
        }
    }

    private void setErrorResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            ExceptionCode exceptionCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();

        int status = 400;
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = exceptionCode.getClass().getName();
        String message = exceptionCode.getMessage();
        String path = request.getServletPath();

        ErrorResponse errorResponse = new ErrorResponse(status, error, exception, message, path);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}