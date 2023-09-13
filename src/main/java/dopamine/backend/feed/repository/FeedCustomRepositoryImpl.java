package dopamine.backend.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dopamine.backend.feed.entity.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dopamine.backend.feed.entity.QFeed.feed;

@Repository
@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Feed> getFeedListOrderByDate(int page) {
        return jpaQueryFactory.selectFrom(feed)
                .orderBy(feed.createdDate.desc())
                .limit(10)
                .offset((long) (page - 1) * 10)
                .fetch();
    }
}
