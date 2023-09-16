package dopamine.backend.domain.feed.entity;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.common.entity.BaseEntity;
import dopamine.backend.domain.feed.request.FeedEditDTO;
import dopamine.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feed_id")
    private Long feedId;

    private String content;

    private String image1Url;

    private String image2Url;

    private String image3Url;

    @ColumnDefault("0")
    private Integer likeCount = 0;

    @ColumnDefault("true")
    private Boolean fulfillYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public void setMember(Member member){
        this.member = member;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        if(!this.challenge.getFeeds().contains(this))
            challenge.getFeeds().add(this);
    }

    public void addLikeCount(){
        this.likeCount++;
    }

    public void minusLikeCount(){
        this.likeCount--;
    }

    public void deleteFromChallenge(){
        this.challenge.getFeeds().remove(this);
    }

    @Builder
    public Feed(String content, Boolean openYn, Member member,String image1Url,String image2Url,String image3Url) {
        this.content = content;
        this.member = member;
        this.image1Url = image1Url;
        this.image2Url = image2Url;
        this.image3Url = image3Url;
    }

    public void changeFeed(FeedEditDTO feedEditDTO){
        this.content = feedEditDTO.getContent();
        this.image1Url = feedEditDTO.getImage1Url();
        this.image2Url = feedEditDTO.getImage2Url();
        this.image3Url = feedEditDTO.getImage3Url();
    }
}
