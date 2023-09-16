package dopamine.backend.domain.feedLike.repository;

import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.feedLike.entity.FeedLike;
import dopamine.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    Optional<FeedLike> findByMemberAndFeed(Member member, Feed feed);
    List<FeedLike> findByFeed(Feed feed);
}
