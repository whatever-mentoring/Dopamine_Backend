package dopamine.backend.feed.repository;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.feed.entity.Feed;
import dopamine.backend.member.entity.Member;

import java.util.List;

public interface FeedCustomRepository {
    public List<Feed> getFeedListOrderByDate(int page);

    public List<Feed> getFeedListOrderByLikeCount(int page);

    public List<Feed> getFeedListByChallengeOrderByLikeCount(Challenge challenge);

    public List<Feed> getFeedListByMemberOrderByDate(int page, Member member);
}
