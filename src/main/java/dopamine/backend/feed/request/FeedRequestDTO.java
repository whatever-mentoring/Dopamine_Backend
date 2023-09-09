package dopamine.backend.feed.request;

import dopamine.backend.feedImage.request.FeedImageRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedRequestDTO {

    private String content;

    private Boolean openYn;

    private Long memberId;

    private Long challengeId;

    private FeedImageRequestDTO feedImageRequestDTO;
}
