package dopamine.backend.feed.repository;

import dopamine.backend.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Optional<Feed> findFeedByContent(String content);
}
