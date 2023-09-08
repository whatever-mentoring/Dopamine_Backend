package dopamine.backend.feed.entity;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.feedImage.entity.FeedImage;
import dopamine.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feed_id")
    private Long feedId;

    private String content;

    private Boolean openYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToOne(mappedBy = "feed", cascade = CascadeType.ALL)
    private FeedImage feedImage;

    private void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        challenge.getFeeds().add(this);
    }

    public void deleteFromChallenge(){
        this.challenge.getFeeds().remove(this);
    }

    // feedImage와 연관관계 생성 - 오직 생성시에만
    private void setFeedImage(){
        FeedImage newfeedImage = new FeedImage();
        feedImage.setFeed(this);
        this.feedImage = newfeedImage;
    }

    public Feed(String content, Boolean openYn, Member member, Challenge challenge) {
        this.content = content;
        this.openYn = openYn;
        this.member = member;
        setChallenge(challenge);
        setFeedImage();
    }

    // todo Member entity 편의 메소드 추가

    // like 연관관계는 API에서 feedId로 쿼리 조회하므로, 필요없을 것 같습니다 => CASCADE가 필요하면 DDL에서
}
