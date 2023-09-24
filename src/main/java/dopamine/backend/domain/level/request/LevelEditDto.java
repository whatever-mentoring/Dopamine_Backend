package dopamine.backend.domain.level.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LevelEditDto {

    private String name;

    private String badge;

    private int exp;
}
