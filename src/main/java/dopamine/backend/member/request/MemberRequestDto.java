package dopamine.backend.member.request;

import dopamine.backend.level.entity.Level;
import dopamine.backend.level.service.LevelService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private String kakaoId;
    private String nickname;
    private String refreshToken;
    private Level level;
    private Long levelId;
}
