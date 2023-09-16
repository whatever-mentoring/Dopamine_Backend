package dopamine.backend.feed.service;

import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.feed.mapper.FeedMapper;
import dopamine.backend.domain.feed.repository.FeedRepository;
import dopamine.backend.domain.feed.response.FeedResponseDTO;
import dopamine.backend.domain.feed.service.FeedService;
import dopamine.backend.domain.feedLike.service.FeedLikeService;
import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.level.repository.LevelRepository;
import dopamine.backend.domain.level.request.LevelRequestDto;
import dopamine.backend.domain.level.service.LevelService;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.domain.member.repository.MemberRepository;
import dopamine.backend.domain.member.request.MemberRequestDto;
import dopamine.backend.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class FeedServiceTest {

    @Autowired
    private FeedService feedService;
    @Autowired
    private LevelService levelService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FeedLikeService feedLikeService;

    @Autowired
    private FeedMapper feedMapper;

    @Test
    @DisplayName("인증글 리스트 - 최신순")
    void feedlistbydate(){

        // given
        Feed feed1 = Feed.builder().content("1").build();
        Feed feed2 = Feed.builder().content("2").build();
        Feed feed3 = Feed.builder().content("3").build();
        Feed feed4 = Feed.builder().content("4").build();
        Feed feed5 = Feed.builder().content("5").build();
        Feed feed6 = Feed.builder().content("6").build();
        Feed feed7 = Feed.builder().content("7").build();
        Feed feed8 = Feed.builder().content("8").build();
        Feed feed9 = Feed.builder().content("9").build();
        Feed feed10 = Feed.builder().content("10").build();

        feedRepository.saveAll(List.of(feed1,feed2,feed3,feed4,feed5,feed6,feed7,feed8,feed9));
        feedRepository.save(feed10);

        // when
        List<FeedResponseDTO> feedResponseDTOS = feedService.feedListOrderByDate(1);

        // then;
        assertThat(feedResponseDTOS.stream().map(FeedResponseDTO::getContent).collect(Collectors.toList())).contains("10");
        assertThat(feedResponseDTOS.size()).isEqualTo(9);
    }

    @Test
    @DisplayName("인증글 리스트 - 좋아요 순")
    void feedlistbylikecount(){

        // given
        MemberRequestDto requestDto = MemberRequestDto.builder().nickname("test").build();
        LevelRequestDto levelRequestDto = LevelRequestDto.builder().name("testLev").exp(5).build();
        Level level = levelService.createLevel(levelRequestDto);
        Member member = memberService.createMember(requestDto);

        levelRepository.save(level);
        memberRepository.save(member);

        Feed feed1 = Feed.builder().content("1").build();
        Feed feed2 = Feed.builder().content("2").build();
        Feed feed3 = Feed.builder().content("3").build();
        Feed feed4 = Feed.builder().content("4").build();
        Feed feed5 = Feed.builder().content("5").build();
        Feed feed6 = Feed.builder().content("6").build();
        Feed feed7 = Feed.builder().content("7").build();
        Feed feed8 = Feed.builder().content("8").build();
        Feed feed9 = Feed.builder().content("9").build();

        feedRepository.saveAll(List.of(feed1,feed2,feed3,feed4,feed5,feed6,feed7,feed8,feed9));

        Member findMember = memberRepository.findMemberByNickname("test").orElseThrow(() -> new RuntimeException("찾을 수 없음"));
        Long memberId = findMember.getMemberId();

        Feed feed = feedRepository.findFeedByContent("9").orElseThrow(() -> new RuntimeException("찾을 수 없음"));
        Long feedId = feed.getFeedId();

        // when
        feedLikeService.feedLike(feedId, memberId);
        List<FeedResponseDTO> feedResponseDTOS = feedService.feedListOrderByLikeCount(1);

        // then
        assertThat(feedResponseDTOS.get(0).getContent()).isEqualTo("9");
        assertThat(feedResponseDTOS.size()).isEqualTo(9);
    }

    @Test
    @DisplayName("인증글 리스트 - 사용자기준 최신 순")
    void feedlistbymember(){

        // given
        MemberRequestDto requestDto = MemberRequestDto.builder().nickname("test").build();
        LevelRequestDto levelRequestDto = LevelRequestDto.builder().name("testLev").exp(5).build();
        Level level = levelService.createLevel(levelRequestDto);
        Member member = memberService.createMember(requestDto);

        levelRepository.save(level);
        memberRepository.save(member);

        Feed feed1 = Feed.builder().content("1").member(member).build();
        Feed feed2 = Feed.builder().content("2").member(member).build();
        Feed feed3 = Feed.builder().content("3").member(member).build();
        Feed feed4 = Feed.builder().content("4").member(member).build();
        Feed feed5 = Feed.builder().content("5").member(member).build();
        Feed feed6 = Feed.builder().content("6").member(member).build();
        Feed feed7 = Feed.builder().content("7").member(member).build();
        Feed feed8 = Feed.builder().content("8").member(member).build();
        Feed feed9 = Feed.builder().content("9").member(member).build();
        Feed feed10 = Feed.builder().content("10").member(member).build();

        Feed feed11 = Feed.builder().content("11").build();
        Feed feed12 = Feed.builder().content("12").build();
        Feed feed13 = Feed.builder().content("13").build();
        Feed feed14 = Feed.builder().content("14").build();
        Feed feed15 = Feed.builder().content("15").build();
        Feed feed16 = Feed.builder().content("16").build();
        Feed feed17 = Feed.builder().content("17").build();
        Feed feed18 = Feed.builder().content("18").build();
        Feed feed19 = Feed.builder().content("19").build();

        feedRepository.saveAll(List.of(feed1,feed2,feed3,feed4,feed5,feed6,feed7,feed8,feed9,feed11,feed12,feed13,feed14,feed15,feed16,feed17,feed18,feed19));
        feedRepository.save(feed10);

        // when
        Member findMember = memberRepository.findMemberByNickname("test").orElseThrow(() -> new RuntimeException("찾을 수 없음"));
        Long memberId = findMember.getMemberId();
        List<FeedResponseDTO> feedResponseDTOS = feedService.feedListByMember(memberId, 1);

        // then
        assertThat(feedResponseDTOS.stream().map(FeedResponseDTO::getContent).collect(Collectors.toList())).contains("10","2","3","4","5","6","7","8","9");
        assertThat(feedResponseDTOS.size()).isEqualTo(9);
    }

}