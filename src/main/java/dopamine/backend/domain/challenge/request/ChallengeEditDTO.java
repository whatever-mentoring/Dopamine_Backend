package dopamine.backend.domain.challenge.request;

import dopamine.backend.domain.challenge.entity.ChallengeLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ChallengeEditDTO {
    @NotBlank(message = "챌린지 제목은 공백이 될 수 없습니다.")
    private String title;
    private String subtitle;
    private String image;
    private String challengeGuide;
    private ChallengeLevel challengeLevel;
}
