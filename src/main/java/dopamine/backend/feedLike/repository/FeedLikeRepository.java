package dopamine.backend.feedLike.repository;

import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feedLike.entity.FeedLike;
import dopamine.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    Optional<FeedLike> findByMemberAndFeed(Member member, Feed feed);
    List<FeedLike> findByFeed(Feed feed);
}
