package dopamine.backend.feed.repository;

import dopamine.backend.feed.entity.Feed;

import java.util.List;

public interface FeedCustomRepository {
    public List<Feed> getFeedListOrderByDate(int page);
}
