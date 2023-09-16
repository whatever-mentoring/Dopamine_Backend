package dopamine.backend.domain.challengemember.repository;

import dopamine.backend.domain.challengemember.entity.ChallengeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {
}
