package dopamine.backend.feed.mapper;

import dopamine.backend.challenge.response.ChallengeResponseDTO;
import dopamine.backend.feed.entity.Feed;
import dopamine.backend.feed.request.FeedRequestDTO;
import dopamine.backend.feed.response.FeedResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedMapper {

    // FeedRequestDTO -> Feed
    Feed feedRequestDtoToFeed(FeedRequestDTO feedRequestDTO);

    // Feed -> FeedResponseDTO
    FeedResponseDTO feedToFeedResponseDto(Feed feed, ChallengeResponseDTO challengeResponseDTO);
}
