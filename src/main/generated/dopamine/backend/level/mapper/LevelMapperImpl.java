package dopamine.backend.level.mapper;

import dopamine.backend.level.entity.Level;
import dopamine.backend.level.response.LevelResponseDto;
import dopamine.backend.level.response.LevelResponseDto.LevelResponseDtoBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-10T17:04:10+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class LevelMapperImpl implements LevelMapper {

    @Override
    public LevelResponseDto levelToLevelResponseDto(Level level) {
        if ( level == null ) {
            return null;
        }

        LevelResponseDtoBuilder levelResponseDto = LevelResponseDto.builder();

        if ( level.getLevelId() != null ) {
            levelResponseDto.levelId( level.getLevelId().intValue() );
        }
        levelResponseDto.levelNum( level.getLevelNum() );
        levelResponseDto.name( level.getName() );
        levelResponseDto.image( level.getImage() );
        levelResponseDto.challengeCnt( level.getChallengeCnt() );
        levelResponseDto.createdDate( level.getCreatedDate() );
        levelResponseDto.modifiedDate( level.getModifiedDate() );
        levelResponseDto.delYn( level.getDelYn() );

        return levelResponseDto.build();
    }
}
