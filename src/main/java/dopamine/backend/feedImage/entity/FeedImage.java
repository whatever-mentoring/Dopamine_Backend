package dopamine.backend.feedImage.entity;

import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.feed.entity.Feed;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
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

    public void setImage1Url(String image1Url) {
        this.image1Url = image1Url;
    }

    public void setImage2Url(String image2Url) {
        this.image2Url = image2Url;
    }

    public void setImage3Url(String image3Url) {
        this.image3Url = image3Url;
    }

    public void deleteImage1Url() {
        this.image1Url = null;
    }

    public void deleteImage2Url(String image2Url) {
        this.image2Url = null;
    }

    public void deleteImage3Url(String image3Url) {
        this.image3Url = null;
    }
}
