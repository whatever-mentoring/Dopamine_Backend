package dopamine.backend.domain.level.service;
import dopamine.backend.domain.level.repository.LevelRepository;
import dopamine.backend.domain.level.request.LevelRequestDto;
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
                            .name("새싹지키미")
                            .badge("/url/testurl")
                            .exp(0)
                            .build()
            );
        });


    }
}