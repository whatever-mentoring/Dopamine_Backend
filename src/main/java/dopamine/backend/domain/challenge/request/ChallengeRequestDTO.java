package dopamine.backend.domain.challenge.request;

import dopamine.backend.domain.challenge.entity.ChallengeLevel;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRequestDTO {
    @NotBlank(message = "챌린지 제목은 공백이 될 수 없습니다.")
    private String title;
    private String subtitle;
    private String image;
    private String challengeGuide;
    private ChallengeLevel challengeLevel;

    public void setImage(String image) {
        this.image = image;
    }

}
