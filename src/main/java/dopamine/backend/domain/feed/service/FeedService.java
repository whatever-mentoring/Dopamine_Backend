package dopamine.backend.domain.feed.service;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.challenge.mapper.ChallengeMapper;
import dopamine.backend.domain.challenge.repository.ChallengeRepository;
import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import dopamine.backend.domain.challenge.service.ChallengeService;
import dopamine.backend.domain.challengemember.entity.ChallengeMember;
import dopamine.backend.domain.challengemember.repository.ChallengeMemberRepository;
import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.feed.mapper.FeedMapper;
import dopamine.backend.domain.feed.repository.FeedCustomRepository;
import dopamine.backend.domain.feed.repository.FeedRepository;
import dopamine.backend.domain.feed.request.FeedEditDTO;
import dopamine.backend.domain.feed.request.FeedRequestDTO;
import dopamine.backend.domain.feed.response.FeedResponseDTO;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.domain.member.service.MemberService;
import dopamine.backend.global.exception.BusinessLogicException;
import dopamine.backend.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedService {

    private final ChallengeService challengeService;
    private final MemberService memberService;

    private final FeedRepository feedRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberRepository challengeMemberRepository;

    private final FeedCustomRepository feedCustomRepository;

    private final ChallengeMapper challengeMapper;
    private final FeedMapper feedMapper;

    private Feed verifiedFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
    }

    /**
     * 단건 피드 가져오기
     *
     * @param feedId
     * @return
     */
    @Transactional(readOnly = true)
    public FeedResponseDTO getFeed(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        if (!feed.getFulfillYn()) throw new BusinessLogicException(ExceptionCode.FEED_FULFILL_NOT_VALID);

        Challenge challenge = feed.getChallenge();
        ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(challenge);

        return feedMapper.feedToFeedResponseDto(feed, challengeResponseDTO);
    }

    /**
     * 피드 작성
     *
     * @param feedRequestDTO
     */
    public void postFeed(Member member, FeedRequestDTO feedRequestDTO) {
        Challenge challenge = challengeRepository.findById(feedRequestDTO.getChallengeId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHALLENGE_NOT_FOUND));

        Feed feed = feedMapper.feedRequestDtoToFeed(feedRequestDTO);
        feed.setChallenge(challenge);
        feed.setMember(member);

        setCertification(member, challenge);

        int exp = feed.getChallenge().getChallengeLevel().getExp();

        memberService.plusMemberExp(member, exp);

        feedRepository.save(feed);
    }

    /**
     * 인증 여부 갱신
     * @param member
     * @param challenge
     */
    private void setCertification(Member member, Challenge challenge) {
        ChallengeMember challengeMember = challengeMemberRepository.findChallengeMemberByChallengeAndMember(challenge, member).orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHALLENGE_NOT_FOUND));
        challengeMember.setCertificationYn(true);
    }

    /**
     * 피드 수정
     *
     * @param feedId
     * @param feedEditDTO
     */
    public void editFeed(Long feedId, FeedEditDTO feedEditDTO) {
        Feed feed = verifiedFeed(feedId);

        feed.changeFeed(feedEditDTO);
    }

    /**
     * 피드 삭제 처리
     *
     * @param feedId
     */
    public void deleteFeed(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        feed.changeDelYn(true);
    }

    /**
     * 피드 완전 삭제 (DB)
     *
     * @param feedId
     */
    public void deleteFeedHard(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        feed.deleteFromChallenge();

        feedRepository.delete(feed);
    }

    /**
     * 피드 리스트 변환 List<feed> -> List<feedResponseDTO>
     *
     * @param feedList
     * @return
     */
    private List<FeedResponseDTO> getFeedResponseDTOS(List<Feed> feedList) {
        List<FeedResponseDTO> feedResponseDTOList = feedList.stream().map(feed -> {
            ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(feed.getChallenge());
            return feedMapper.feedToFeedResponseDto(feed, challengeResponseDTO);
        }).collect(Collectors.toList());
        return feedResponseDTOList;
    }

    /**
     * 피드 리스트 조회 - 최신순
     *
     * @param page
     * @return
     */
    public List<FeedResponseDTO> feedListOrderByDate(Integer page) {
        List<Feed> feedList = feedCustomRepository.getFeedListOrderByDate(page);
        return getFeedResponseDTOS(feedList);
    }

    /**
     * 피드 리스트 조회 - 좋아요 순
     *
     * @param page
     * @return
     */
    public List<FeedResponseDTO> feedListOrderByLikeCount(Integer page) {
        List<Feed> feedList = feedCustomRepository.getFeedListOrderByLikeCount(page);
        return getFeedResponseDTOS(feedList);
    }

    /**
     * 피드 리스트 조회 - 챌린지 기준
     *
     * @param challengeId
     * @return
     */
    public List<FeedResponseDTO> feedListByChallengeOrderByDate(Long challengeId) {
        Challenge challenge = challengeService.verifiedChallenge(challengeId);
        List<Feed> feedListByChallengeOrderByLikeCount = feedCustomRepository.getFeedListByChallengeOrderByLikeCount(challenge);
        return getFeedResponseDTOS(feedListByChallengeOrderByLikeCount);
    }

    /**
     * 피드 리스트 조회 - 유저 기준
     *
     * @param memberId
     * @param page
     * @return
     */
    public List<FeedResponseDTO> feedListByMember(Long memberId, Integer page) {
        Member member = memberService.verifiedMember(memberId);
        List<Feed> feedListByMemberOrderByDate = feedCustomRepository.getFeedListByMemberOrderByDate(page, member);
        return getFeedResponseDTOS(feedListByMemberOrderByDate);
    }

    /**
     * 피드 리스트 조회 - 월 필터
     * @param member
     * @param month
     * @return
     */
    public List<FeedResponseDTO> feedListByMemberAndMonth(Member member, String month) {

        LocalDate date = LocalDate.parse(month + "-01");
        LocalDateTime startDate = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime finishDate = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX);

        List<Feed> findListByMemberAndDate = feedRepository.findFeedByMemberAndCreatedDateBetweenOrderByCreatedDate(member, startDate, finishDate);

        return getFeedResponseDTOS(findListByMemberAndDate);
    }


    /**
     * 인증 미달 여부 변경
     *
     * @param feedId
     * @param value
     */
    public void patchFeedFulfill(Long feedId, Boolean value) {
        Feed feed = verifiedFeed(feedId);

        feed.setFulfillYn(value);
    }
}
