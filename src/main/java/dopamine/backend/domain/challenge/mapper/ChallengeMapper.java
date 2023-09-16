package dopamine.backend.domain.challenge.mapper;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.challenge.request.ChallengeRequestDTO;
import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChallengeMapper {

    // Challenge -> ChallengeResponseDTO
    ChallengeResponseDTO challengeToChallengeResponseDTO(Challenge challenge);

    // ChallengeRequestDTO -> Challenge
    Challenge challengeRequestDtoToChallenge(ChallengeRequestDTO challengeRequestDTO);
}
