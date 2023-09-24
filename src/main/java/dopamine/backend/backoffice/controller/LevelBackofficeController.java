package dopamine.backend.backoffice.controller;

import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.level.repository.LevelRepository;
import dopamine.backend.domain.level.request.LevelEditDto;
import dopamine.backend.domain.level.request.LevelRequestDto;
import dopamine.backend.domain.level.response.LevelResponseDto;
import dopamine.backend.domain.level.service.LevelService;
import dopamine.backend.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice/level")
@RequiredArgsConstructor
public class LevelBackofficeController {

    private final LevelRepository levelRepository;
    private final LevelService levelService;
    private final ImageService imageService;

    /**
     * READ : 레벨 확인
     *
     * @param model
     * @return
     */
    @GetMapping
    public String level(Model model) {
        List<Level> levels = levelRepository.findAll();
        model.addAttribute("levels", levels);
        return "level/level";
    }

    /**
     * DELETE : 레벨 삭제
     *
     * @param levelId
     * @return
     */
    @GetMapping("/{levelId}/delete")
    public String levelDelete(@PathVariable("levelId") Long levelId) {
        levelService.deleteLevel(levelId);
        return "redirect:/backoffice/level";
    }

    @GetMapping("/{levelId}/update")
    public String levelUpdate(@PathVariable("levelId") Long levelId,
                              Model model) {
        Level level = levelService.verifiedLevel(levelId);
        model.addAttribute("levelNum", level.getLevelNum());
        model.addAttribute("levelId", level.getLevelId());
        model.addAttribute("form", LevelEditDto.builder()
                .name(level.getName())
                .badge(level.getBadge())
                .exp(level.getExp()).build()
        );
        return "level/levelUpdate";
    }

    /**
     * UPDATE : 레벨 수정
     *
     * @param levelId
     * @return
     */
    @PostMapping("/{levelId}/update")
    public String editLevel(@PathVariable("levelId") Long levelId,
                            LevelEditDto levelEditDto,
                            @RequestPart("file") MultipartFile file) {
        // 이미지 업로드
        if (!file.isEmpty()) {
            levelEditDto.setBadge(imageService.updateImage(file, "level", "badge"));
        }
        // 레벨 수정
        levelService.editLevel(levelId, levelEditDto);
        return "redirect:/backoffice/level";
    }

    /**
     * GET : 레벨 생성 화면
     *
     * @param model
     * @return
     */
    @GetMapping("/create")
    public String levelCreate(Model model) {
        model.addAttribute("levelNum", levelService.createLevelNum());
        model.addAttribute("form", new LevelRequestDto());
        return "level/levelCreate";
    }

    /**
     * CREATE : 레벨 생성
     *
     * @param levelRequestDto
     * @param file
     * @return
     */
    @PostMapping("/create")
    public String levelCreate(LevelRequestDto levelRequestDto,
                              @RequestPart("file") MultipartFile file) {
        // 이미지 업로드
        if (!file.isEmpty()) {
            levelRequestDto.setBadge(imageService.updateImage(file, "level", "badge"));
        }

        // 레벨 생성
        Level level = levelService.createLevel(levelRequestDto);

        return "redirect:/backoffice/level";
    }
}
