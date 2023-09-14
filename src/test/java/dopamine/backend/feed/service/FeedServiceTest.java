package dopamine.backend.feed.service;

import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feed.mapper.FeedMapper;
import dopamine.backend.feed.repository.FeedRepository;
import dopamine.backend.feed.response.FeedResponseDTO;
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
        Feed feed11 = Feed.builder().content("11").build();

        feedRepository.saveAll(List.of(feed1,feed2,feed3,feed4,feed5,feed6,feed7,feed8,feed9,feed10));
        feedRepository.save(feed11);

        // when
        List<FeedResponseDTO> feedResponseDTOS = feedService.feedListOrderByDate(1);

        // then;
        assertThat(feedResponseDTOS.stream().map(FeedResponseDTO::getContent).collect(Collectors.toList())).contains("11");
        assertThat(feedResponseDTOS.size()).isEqualTo(10);
    }
}