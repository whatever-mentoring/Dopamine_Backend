package dopamine.backend.level.controller;

import dopamine.backend.level.entity.Level;
import dopamine.backend.level.mapper.LevelMapper;
import dopamine.backend.level.request.LevelEditDto;
import dopamine.backend.level.request.LevelRequestDto;
import dopamine.backend.level.response.LevelResponseDto;
import dopamine.backend.level.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/levels")
public class LevelController {

    private final LevelService levelService;
    private final LevelMapper levelMapper;

    // CREATE : 생성
    @PostMapping
    public LevelResponseDto createLevel(@Valid @RequestBody LevelRequestDto levelRequestDto) {
        // todo : levelNum 중복되지 않도록 만들어야 함
        Level level = levelService.createLevel(levelRequestDto);
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
    public LevelResponseDto editLevel(@Positive @PathVariable("level-id") Long levelId,
                                      @Valid @RequestBody LevelEditDto levelEditDto) {
        // todo : levelNum 중복되지 않도록 만들어야 함
        return levelService.editLevel(levelId, levelEditDto);
    }
}
