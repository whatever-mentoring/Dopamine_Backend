package dopamine.backend.level.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Builder
@Getter
public class LevelResponseDto {
    private int levelId;
    private int levelNum;
    private String name;
    private String image;
    private int challengeCnt;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String delYn;
}
