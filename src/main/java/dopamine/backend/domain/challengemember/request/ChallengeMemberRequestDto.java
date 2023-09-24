package dopamine.backend.domain.challengemember.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeMemberRequestDto {

    private Long memberId;
    private Long challengeId;
}
