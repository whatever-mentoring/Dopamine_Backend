package dopamine.backend.challenge.entity;

import dopamine.backend.challenge.request.ChallengeEditDTO;
import dopamine.backend.challengemember.entity.ChallengeMember;
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
    @Column(name = "challenge_id")
    private Long challengeId;

    private String title;

    private String subtitle;

    private String image;

    private String challengeGuide;

    private Integer challengeLevel;

    @OneToMany(mappedBy = "challenge")
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ChallengeMember> challengeMembers = new ArrayList<>();

    @Builder
    public Challenge(String title, String subtitle, String image, String challengeGuide, Integer challengeLevel) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.challengeGuide = challengeGuide;
        this.challengeLevel = challengeLevel;
    }

    public void changeChallenge(ChallengeEditDTO challengeEditDTO){
        this.title = challengeEditDTO.getTitle();
        this.subtitle = challengeEditDTO.getSubtitle();
        this.image = challengeEditDTO.getImage();
        this.challengeGuide = challengeEditDTO.getChallengeGuide();
        this.challengeLevel = challengeEditDTO.getChallengeLevel();
    }
}
