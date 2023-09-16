package dopamine.backend.domain.challengemember.entity;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.common.entity.BaseEntity;
import dopamine.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("false")
    private Boolean certificationYn;

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

    public void deleteChallengeMember(){
        deleteMember();
        deleteChallenge();
    }

    public void setCertificationYn(Boolean certificationYn){
        this.certificationYn = certificationYn;
    }

    // == 연관관계 편의 메소드 == //
    private void setMember(Member member) {
        deleteMember();
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getChallengeMembers().add(this);
    }

    private void deleteMember() {
        if (this.member != null) {
            if (this.member.getChallengeMembers().contains(this)) {
                this.member.getChallengeMembers().remove(this);
            }
        }
    }

    private void setChallenge(Challenge challenge) {
        deleteChallenge();
        this.challenge = Optional.ofNullable(challenge).orElse(this.challenge);
        this.challenge.getChallengeMembers().add(this);
    }

    private void deleteChallenge() {
        if (this.challenge != null) {
            if (this.challenge.getChallengeMembers().contains(this)) {
                this.challenge.getChallengeMembers().remove(this);
            }
        }
    }
}
