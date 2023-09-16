package dopamine.backend.domain.feed.repository;

import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Optional<Feed> findFeedByContent(String content);
    List<Feed> findFeedByMember(Member member);
}
