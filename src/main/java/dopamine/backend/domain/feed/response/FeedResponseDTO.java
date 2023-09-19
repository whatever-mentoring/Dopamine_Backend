package dopamine.backend.domain.feed.response;

import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import dopamine.backend.domain.member.response.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class FeedResponseDTO {
    private Long feedId;

    private String content;

    private String image1Url;

    private String image2Url;

    private String image3Url;

    private Boolean openYn;

    private Boolean fulfillYn;

    private LocalDateTime createdDate;

    private MemberResponseDto memberResponseDto;

    private String badgeimage;

    private ChallengeResponseDTO challengeResponseDTO;
}
