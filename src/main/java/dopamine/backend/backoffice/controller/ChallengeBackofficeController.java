package dopamine.backend.backoffice.controller;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.challenge.repository.ChallengeRepository;
import dopamine.backend.domain.challenge.request.ChallengeRequestDTO;
import dopamine.backend.domain.challenge.service.ChallengeService;
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
}
