package dopamine.backend.feedImage.entity;

import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feedImage.request.FeedImageRequestDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Getter
public class FeedImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imageId;

    private String image1Url;

    private String image2Url;

    private String image3Url;

    @OneToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public void setFeed(Feed feed){
        this.feed = feed;
    }

    public void changeFeedImage(FeedImageRequestDTO feedImageRequestDTO){
        this.image1Url = image1Url;
        this.image2Url = image2Url;
        this.image3Url = image3Url;
    }
}
