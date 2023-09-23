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

    /**
     * 피드 리스트 조회 - 최신순
     * @param page
     * @return
     */
    @Override
    public List<Feed> getFeedListOrderByDate(int page) {
        return jpaQueryFactory.selectFrom(feed)
                .where(feed.fulfillYn.eq(true),
                        feed.delYn.ne(true))
                .orderBy(feed.createdDate.desc())
                .limit(9)
                .offset((long) (page - 1) * PAGEOFFSET)
                .fetch();
    }

    /**
     * 피드 리스트 조회 - 좋아요순
     * @param page
     * @return
     */
    @Override
    public List<Feed> getFeedListOrderByLikeCount(int page) {
        LocalDateTime dayOffset = LocalDateTime.now().minusDays(30L);

        return jpaQueryFactory.selectFrom(feed)
                .where(feed.createdDate.after(dayOffset),
                        feed.fulfillYn.eq(true),
                        feed.delYn.ne(true))
                .orderBy(feed.likeCount.desc())
                .limit(9)
                .offset((long) (page - 1) * PAGEOFFSET)
                .fetch();
    }

    /**
     * 피드 리스트 조회 - 챌린지 기준, 좋아요 순
     * @param challenge
     * @return
     */
    @Override
    public List<Feed> getFeedListByChallengeOrderByLikeCount(Challenge challenge) {
        LocalDateTime dayOffset = LocalDateTime.now().minusDays(30L);

        return jpaQueryFactory.selectFrom(feed)
                .where(feed.createdDate.after(dayOffset),
                        feed.challenge.eq(challenge),
                        feed.fulfillYn.eq(true),
                        feed.delYn.ne(true))
                .orderBy(feed.likeCount.desc())
                .limit(6)
                .fetch();
    }

    /**
     * 피드 리스트 조회 - 멤버 기준, 최신순
     * @param page, member
     * @return
     */
    @Override
    public List<Feed> getFeedListByMemberOrderByDate(int page, Member member) {
        return jpaQueryFactory.selectFrom(feed)
                .where(feed.member.eq(member),
                        feed.fulfillYn.eq(true),
                        feed.delYn.ne(true))
                .orderBy(feed.createdDate.desc())
                .limit(9)
                .offset((long) (page - 1) * PAGEOFFSET)
                .fetch();
    }
}
