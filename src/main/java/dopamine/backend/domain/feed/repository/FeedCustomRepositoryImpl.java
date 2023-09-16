package dopamine.backend.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static dopamine.backend.domain.feed.entity.QFeed.feed;


@Repository
@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final Integer PAGEOFFSET = 9;

    @Override
    public List<Feed> getFeedListOrderByDate(int page) {
        return jpaQueryFactory.selectFrom(feed)
                .orderBy(feed.createdDate.desc())
                .limit(9)
                .offset((long) (page - 1) * PAGEOFFSET)
                .fetch();
    }

    @Override
    public List<Feed> getFeedListOrderByLikeCount(int page) {
        LocalDateTime dayOffset = LocalDateTime.now().minusDays(30L);

        return jpaQueryFactory.selectFrom(feed)
                .where(feed.createdDate.after(dayOffset))
                .orderBy(feed.likeCount.desc())
                .limit(9)
                .offset((long) (page - 1) * PAGEOFFSET)
                .fetch();
    }

    @Override
    public List<Feed> getFeedListByChallengeOrderByLikeCount(Challenge challenge) {
        LocalDateTime dayOffset = LocalDateTime.now().minusDays(30L);

        return jpaQueryFactory.selectFrom(feed)
                .where(feed.createdDate.after(dayOffset),
                        feed.challenge.eq(challenge))
                .orderBy(feed.likeCount.desc())
                .limit(6)
                .fetch();
    }

    @Override
    public List<Feed> getFeedListByMemberOrderByDate(int page, Member member) {
        return jpaQueryFactory.selectFrom(feed)
                .where(feed.member.eq(member))
                .orderBy(feed.createdDate.desc())
                .limit(9)
                .offset((long) (page - 1) * PAGEOFFSET)
                .fetch();
    }
}
