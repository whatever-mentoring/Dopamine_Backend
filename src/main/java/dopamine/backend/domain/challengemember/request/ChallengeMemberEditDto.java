package dopamine.backend.domain.challengemember.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ChallengeMemberEditDto {

    private Long memberId;
    private Long challengeId;
}
