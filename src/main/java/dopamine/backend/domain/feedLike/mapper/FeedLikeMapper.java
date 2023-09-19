package dopamine.backend.domain.feedLike.mapper;

import dopamine.backend.domain.feedLike.entity.FeedLike;
import dopamine.backend.domain.feedLike.response.FeedLikeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedLikeMapper {

    // Feed -> FeedResponseDTO
    FeedLikeResponseDTO feedLikeToFeedLikeResponseDto(FeedLike feedLike);
}
