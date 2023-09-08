package dopamine.backend.member.entity;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.level.entity.Level;
import dopamine.backend.level.request.LevelEditDto;
import dopamine.backend.level.service.LevelService;
import dopamine.backend.member.request.MemberEditDto;
import dopamine.backend.member.request.MemberRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long memberId;

    private Long kakaoId;

    private String nickname;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;

    /**
     * 생성자
     * @param memberRequestDto
     */
    @Builder
    public Member(MemberRequestDto memberRequestDto) {
        this.memberId = memberRequestDto.getMemberId();
        this.kakaoId = memberRequestDto.getKakaoId();
        this.nickname = memberRequestDto.getNickname();
        this.refreshToken = memberRequestDto.getRefreshToken();
        setLevel(memberRequestDto.getLevel());
    }

    /**
     * 수정(UPDATE)
     * @param memberEditDto
     */
    public Member changeMember(MemberEditDto memberEditDto) {
        this.memberId = memberEditDto.getMemberId();
        this.kakaoId = memberEditDto.getKakaoId();
        this.nickname = memberEditDto.getNickname();
        this.refreshToken = memberEditDto.getRefreshToken();
        setLevel(memberEditDto.getLevel());
        return this;
    }

    // == 연관관계 편의 메소드 == //
    private void setLevel(Level level) {
        this.level = level;
        level.getMembers().add(this);
    }


}