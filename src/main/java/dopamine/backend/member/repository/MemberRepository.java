package dopamine.backend.member.repository;

import dopamine.backend.level.entity.Level;
import dopamine.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

<<<<<<< HEAD
    Optional<Member> findMemberByKakaoId(@Param("kakaoId") String kakaoId);
    Optional<Member> findMemberByNickname(@Param("nickname") String nickname);
    List<Member> findAllByLevel(Level level);

=======
    Optional<Member> findMemberByKakaoId(String kakaoId);
    Optional<Member> findMemberByNickname(String nickname);
>>>>>>> feature/karmapol/feedlist
}
