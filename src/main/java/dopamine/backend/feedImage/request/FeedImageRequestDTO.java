package dopamine.backend.feedImage.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FeedImageRequestDTO {
    private String image1Url;
    private String image2Url;
    private String image3Url;
}
