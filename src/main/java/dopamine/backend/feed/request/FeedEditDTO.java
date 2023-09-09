package dopamine.backend.feed.request;

import dopamine.backend.feedImage.request.FeedImageRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedEditDTO {
    private String content;
    private Boolean openYn;
    private FeedImageRequestDTO feedImageRequestDTO;
}
