package dopamine.backend.global.jwt.response;

import dopamine.backend.domain.member.response.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtResponse {
    private TokenResponse token;
    private MemberResponseDto member;
}
