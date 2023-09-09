package dopamine.backend.challenge.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter // ToDo : 여기 Getter 가 없어서 에러가 발생해서 추가했어요!
public class ChallengeResponseDTO {
    private String title;
    private String subtitle;
    private String image;
    private String challengeGuide;
    private Integer challengeLevel;
}
