package dopamine.backend.domain.feed.response;

import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FeedResponseDTO {
    private String content;

    private String image1Url;

    private String image2Url;

    private String image3Url;

    private Boolean openYn;

    private Boolean fulfillYn;

    // todo member 정보 어디까지 추가해야하는지 결정
    private Long memberId;

    private ChallengeResponseDTO challengeResponseDTO;
}
