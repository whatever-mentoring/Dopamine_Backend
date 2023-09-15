package dopamine.backend.feed.service;

import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feed.mapper.FeedMapper;
import dopamine.backend.feed.repository.FeedRepository;
import dopamine.backend.feed.response.FeedResponseDTO;
import dopamine.backend.feedLike.service.FeedLikeService;
import dopamine.backend.level.entity.Level;
import dopamine.backend.level.repository.LevelRepository;
import dopamine.backend.level.request.LevelRequestDto;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.repository.MemberRepository;
import dopamine.backend.member.request.MemberRequestDto;
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
        LevelRequestDto levelRequestDto = LevelRequestDto.builder().name("testLev").build();
        Level level = Level.builder().levelRequestDto(levelRequestDto).build();
        Member member = Member.builder().memberRequestDto(requestDto).level(level).build();

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

        // then;
        assertThat(feedResponseDTOS.get(0)).isEqualTo(feed9);
        assertThat(feedResponseDTOS.size()).isEqualTo(9);
    }
}