package dopamine.backend.challenge.response;

import lombok.Builder;

@Builder
public class ChallengeResponseDTO {
    private String title;
    private String subtitle;
    private String image;
}
