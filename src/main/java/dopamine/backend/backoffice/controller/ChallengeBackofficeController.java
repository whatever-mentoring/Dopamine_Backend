package dopamine.backend.backoffice.controller;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.challenge.repository.ChallengeRepository;
import dopamine.backend.domain.challenge.request.ChallengeEditDTO;
import dopamine.backend.domain.challenge.request.ChallengeRequestDTO;
import dopamine.backend.domain.challenge.service.ChallengeService;
import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.level.request.LevelEditDto;
import dopamine.backend.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import java.util.List;


@Controller
@EnableWebMvc
@RequestMapping("/backoffice/challenge")
@RequiredArgsConstructor
@Slf4j
public class ChallengeBackofficeController {

    private final ChallengeRepository challengeRepository;
    private final ChallengeService challengeService;
    private final ImageService imageService;

    @GetMapping
    public String challenge(Model model) {
        List<Challenge> challenges = challengeRepository.findAll();
        model.addAttribute("challenges", challenges);
        return "challenge/challenge";
    }

    @GetMapping("/{challengeId}/delete")
    public String challengeDelete(@PathVariable("challengeId") Long challengeId) {
        challengeService.deleteChallengeHard(challengeId);
        return "redirect:/backoffice/challenge";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("form", new ChallengeRequestDTO());
        return "challenge/challengeCreate";
    }

    /**
     * 챌린지 생성
     * @param challengeRequestDTO
     */
    @PostMapping(value = "/create")
    public String createChallenge(ChallengeRequestDTO challengeRequestDTO,
                                @RequestPart(value = "file", required = false) MultipartFile file){

        if(!file.isEmpty()){
            challengeRequestDTO.setImage(imageService.updateImage(file, "challenge", "image"));
        }

        challengeService.createChallenge(challengeRequestDTO);
        return "redirect:/backoffice/challenge";
    }

    /**
     * 챌린지 수정
     * @param challengeId
     * @param model
     * @return
     */
    @GetMapping("/{challengeId}/update")
    public String challengeUpdate(@PathVariable("challengeId") Long challengeId,
                              Model model) {
        Challenge challenge = challengeService.verifiedChallenge(challengeId);
        model.addAttribute("title", challenge.getTitle());
        model.addAttribute("subtitle", challenge.getSubtitle());
        model.addAttribute("challengeGuide", challenge.getChallengeGuide());
        model.addAttribute("challengeLevel", challenge.getChallengeLevel());

        model.addAttribute("form", ChallengeEditDTO.builder()
                .title(challenge.getTitle())
                .subtitle(challenge.getSubtitle())
                .challengeGuide(challenge.getChallengeGuide())
                .challengeLevel(challenge.getChallengeLevel())
                .build()
        );
        return "challenge/challengeUpdate";
    }

    /**
     * 챌린지 수정
     * @param challengeId
     * @param challengeEditDTO
     * @param file
     * @return
     */
    @PostMapping("/{challengeId}/update")
    public String editLevel(@PathVariable("challengeId") Long challengeId,
                            ChallengeEditDTO challengeEditDTO,
                            @RequestPart("file") MultipartFile file) {
        // 이미지 업로드
        if (!file.isEmpty()) {
            challengeEditDTO.setImage(imageService.updateImage(file, "level", "badge"));
        }
        // 레벨 수정
        challengeService.editChallenge(challengeId, challengeEditDTO);
        return "redirect:/backoffice/challenge";
    }
}
