package dopamine.backend.level.entity;

import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.level.request.LevelEditDto;
import dopamine.backend.level.request.LevelRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Level extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "level_id")
    private Long levelId;

    private int levelNum;

    private String name;

    private String image;

    private int challengeCnt;


    @Builder
    public Level(LevelRequestDto levelRequestDto) {
        this.levelNum = levelRequestDto.getLevelNum();
        this.name = levelRequestDto.getName();
        this.image = levelRequestDto.getImage();
        this.challengeCnt = levelRequestDto.getChallengeCnt();
    }

    public void changeLevel(LevelEditDto levelEditDto) {
        this.levelNum = levelEditDto.getLevelNum();
        this.name = levelEditDto.getName();
        this.image = levelEditDto.getImage();
        this.challengeCnt = levelEditDto.getChallengeCnt();
    }

}
