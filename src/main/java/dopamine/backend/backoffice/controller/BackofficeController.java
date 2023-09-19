package dopamine.backend.backoffice.controller;

import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.level.repository.LevelRepository;
import dopamine.backend.domain.level.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice")
@RequiredArgsConstructor
public class BackofficeController {

    private final LevelRepository levelRepository;
    private final LevelService levelService;

    @GetMapping
    public String home() {
        return "home/home";
    }

    @GetMapping("/challenge")
    public String challenge(Model model) {
        model.addAttribute("data", "안녕");
        return "challenge/challenge";
    }

    @GetMapping("/feed")
    public String feed(Model model) {
        model.addAttribute("data", "안녕");
        return "feed/feed";
    }

    @GetMapping("/level")
    public String level(Model model) {
        List<Level> levels = levelRepository.findAll();
        model.addAttribute("levels", levels);
        return "level/level";
    }

    @GetMapping("/level/{levelId}/delete")
    public String levelDelete(@PathVariable("levelId") Long levelId) {
        levelService.deleteLevel(levelId);
        return "redirect:/backoffice/level";
    }

    @GetMapping("/level/create")
    public String levelCreate(Model model) {
        List<Level> levels = levelRepository.findAll();
        model.addAttribute("levels", levels);
        return "level/levelCreate";
    }


}
