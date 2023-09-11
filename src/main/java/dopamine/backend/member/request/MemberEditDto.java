package dopamine.backend.member.request;

import dopamine.backend.level.entity.Level;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEditDto {
    private String kakaoId;
    private String nickname;
    private String refreshToken;
    private Level level;
    private Long levelId;
}
