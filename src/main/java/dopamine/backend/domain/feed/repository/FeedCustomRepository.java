package dopamine.backend.domain.feed.repository;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.member.entity.Member;

import java.util.List;

public interface FeedCustomRepository {
    public List<Feed> getFeedListOrderByDate(int page);

    public List<Feed> getFeedListOrderByLikeCount(int page);

    public List<Feed> getFeedListByChallengeOrderByLikeCount(Challenge challenge);

    public List<Feed> getFeedListByMemberOrderByDate(int page, Member member);
}
