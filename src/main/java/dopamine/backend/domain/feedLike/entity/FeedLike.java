package dopamine.backend.domain.feedLike.entity;

import dopamine.backend.domain.common.entity.BaseEntity;
import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class FeedLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feedlike_id")
    private Long feedlikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public FeedLike(Member member, Feed feed) {
        this.member = member;
        this.feed = feed;
    }

    //== 연관관계 매핑 ==//
    public void setMember(Member member) {
        deleteMember();
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getFeedLikes().add(this);
    }

    private void deleteMember() {
        if (this.member != null) {
            if (this.member.getFeedLikes().contains(this)) {
                this.member.getFeedLikes().remove(this);
            }
        }
    }

    public void setFeed(Feed feed) {
        deleteFeed();
        this.feed = Optional.ofNullable(feed).orElse(this.feed);
        this.feed.getFeedLikeList().add(this);
    }

    private void deleteFeed() {
        if (this.feed != null) {
            if (this.feed.getFeedLikeList().contains(this)) {
                this.feed.getFeedLikeList().remove(this);
            }
        }
    }
}
