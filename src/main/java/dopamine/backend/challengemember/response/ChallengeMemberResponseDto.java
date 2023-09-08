package dopamine.backend.challengemember.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChallengeMemberResponseDto {
    private Long challengeMemberId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String delYn;
}
