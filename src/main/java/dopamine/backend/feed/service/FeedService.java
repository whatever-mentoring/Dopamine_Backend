package dopamine.backend.feed.service;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challenge.repository.ChallengeRepository;
import dopamine.backend.challenge.response.ChallengeResponseDTO;
import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feed.repository.FeedRepository;
import dopamine.backend.feed.request.FeedEditDTO;
import dopamine.backend.feed.request.FeedRequestDTO;
import dopamine.backend.feed.response.FeedResponseDTO;
import dopamine.backend.feedImage.entity.FeedImage;
import dopamine.backend.feedImage.response.FeedImageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedService {

    private final FeedRepository feedRepository;
    private final ChallengeRepository challengeRepository;

    private Feed verifiedFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("존재하지 않는 피드입니다."));
    }

    @Transactional(readOnly = true)
    public FeedResponseDTO getFeed(Long feedId) {
        Feed feed = verifiedFeed(feedId);

        if(!feed.getFulfillYn()) throw new RuntimeException("기준이 미달된 피드입니다.");

        Challenge challenge = feed.getChallenge();
        ChallengeResponseDTO challengeResponseDTO = ChallengeResponseDTO.builder().title(challenge.getTitle()).subtitle(challenge.getSubtitle())
                .challengeGuide(challenge.getChallengeGuide()).challengeLevel(challenge.getChallengeLevel()).image(challenge.getImage()).build();

        FeedImage feedImage = feed.getFeedImage();
        FeedImageResponseDTO feedImageResponseDTO = FeedImageResponseDTO.builder().image1Url(feedImage.getImage1Url()).image2Url(feedImage.getImage2Url())
                .image3Url(feedImage.getImage3Url()).build();

        return FeedResponseDTO.builder().openYn(feed.getOpenYn()).content(feed.getContent()).memberId(feed.getMember().getMemberId())
                .challengeResponseDTO(challengeResponseDTO).feedImageResponseDTO(feedImageResponseDTO).build();
    }

    public void postFeed(FeedRequestDTO feedRequestDTO) {
        Challenge challenge = challengeRepository.findById(feedRequestDTO.getChallengeId()).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));

        // todo 멤버 가져오기 -> 카카오 어떻게 사용하는지?

        Feed feed = Feed.builder().content(feedRequestDTO.getContent()).openYn(feedRequestDTO.getOpenYn()).challenge(challenge).build();
        // todo 멤버 추가

        FeedImage feedImage = feed.getFeedImage();
        feedImage.changeFeedImage(feedRequestDTO.getFeedImageRequestDTO());

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
}
