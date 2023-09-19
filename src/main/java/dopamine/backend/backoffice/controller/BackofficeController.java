package dopamine.backend.backoffice.controller;

import dopamine.backend.backoffice.form.LevelForm;
import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.level.repository.LevelRepository;
import dopamine.backend.domain.level.request.LevelRequestDto;
import dopamine.backend.domain.level.response.LevelResponseDto;
import dopamine.backend.domain.level.service.LevelService;
import dopamine.backend.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice")
@RequiredArgsConstructor
public class BackofficeController {

    private final LevelRepository levelRepository;
    private final LevelService levelService;
    private final ImageService imageService;

    @GetMapping
    public String home() {
        return "home/home";
    }

}
