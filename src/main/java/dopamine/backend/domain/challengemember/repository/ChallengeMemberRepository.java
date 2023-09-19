package dopamine.backend.domain.challengemember.repository;

import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.challengemember.entity.ChallengeMember;
import dopamine.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {

    Optional<ChallengeMember> findChallengeMemberByChallengeAndMember(Challenge challenge, Member member);
}
