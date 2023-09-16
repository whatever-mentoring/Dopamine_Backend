package dopamine.backend.domain.level.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LevelDetailResponseDto {
    private Long levelId;
    private int levelNum;
    private String name;
    private String badge;
    private int expRange;
    private int expMember;
    private int expPercent;
}
