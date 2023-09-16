package dopamine.backend.domain.challenge.controller;

import dopamine.backend.domain.challenge.request.ChallengeEditDTO;
import dopamine.backend.domain.challenge.request.ChallengeRequestDTO;
import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import dopamine.backend.domain.challenge.service.ChallengeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = "챌린지 API")
public class ChallengeController {

    private final ChallengeService challengeService;

    /**
     * 챌린지 생성
     * @param challengeRequestDTO
     */
    @PostMapping("/challenges")
    public void createChallenge(@Valid @RequestBody ChallengeRequestDTO challengeRequestDTO){
        challengeService.createChallenge(challengeRequestDTO);
    }

    /**
     * 챌린지 삭제 (DB에서도)
     * @param challengeId
     */
    @DeleteMapping("/challenges/{challengeId}/hard")
    public void deleteChallengeHard(@PathVariable Long challengeId){
        challengeService.deleteChallengeHard(challengeId);
    }

    /**
     * 챌린지 삭제
     * @param challengeId
     */
    @DeleteMapping("/challenges/{challengeId}")
    public void deleteChallenge(@PathVariable Long challengeId){
        challengeService.deleteChallenge(challengeId);
    }

    /**
     * 챌린지 조회
     * @param challengeId
     * @return
     */
    @GetMapping("/challenges/{challengeId}")
    public ChallengeResponseDTO getChallenge(@PathVariable("challengeId") Long challengeId){
        return challengeService.getChallenge(challengeId);
    }

    /**
     * 챌린지 수정
     * @param challengeId
     * @param challengeEditDTO
     */
    @PutMapping("/challenges/{challengeId}")
    public void editChallenge(@PathVariable Long challengeId, @RequestBody ChallengeEditDTO challengeEditDTO){
        challengeService.editChallenge(challengeId, challengeEditDTO);
    }

    @GetMapping("/challenges/today-challenge/{userId}")
    public void todayChallenge(@PathVariable Long userId){
        challengeService.todayChallenge(userId);
    }
}
