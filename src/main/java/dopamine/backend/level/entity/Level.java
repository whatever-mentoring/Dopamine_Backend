package dopamine.backend.level.entity;

import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.level.request.LevelEditDto;
import dopamine.backend.level.request.LevelRequestDto;
import dopamine.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @OneToMany(mappedBy = "level", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Member> members = new ArrayList<>();

    @Builder
    public Level(LevelRequestDto levelRequestDto) {
        this.levelNum = levelRequestDto.getLevelNum();
        this.name = levelRequestDto.getName();
        this.image = levelRequestDto.getImage();
        this.challengeCnt = levelRequestDto.getChallengeCnt();
    }

    public void changeLevel(LevelEditDto levelEditDto) {
        this.levelNum = (levelEditDto.getLevelNum() != 0) ? levelEditDto.getLevelNum() : this.levelNum;
        this.name = Optional.ofNullable(levelEditDto.getName()).orElse(this.name);
        this.image = Optional.ofNullable(levelEditDto.getImage()).orElse(this.image);
        this.challengeCnt = (levelEditDto.getChallengeCnt() != 0) ? levelEditDto.getChallengeCnt() : this.challengeCnt;
    }

}
