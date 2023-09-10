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
    @Column(name="member_id")
    private Long memberId;

    private String kakaoId;

    private String nickname;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ChallengeMember> challengeMembers = new ArrayList<>();

    /**
     * 생성자
     * @param memberRequestDto
     */
    @Builder
    public Member(MemberRequestDto memberRequestDto, Level level) {
        this.kakaoId = memberRequestDto.getKakaoId();
        this.nickname = memberRequestDto.getNickname();
        this.refreshToken = memberRequestDto.getRefreshToken();
        setLevel(level);
    }

    /**
     * 수정(UPDATE)
     * @param memberEditDto
     */
    public void changeMember(MemberEditDto memberEditDto) {
        this.kakaoId = Optional.ofNullable(memberEditDto.getKakaoId()).orElse(this.kakaoId);
        this.nickname = Optional.ofNullable(memberEditDto.getNickname()).orElse(this.nickname);
        this.refreshToken = Optional.ofNullable(memberEditDto.getRefreshToken()).orElse(this.refreshToken);
        setLevel(memberEditDto.getLevel());
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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