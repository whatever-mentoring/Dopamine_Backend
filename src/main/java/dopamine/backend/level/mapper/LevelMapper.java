package dopamine.backend.level.mapper;

import dopamine.backend.level.entity.Level;
import dopamine.backend.level.response.LevelResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LevelMapper {
    LevelResponseDto levelToLevelResponseDto(Level level);
}