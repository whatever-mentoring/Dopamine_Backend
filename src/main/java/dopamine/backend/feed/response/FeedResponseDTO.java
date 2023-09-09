package dopamine.backend.feed.response;

import dopamine.backend.challenge.response.ChallengeResponseDTO;
import dopamine.backend.feedImage.response.FeedImageResponseDTO;
import lombok.Builder;

@Builder
public class FeedResponseDTO {
    private String content;

    private Boolean openYn;

    // todo member 정보 어디까지 추가해야하는지 결정
    private Long memberId;

    private ChallengeResponseDTO challengeResponseDTO;

    private FeedImageResponseDTO feedImageResponseDTO;
}
