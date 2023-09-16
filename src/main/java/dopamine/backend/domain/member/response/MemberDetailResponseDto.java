package dopamine.backend.domain.member.response;

import dopamine.backend.domain.level.response.LevelDetailResponseDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetailResponseDto {

    private Long memberId;
    private String kakaoId;
    private String nickname;
    private int exp;
    private LevelDetailResponseDto level;
}
