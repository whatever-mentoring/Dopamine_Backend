package dopamine.backend.feedLike.entity;

import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.feed.entity.Feed;
import dopamine.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
