package dopamine.backend.domain.level.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LevelRequestDto {

    private String name;

    private String badge;

    private int exp;
}
