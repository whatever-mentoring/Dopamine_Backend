package dopamine.backend.feedImage.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FeedImageResponseDTO {
    private String image1Url;
    private String image2Url;
    private String image3Url;
}
