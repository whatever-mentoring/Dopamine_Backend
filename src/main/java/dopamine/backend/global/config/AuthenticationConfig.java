package dopamine.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dopamine.backend.global.jwt.filter.JwtExceptionFilter;
import dopamine.backend.global.jwt.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig {


    @Value("${jwt.secret}")
    private String secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html","/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용하는 경우 사용
                .and()
                .authorizeRequests(authorize -> authorize.antMatchers("/api/auth/login", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll())
                //.authorizeRequests(authorize -> authorize.anyRequest().permitAll())
                .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class)
                .build()
                ;
    }
}