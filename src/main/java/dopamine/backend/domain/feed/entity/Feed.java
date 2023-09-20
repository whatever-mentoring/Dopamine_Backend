package dopamine.backend.domain.feed.entity;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.common.entity.BaseEntity;
import dopamine.backend.domain.feed.request.FeedEditDTO;
import dopamine.backend.domain.feedLike.entity.FeedLike;
import dopamine.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Slf4j
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
    private Boolean fulfillYn = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "feed")
    private List<FeedLike> feedLikeList = new ArrayList<>();

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        if(!this.challenge.getFeeds().contains(this))
            challenge.getFeeds().add(this);
    }

    public void setFulfillYn(Boolean fulfillYn) {
        this.fulfillYn = fulfillYn;
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

    //== 연관관계 매핑 ==//
    public void setMember(Member member) {
        log.info("피드5");

        deleteMember();
        log.info("피드6");

        this.member = Optional.ofNullable(member).orElse(this.member);
        log.info("피드7");

        log.info(this.member.getNickname());
        System.out.println(this);
        this.member.getFeeds().add(this);
        log.info("피드 스페셜");
    }

    private void deleteMember() {
        log.info("피드8");

        if (this.member != null) {
            log.info("피드9");
            if (this.member.getFeeds().contains(this)) {

                log.info("피드10");
                this.member.getFeeds().remove(this);
            }
        }
    }
}
