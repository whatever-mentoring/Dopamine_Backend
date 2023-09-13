package dopamine.backend.member.repository;

import dopamine.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByKakaoId(String kakaoId);
    Optional<Member> findMemberByNickname(String nickname);
}
