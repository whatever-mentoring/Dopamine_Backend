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

    @OneToMany(mappedBy = "feed", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<FeedLike> feedLikeList = new ArrayList<>();

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
        deleteMember();
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getFeeds().add(this);
    }

    private void deleteMember() {
        if (this.member != null) {
            if (this.member.getFeeds().contains(this)) {
                this.member.getFeeds().remove(this);
            }
        }
    }
    public void setChallenge(Challenge challenge) {
        deleteChallenge();
        this.challenge = Optional.ofNullable(challenge).orElse(this.challenge);
        this.challenge.getFeeds().add(this);
    }

    private void deleteChallenge() {
        if (this.challenge != null) {
            if (this.challenge.getFeeds().contains(this)) {
                this.challenge.getFeeds().remove(this);
            }
        }
    }
}
