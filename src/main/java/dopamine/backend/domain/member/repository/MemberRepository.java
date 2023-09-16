package dopamine.backend.domain.member.repository;

import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByKakaoId(@Param("kakaoId") String kakaoId);
    Optional<Member> findMemberByNickname(@Param("nickname") String nickname);
    List<Member> findAllByLevel(Level level);
}
