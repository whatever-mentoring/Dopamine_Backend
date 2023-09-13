package dopamine.backend.level.controller;

import dopamine.backend.level.entity.Level;
import dopamine.backend.level.mapper.LevelMapper;
import dopamine.backend.level.request.LevelEditDto;
import dopamine.backend.level.request.LevelRequestDto;
import dopamine.backend.level.response.LevelResponseDto;
import dopamine.backend.level.service.LevelService;
import dopamine.backend.member.entity.Member;
import dopamine.backend.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/levels")
public class LevelController {

    private final LevelService levelService;
    private final LevelMapper levelMapper;
    private final ImageService imageService;

    // CREATE : 생성
    @PostMapping
    public LevelResponseDto createLevel(
            @RequestPart(value = "request") LevelRequestDto levelRequestDto,
            @RequestPart(value = "badge", required = false) MultipartFile file) {

        // 이미지 업로드
        if (file != null) {
            levelRequestDto.setBadge(imageService.updateImage(file, "level", "badge"));
        }

        // 레벨 생성
        Level level = levelService.createLevel(levelRequestDto);

        // Response
        LevelResponseDto levelResponseDto = levelMapper.levelToLevelResponseDto(level);

        return levelResponseDto;
    }

    // DELETE : 삭제
    @DeleteMapping("/{level-id}")
    public void deleteLevel(@Positive @PathVariable("level-id") Long levelId) {
        levelService.deleteLevel(levelId);
    }

    // GET : 조회
    @GetMapping("/{level-id}")
    public LevelResponseDto getLevel(@Positive @PathVariable("level-id") Long levelId) {
        return levelService.getLevel(levelId);
    }

    // UPDATE : 수정
    @PutMapping("/{level-id}")
    public LevelResponseDto editLevel(
            @Positive @PathVariable("level-id") Long levelId,
            @RequestPart(value = "request") LevelEditDto levelEditDto,
            @RequestPart(value = "badge", required = false) MultipartFile file) {

        // 이미지 업로드
        if (file != null) {
            levelEditDto.setBadge(imageService.updateImage(file, "level", "badge"));
        }

        return levelService.editLevel(levelId, levelEditDto);
    }
}
