package dopamine.backend.domain.feed.repository;

import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findFeedByMemberAndDelYn(Member member, Boolean delYn);

    public List<Feed> findFeedByMemberAndDelYnAndCreatedDateBetweenOrderByCreatedDate(Member member, Boolean delYn, LocalDateTime startDate, LocalDateTime finishDate);
}
