package dopamine.backend.challenge.response;

import dopamine.backend.challenge.entity.ChallengeLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengeResponseDTO {
    private String title;
    private String subtitle;
    private String image;
    private String challengeGuide;
    private ChallengeLevel challengeLevel;
}
