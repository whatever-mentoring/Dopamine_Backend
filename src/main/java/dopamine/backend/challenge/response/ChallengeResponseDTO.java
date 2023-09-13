package dopamine.backend.challenge.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengeResponseDTO {
    private String title;
    private String subtitle;
    private String image;
    private String challengeGuide;
    private Integer challengeLevel;
}
