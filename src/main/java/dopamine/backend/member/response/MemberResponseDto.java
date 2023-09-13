package dopamine.backend.member.response;

import dopamine.backend.jwt.response.JwtResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class MemberResponseDto {
    private Long memberId;
    private String kakaoId;
    private String nickname;
}
