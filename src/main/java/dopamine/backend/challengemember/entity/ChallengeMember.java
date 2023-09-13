package dopamine.backend.challengemember.entity;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challengemember.request.ChallengeMemberEditDto;
import dopamine.backend.challengemember.request.ChallengeMemberRequestDto;
import dopamine.backend.common.entity.BaseEntity;
import dopamine.backend.level.entity.Level;
import dopamine.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeMember extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "challenge_member_id")
    private Long challengeMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;


    /**
     * 생성자
     * @param member
     * @param challenge
     */
    @Builder
    public ChallengeMember(Member member, Challenge challenge) {
        setMember(member);
        setChallenge(challenge);
    }


    /**
     * UPDATE : 수정
     * @param member
     * @param challenge
     */
    public void changeChallengeMember(Member member, Challenge challenge) {
        setMember(member);
        setChallenge(challenge);
    }

    // == 연관관계 편의 메소드 == //
    private void setMember(Member member) {
        if (this.member != null) {
            if (this.member.getChallengeMembers().contains(this)) {
                this.member.getChallengeMembers().remove(this);
            }
        }
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getChallengeMembers().add(this);
    }

    private void setChallenge(Challenge challenge) {
        if (this.challenge != null) {
            if (this.challenge.getChallengeMembers().contains(this)) {
                this.challenge.getChallengeMembers().remove(this);
            }
        }
        this.challenge = Optional.ofNullable(challenge).orElse(this.challenge);
        this.challenge.getChallengeMembers().add(this);
    }



}
