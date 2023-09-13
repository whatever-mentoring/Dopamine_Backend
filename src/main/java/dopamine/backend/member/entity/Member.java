package dopamine.backend.member.entity;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challengemember.entity.ChallengeMember;
import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.level.entity.Level;
import dopamine.backend.level.request.LevelEditDto;
import dopamine.backend.level.service.LevelService;
import dopamine.backend.member.request.MemberEditDto;
import dopamine.backend.member.request.MemberRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    private String kakaoId;

    @Column(unique = true)
    private String nickname;

    private String refreshToken;

    private int exp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ChallengeMember> challengeMembers = new ArrayList<>();

    /**
     * 생성자
     *
     * @param memberRequestDto
     */
    @Builder
    public Member(String kakaoId, String nickname, String refreshToken, int exp, Level level) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
        this.exp = exp;
        setLevel(level);
    }

    /**
     * 수정(UPDATE)
     *
     * @param memberEditDto
     */
    public void changeMember(String kakaoId, String nickname, String refreshToken, int exp, Level level) {
        this.kakaoId = Optional.ofNullable(kakaoId).orElse(this.kakaoId);
        this.nickname = Optional.ofNullable(nickname).orElse(this.nickname);
        this.refreshToken = Optional.ofNullable(refreshToken).orElse(this.refreshToken);
        this.exp = (exp != 0) ? exp : this.exp;
        setLevel(level);
    }

    // == 연관관계 편의 메소드 == //
    private void setLevel(Level level) {
        if (this.level != null) {
            if (this.level.getMembers().contains(this)) {
                this.level.getMembers().remove(this);
            }
        }
        this.level = Optional.ofNullable(level).orElse(this.level);
        this.level.getMembers().add(this);
    }
}