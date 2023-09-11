package dopamine.backend.level.service;
import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.level.repository.LevelRepository;
import dopamine.backend.level.request.LevelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LevelInitService implements CommandLineRunner {
    private final LevelService levelService;
    private final LevelRepository levelRepository;

    public LevelInitService(LevelService levelService, LevelRepository levelRepository) {
        this.levelService = levelService;
        this.levelRepository = levelRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 레벨 1 자동 생성
        levelRepository.findLevelByLevelNum(1).orElseGet(() -> {
            return levelService.createLevel(
                    LevelRequestDto
                            .builder()
                            .levelNum(1)
                            .name("새싹지키미")
                            .image("/url/testurl")
                            .challengeCnt(0)
                            .build()
            );
        });


    }
}