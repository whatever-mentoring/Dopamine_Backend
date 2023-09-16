package dopamine.backend.domain.level.entity;

import dopamine.backend.domain.common.entity.BaseEntity;
import dopamine.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(unique = true)
    private int levelNum;

    private String name;

    private String badge;

    private int exp;

    @OneToMany(mappedBy = "level", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Member> members = new ArrayList<>();

    @Builder
    public Level(int levelNum, String name, String badge, int exp) {
        this.levelNum = levelNum;
        this.name = name;
        this.badge = badge;
        this.exp = exp;
    }

    public void changeLevel(int levelNum, String name, String badge, int exp) {
        this.levelNum = (levelNum != 0) ? levelNum : this.levelNum;
        this.name = Optional.ofNullable(name).orElse(this.name);
        this.badge = Optional.ofNullable(badge).orElse(this.badge);
        this.exp = (exp != 0) ? exp : this.exp;
    }

}
