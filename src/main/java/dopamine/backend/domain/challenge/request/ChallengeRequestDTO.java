package dopamine.backend.domain.challenge.request;

import dopamine.backend.domain.challenge.entity.ChallengeLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
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

    @Builder
    public ChallengeRequestDTO(String title, String subtitle, String image, String challengeGuide, ChallengeLevel challengeLevel) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.challengeGuide = challengeGuide;
        this.challengeLevel = challengeLevel;
    }
}
