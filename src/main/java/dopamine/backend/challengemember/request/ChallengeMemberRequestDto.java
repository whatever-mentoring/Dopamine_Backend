package dopamine.backend.challengemember.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ChallengeMemberRequestDto {

    private Long memberId;
    private Long challengeId;
}
