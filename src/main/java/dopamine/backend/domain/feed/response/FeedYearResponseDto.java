package dopamine.backend.domain.feed.response;

import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FeedYearResponseDto {

    private String yearMonth;
    private Boolean feedYn;
}
