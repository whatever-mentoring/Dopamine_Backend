package dopamine.backend.jwt.response;

import dopamine.backend.member.response.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}