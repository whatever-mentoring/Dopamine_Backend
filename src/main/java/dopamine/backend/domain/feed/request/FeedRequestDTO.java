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

    private Boolean openYn;

    private Long memberId;

    private Long challengeId;
}
