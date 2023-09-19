package dopamine.backend.domain.feed.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedRequestDTO {

    private String content;

    private String image1Url;

    private String image2Url;

    private String image3Url;

    private Long challengeId;

    public void setImage1Url(String image1Url) {
        this.image1Url = image1Url;
    }

    public void setImage2Url(String image2Url) {
        this.image2Url = image2Url;
    }

    public void setImage3Url(String image3Url) {
        this.image3Url = image3Url;
    }
}
