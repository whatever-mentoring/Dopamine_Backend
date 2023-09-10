package dopamine.backend.level.service;
import dopamine.backend.level.request.LevelRequestDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LevelInitService implements CommandLineRunner {
    private final LevelService levelService;

    public LevelInitService(LevelService levelService) {
        this.levelService = levelService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 레벨 1 자동 생성
        levelService.createLevel(
                LevelRequestDto
                        .builder()
                        .levelNum(1)
                        .name("새싹지키미")
                        .image("/url/testurl")
                        .challengeCnt(0)
                        .build()
        );
    }
}