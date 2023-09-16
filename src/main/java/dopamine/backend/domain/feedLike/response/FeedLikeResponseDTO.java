package dopamine.backend.domain.feedLike.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedLikeResponseDTO {
    private final Long memberId;
    private final String nickName;
}
