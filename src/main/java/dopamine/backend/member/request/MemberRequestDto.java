package dopamine.backend.member.request;

import dopamine.backend.level.entity.Level;
import dopamine.backend.level.service.LevelService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private String kakaoId;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;
    private String refreshToken;
    private Level level;
    private Long levelId;
}
