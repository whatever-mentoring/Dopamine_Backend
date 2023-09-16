package dopamine.backend.domain.member.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MemberResponseDto {
    private Long memberId;
    private String kakaoId;
    private String nickname;
    private int exp;
}
