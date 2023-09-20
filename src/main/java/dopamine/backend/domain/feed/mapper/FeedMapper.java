package dopamine.backend.domain.feed.mapper;

import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.feed.request.FeedRequestDTO;
import dopamine.backend.domain.feed.response.FeedResponseDTO;
import dopamine.backend.domain.feedLike.response.FeedLikeResponseDTO;
import dopamine.backend.domain.member.response.MemberResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedMapper {

    // FeedRequestDTO -> Feed
    Feed feedRequestDtoToFeed(FeedRequestDTO feedRequestDTO);

    // Feed -> FeedResponseDTO
    FeedResponseDTO feedToFeedResponseDto(Feed feed, ChallengeResponseDTO challengeResponseDTO, MemberResponseDto memberResponseDto, String badgeimage, List<FeedLikeResponseDTO> feedLikeResponseDTOList, boolean likePresent);
}
