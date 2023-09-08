package dopamine.backend.level.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LevelResponseDto {
    private int levelId;
    private int levelNum;
    private String name;
    private String image;
    private int challengeCnt;
}
