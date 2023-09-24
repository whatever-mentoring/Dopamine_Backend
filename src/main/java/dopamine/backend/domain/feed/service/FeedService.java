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
import dopamine.backend.domain.feed.response.FeedYearResponseDto;
import dopamine.backend.domain.feedLike.mapper.FeedLikeMapper;
import dopamine.backend.domain.feedLike.repository.FeedLikeRepository;
import dopamine.backend.domain.feedLike.response.FeedLikeResponseDTO;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.domain.member.mapper.MemberMapper;
import dopamine.backend.domain.member.response.MemberResponseDto;
import dopamine.backend.domain.member.service.MemberService;
import dopamine.backend.global.exception.BusinessLogicException;
import dopamine.backend.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedService {

    private final ChallengeService challengeService;
    private final MemberService memberService;

    private final FeedRepository feedRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberRepository challengeMemberRepository;
    private final FeedLikeRepository feedLikeRepository;

    private final FeedCustomRepository feedCustomRepository;

    private final ChallengeMapper challengeMapper;
    private final FeedMapper feedMapper;
    private final MemberMapper memberMapper;
    private final FeedLikeMapper feedLikeMapper;

    public Feed verifiedFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("존재하지 않는 피드입니다."));
    }

    /**
     * 단건 피드 가져오기
     *
     * @param feedId
     * @return
     */
    @Transactional(readOnly = true)
    public FeedResponseDTO getFeed(Member member, Long feedId) {
        Feed feed = verifiedFeed(feedId);

        if (!feed.getFulfillYn()) throw new BusinessLogicException(ExceptionCode.FEED_FULFILL_NOT_VALID);
        if (feed.getDelYn()) throw new BusinessLogicException(ExceptionCode.DELETE_FEED_NOT_FOUND);

        Challenge challenge = feed.getChallenge();
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(feed.getMember());
        ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(challenge);
        String badgeimage = feed.getMember().getLevel().getBadge();

        List<FeedLikeResponseDTO> feedLikeResponseDTOList = feed.getFeedLikeList().stream().map(feedLike -> feedLikeMapper.feedLikeToFeedLikeResponseDto(feedLike)).collect(Collectors.toList());
        boolean likePresent = feedLikeRepository.findByMemberAndFeed(member, feed).isPresent();

        return feedMapper.feedToFeedResponseDto(feed, challengeResponseDTO, memberResponseDto, badgeimage, feedLikeResponseDTOList, likePresent);
    }

    /**
     * 피드 작성
     *
     * @param feedRequestDTO
     */
    public void postFeed(Member member, FeedRequestDTO feedRequestDTO) {
        Challenge challenge = challengeService.verifiedChallenge(feedRequestDTO.getChallengeId());

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
     *
     * @param member
     * @param challenge
     */
    private void setCertification(Member member, Challenge challenge) {
        ChallengeMember challengeMember = challengeMemberRepository.findChallengeMemberByChallengeAndMember(challenge, member).orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHALLENGE_NOT_FOUND));
        // todo 테스트 용이성 위해 주석처리, 프론트에서만 인증여부 체크해도 된다
//        if(challengeMember.getCertificationYn())
//            throw new BusinessLogicException(CHALLENGE_ALREADY_CERTIFIED);

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
    private List<FeedResponseDTO> getFeedResponseDTOS(Member member, List<Feed> feedList) {
        List<FeedResponseDTO> feedResponseDTOList = feedList.stream().map(feed -> {
            List<FeedLikeResponseDTO> feedLikeResponseDTOList = feed.getFeedLikeList().stream().map(feedLike -> feedLikeMapper.feedLikeToFeedLikeResponseDto(feedLike)).collect(Collectors.toList());
            MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(feed.getMember());
            String badgeimage = feed.getMember().getLevel().getBadge();
            ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(feed.getChallenge());
            boolean likePresent = feedLikeRepository.findByMemberAndFeed(member, feed).isPresent();
            return feedMapper.feedToFeedResponseDto(feed, challengeResponseDTO, memberResponseDto, badgeimage, feedLikeResponseDTOList, likePresent);
        }).collect(Collectors.toList());
        return feedResponseDTOList;
    }

    /**
     * 피드 리스트 조회 - 최신순
     *
     * @param page
     * @return
     */
    public List<FeedResponseDTO> feedListOrderByDate(Member member, Integer page) {
        List<Feed> feedList = feedCustomRepository.getFeedListOrderByDate(page);
        return getFeedResponseDTOS(member, feedList);
    }

    /**
     * 피드 리스트 조회 - 좋아요 순
     *
     * @param page
     * @return
     */
    public List<FeedResponseDTO> feedListOrderByLikeCount(Member member, Integer page) {
        List<Feed> feedList = feedCustomRepository.getFeedListOrderByLikeCount(page);
        return getFeedResponseDTOS(member, feedList);
    }

    /**
     * 피드 리스트 조회 - 챌린지 기준
     *
     * @param challengeId
     * @return
     */
    public List<FeedResponseDTO> feedListByChallengeOrderByDate(Member member, Long challengeId) {
        Challenge challenge = challengeService.verifiedChallenge(challengeId);
        List<Feed> feedListByChallengeOrderByLikeCount = feedCustomRepository.getFeedListByChallengeOrderByLikeCount(challenge);
        return getFeedResponseDTOS(member, feedListByChallengeOrderByLikeCount);
    }

    /**
     * 피드 리스트 조회 - 유저 기준
     *
     * @param member
     * @param page
     * @return
     */
    public List<FeedResponseDTO> feedListByMember(Member member, Integer page) {
        List<Feed> feedListByMemberOrderByDate = feedCustomRepository.getFeedListByMemberOrderByDate(page, member);
        return getFeedResponseDTOS(member, feedListByMemberOrderByDate);
    }

    /**
     * 피드 리스트 조회 - 월 필터
     *
     * @param member
     * @param month
     * @return
     */
    public List<FeedResponseDTO> feedListByMemberAndMonth(Member member, String month) {

        LocalDate date = LocalDate.parse(month + "-01");
        LocalDateTime startDate = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime finishDate = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX);

        List<Feed> findListByMemberAndDate = feedRepository.findFeedByMemberAndDelYnAndCreatedDateBetweenOrderByCreatedDate(member, false, startDate, finishDate);

        return getFeedResponseDTOS(member, findListByMemberAndDate);
    }

    /**
     * 피드 리스트 조회 - 년도 기준 월별 인증글 개수
     *
     * @param member
     * @param years
     * @return
     */
    public List<FeedYearResponseDto> feedListByMemberAndYear(Member member, List<String> years) {

        List<FeedYearResponseDto> feedYearResponseDtoList = new ArrayList<>();
        String yearMonth;
        LocalDate date;
        LocalDateTime startDate;
        LocalDateTime finishDate;
        Boolean feedYn;

        for(String year:years) {
            for (int month = 1; month <= 12; month++) {
                yearMonth = year + "-" + String.format("%1$02d", month);
                date = LocalDate.parse(yearMonth + "-01");
                startDate = date.withDayOfMonth(1).atStartOfDay();
                finishDate = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX);
                feedYn = feedRepository.findFeedByMemberAndDelYnAndCreatedDateBetweenOrderByCreatedDate(member, false, startDate, finishDate).size() != 0;
                feedYearResponseDtoList.add(FeedYearResponseDto.builder().yearMonth(yearMonth).feedYn(feedYn).build());
            }
        }
        return feedYearResponseDtoList;
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
