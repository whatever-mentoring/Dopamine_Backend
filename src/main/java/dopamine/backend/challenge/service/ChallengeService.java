package dopamine.backend.challenge.service;

import dopamine.backend.challenge.entity.Challenge;
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

    public void createChallenge(ChallengeRequestDTO challengeRequestDTO) {

        // Todo : Exception Code 메서드로 정의하면 코드가 더 깔끔해질 것 같아요(아래 코드에서 중복되는 부분이 많네요 제 코드에서 verifiedLevel 메서드 참고해주세요)
        Challenge challenge = Challenge.builder().title(challengeRequestDTO.getTitle()).subtitle(challengeRequestDTO.getSubtitle())
                .challengeGuide(challengeRequestDTO.getChallengeGuide()).challengeLevel(challengeRequestDTO.getChallengeLevel()).image(challengeRequestDTO.getImage()).build();

        challengeRepository.save(challenge);
    }

    public void deleteChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));

        challenge.changeDelYn(true);
    }

    public void deleteChallengeHard(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));

        challengeRepository.delete(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO getChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));

        ChallengeResponseDTO challengeResponseDTO = ChallengeResponseDTO.builder().title(challenge.getTitle()).subtitle(challenge.getSubtitle())
                .challengeGuide(challenge.getChallengeGuide()).challengeLevel(challenge.getChallengeLevel()).image(challenge.getImage()).build();

        return challengeResponseDTO;
    }

    public void editChallenge(Long challengeId, ChallengeEditDTO challengeEditDTO) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));

        challenge.changeChallenge(challengeEditDTO);
    }
}
