package dopamine.backend.feedLike.service;

import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feed.repository.FeedRepository;
import dopamine.backend.feedLike.entity.FeedLike;
import dopamine.backend.feedLike.repository.FeedLikeRepository;
import dopamine.backend.feedLike.response.FeedLikeResponseDTO;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;

    // service를 di 받는게 나은지, repo를 가져와서 메소드를 작성해주는게 나은지 -> memberserivce의 메소드를 쓰면 코드 의존이 높아질거 같다
    public Member verifiedMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private Feed verifiedFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("존재하지 않는 피드입니다."));
    }

    public void feedLike(Long feedId, Long memberId) {
        Member member = verifiedMember(memberId);
        Feed feed = verifiedFeed(feedId);

        Optional<FeedLike> findFeedLike = feedLikeRepository.findByMemberAndFeed(member, feed);
        if(findFeedLike.isPresent()) throw new RuntimeException("이미 존재하는 좋아요입니다.");

        FeedLike feedLike = FeedLike.builder().feed(feed).member(member).build();
        feedLikeRepository.save(feedLike);
    }

    public void feedLikeCancel(Long feedId, Long memberId) {
        Member member = verifiedMember(memberId);
        Feed feed = verifiedFeed(feedId);

        FeedLike feedLike = feedLikeRepository.findByMemberAndFeed(member, feed).orElseThrow(() -> new RuntimeException("존재하지 않는 좋아요입니다."));
        feedLikeRepository.delete(feedLike);
    }

    public List<FeedLikeResponseDTO> getFeedLikes(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        List<FeedLike> feedLikesbyFeed = feedLikeRepository.findByFeed(feed);

        return feedLikesbyFeed.stream().map((feedLike) -> {
            Member member = feedLike.getMember();
            return FeedLikeResponseDTO.builder().nickName(member.getNickname()).memberId(member.getMemberId()).build();
        }).collect(Collectors.toList());
    }
}
