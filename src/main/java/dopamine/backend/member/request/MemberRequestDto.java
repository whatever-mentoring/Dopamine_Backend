package dopamine.backend.member.request;

import dopamine.backend.level.entity.Level;
import dopamine.backend.level.service.LevelService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {
    private Long memberId;
    private Long kakaoId;
    private String nickname;
    private String refreshToken;
    private Level level;
    private Long levelId;
}
