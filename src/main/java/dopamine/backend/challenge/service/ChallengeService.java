package dopamine.backend.challenge.service;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challenge.mapper.ChallengeMapper;
import dopamine.backend.challenge.repository.ChallengeRepository;
import dopamine.backend.challenge.request.ChallengeEditDTO;
import dopamine.backend.challenge.request.ChallengeRequestDTO;
import dopamine.backend.challenge.response.ChallengeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    private Challenge verifiedChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));
    }

    public void createChallenge(ChallengeRequestDTO challengeRequestDTO) {

        Challenge challenge = challengeMapper.challengeRequestDtoToChallenge(challengeRequestDTO);

        challengeRepository.save(challenge);
    }

    public void deleteChallenge(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        challenge.changeDelYn(true);
    }

    public void deleteChallengeHard(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        challengeRepository.delete(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO getChallenge(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(challenge);

        return challengeResponseDTO;
    }

    public void editChallenge(Long challengeId, ChallengeEditDTO challengeEditDTO) {
        Challenge challenge = verifiedChallenge(challengeId);

        challenge.changeChallenge(challengeEditDTO);
    }
}
