package dopamine.backend.domain.challenge.controller;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.challenge.request.ChallengeEditDTO;
import dopamine.backend.domain.challenge.request.ChallengeRequestDTO;
import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
import dopamine.backend.domain.challenge.service.ChallengeService;
import dopamine.backend.global.s3.service.ImageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "챌린지 API")
public class ChallengeController {

    private final ChallengeService challengeService;

    private final ImageService imageService;

    /**
     * 챌린지 생성
     * @param challengeRequestDTO
     */
    @PostMapping(value = "/challenges", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createChallenge(@Valid @RequestPart(value = "request") ChallengeRequestDTO challengeRequestDTO,
                                @RequestPart(value = "image", required = false) MultipartFile file){
        if(file != null){
            challengeRequestDTO.setImage(imageService.updateImage(file, "challenge", "image"));
        }

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
    @PutMapping(value = "/challenges/{challengeId}", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void editChallenge(@PathVariable Long challengeId, @RequestPart(value = "request") ChallengeEditDTO challengeEditDTO,
                              @RequestPart(value = "image", required = false) MultipartFile file){
        if(file != null){
            challengeEditDTO.setImage(imageService.updateImage(file, "challenge", "image"));
        }

        challengeService.editChallenge(challengeId, challengeEditDTO);
    }

    /**
     * 오늘의 챌린지
     * @param userId
     */
    @GetMapping("/challenges/today-challenge/{userId}")
    public List<ChallengeResponseDTO> todayChallenge(@PathVariable Long userId){
        return challengeService.todayChallenge(userId);
    }
}
