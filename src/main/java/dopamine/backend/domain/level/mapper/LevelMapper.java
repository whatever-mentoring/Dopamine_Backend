package dopamine.backend.domain.level.mapper;

import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.level.response.LevelResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LevelMapper {
    LevelResponseDto levelToLevelResponseDto(Level level);
}