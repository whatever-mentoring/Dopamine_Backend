package dopamine.backend.feed.service;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challenge.entity.ChallengeLevel;
import dopamine.backend.challenge.mapper.ChallengeMapper;
import dopamine.backend.challenge.repository.ChallengeRepository;
import dopamine.backend.challenge.response.ChallengeResponseDTO;
import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feed.mapper.FeedMapper;
import dopamine.backend.feed.repository.FeedRepository;
import dopamine.backend.feed.request.FeedEditDTO;
import dopamine.backend.feed.request.FeedRequestDTO;
import dopamine.backend.feed.response.FeedResponseDTO;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedService {

    private final MemberService memberService;

    private final FeedRepository feedRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;
    private final FeedMapper feedMapper;

    private Feed verifiedFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("존재하지 않는 피드입니다."));
    }

    @Transactional(readOnly = true)
    public FeedResponseDTO getFeed(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        if(!feed.getFulfillYn()) throw new RuntimeException("기준이 미달된 피드입니다.");

        Challenge challenge = feed.getChallenge();
        ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(challenge);

        return feedMapper.feedToFeedResponseDto(feed, challengeResponseDTO);
    }

    public void postFeed(FeedRequestDTO feedRequestDTO) {
        Challenge challenge = challengeRepository.findById(feedRequestDTO.getChallengeId()).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));

        Member member = memberService.verifiedMember(feedRequestDTO.getMemberId());

        Feed feed = feedMapper.feedRequestDtoToFeed(feedRequestDTO);
        feed.setChallenge(challenge);
        feed.setMember(member);

        // member의 exp추가 & level 반영
        int exp = getExpByChallengeLevel(feed.getChallenge().getChallengeLevel());
        memberService.plusMemberExp(member, exp);

        feedRepository.save(feed);
    }

    public void editFeed(Long feedId, FeedEditDTO feedEditDTO) {
        Feed feed = verifiedFeed(feedId);

        feed.changeFeed(feedEditDTO);
    }

    public void deleteFeed(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        feed.changeDelYn(true);
    }

    public void deleteFeedHard(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        feedRepository.delete(feed);
    }

    /**
     * ChallengeLevel에 따른 exp 추출
     * HIGH : 20
     * MID : 10
     * LOW : 5
     */
    private int getExpByChallengeLevel(ChallengeLevel challengeLevel) {
        if (challengeLevel == ChallengeLevel.HIGH) {
            return 30;
        } else if (challengeLevel == challengeLevel.MID) {
            return 10;
        } else {
            return 5;
        }
    }
}
