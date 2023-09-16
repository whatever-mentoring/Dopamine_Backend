package dopamine.backend.domain.level.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class LevelEditDto {

    private String name;

    private String badge;

    private int exp;
}
