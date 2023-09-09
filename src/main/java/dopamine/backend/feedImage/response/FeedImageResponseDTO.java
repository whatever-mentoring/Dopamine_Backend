package dopamine.backend.feedImage.response;

import lombok.Builder;

@Builder
public class FeedImageResponseDTO {
    private String image1Url;
    private String image2Url;
    private String image3Url;
}
