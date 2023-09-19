package dopamine.backend.backoffice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Controller
@EnableWebMvc
@RequestMapping("/backoffice/challenge")
@RequiredArgsConstructor
public class ChallengeBackofficeController {
    @GetMapping
    public String challenge(Model model) {
        model.addAttribute("data", "안녕");
        return "challenge/challenge";
    }
}
