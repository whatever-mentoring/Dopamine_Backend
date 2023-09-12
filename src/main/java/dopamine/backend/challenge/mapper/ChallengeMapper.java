package dopamine.backend.challenge.mapper;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challenge.request.ChallengeRequestDTO;
import dopamine.backend.challenge.response.ChallengeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChallengeMapper {

    // Challenge -> ChallengeResponseDTO
    ChallengeResponseDTO challengeToChallengeResponseDTO(Challenge challenge);

    // ChallengeRequestDTO -> Challenge
    Challenge challengeRequestDtoToChallenge(ChallengeRequestDTO challengeRequestDTO);
}
