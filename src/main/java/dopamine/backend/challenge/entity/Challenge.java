package dopamine.backend.challenge.entity;

import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.feed.entity.Feed;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long challengeId;

    @Column
    private String title;

    @Column
    private String subtitle;

    @Column
    private String image;

    @OneToMany(mappedBy = "challenge")
    private List<Feed> feeds = new ArrayList<>();

    // todo 챌린지사용자 엔티티 연관관계 추가해야 하는가?

    @Builder
    public Challenge(Long challengeId, String title, String subtitle, String image) {
        this.challengeId = challengeId;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
    }
}
