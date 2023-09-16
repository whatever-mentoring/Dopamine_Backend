package dopamine.backend.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dopamine.backend.global.exception.BusinessLogicException;
import dopamine.backend.global.exception.ErrorResponse;
import dopamine.backend.global.exception.ExceptionCode;
import dopamine.backend.global.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        // 로그인일 경우 건너뛰기
        if (
                path.startsWith("/api/auth/login") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs/") || path.startsWith("/swagger-resources/")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization : {}", authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessLogicException(ExceptionCode.AUTHORIZATION_HEADER_NOT_VALID);
        }

        validBlackToken(authorization);

        // Token 꺼내기
        String token = authorization.split(" ")[1];
        // Token Expired 되었는지 여부
        if (JwtProvider.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // UserId Token에서 꺼내기
        Long userId = JwtProvider.getUserId(token, secretKey);
        log.info("userName: {}", userId);

        // 토큰 재발급일 경우 리프레쉬 토큰 확인
        // 위에서 만료됐는지 확인했기 때문에 따로 만료확인 필요 없음
        // 리프레쉬 토큰이 유효한지와 path 정보를 통해 확인이 끝났기 때문에 컨트롤러에서는 바로 토큰 재발행해주고 보내주면 됨
        if (
                !(
                        (path.startsWith("/api/oauth/kakao") && JwtProvider.isRefreshToken(token, secretKey))
                                || JwtProvider.isAccessToken(token, secretKey)
                )
        ) {
            // 재발행 요청 api인데, access token을 전달했을 경우
            // 아니면 access token을 넣어줘야하는데, 다른 토큰을 넣었을 경우
            throw new AuthenticationException();
        }

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail을 넣어줌
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("[+] Token in SecurityContextHolder");
        filterChain.doFilter(request, response);
    }

    private void validBlackToken(String accessToken) {
        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackToken)) {
            throw new IllegalStateException();
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