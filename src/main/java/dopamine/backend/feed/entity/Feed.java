package dopamine.backend.feed.entity;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.feed.request.FeedEditDTO;
import dopamine.backend.member.entity.Member;
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

    private Boolean openYn;

    private String image1Url;

    private String image2Url;

    private String image3Url;

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

    public void deleteFromChallenge(){
        this.challenge.getFeeds().remove(this);
    }

    @Builder
    public Feed(String content, Boolean openYn, Member member,String image1Url,String image2Url,String image3Url) {
        this.content = content;
        this.openYn = openYn;
        this.member = member;
        this.image1Url = image1Url;
        this.image2Url = image2Url;
        this.image3Url = image3Url;
    }

    public void changeFeed(FeedEditDTO feedEditDTO){
        this.content = feedEditDTO.getContent();
        this.openYn = feedEditDTO.getOpenYn();
        this.image1Url = feedEditDTO.getImage1Url();
        this.image2Url = feedEditDTO.getImage2Url();
        this.image3Url = feedEditDTO.getImage3Url();
    }
}
