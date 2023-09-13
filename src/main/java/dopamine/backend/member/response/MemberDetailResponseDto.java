package dopamine.backend.member.response;

import dopamine.backend.level.response.LevelResponseDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetailResponseDto {

    private Long memberId;
    private String kakaoId;
    private String nickname;
    private int feedCnt;
    private LevelResponseDto level;
}
