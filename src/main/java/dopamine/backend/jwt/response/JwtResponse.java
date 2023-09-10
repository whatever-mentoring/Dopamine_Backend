package dopamine.backend.jwt.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
