package dopamine.backend.challenge.repository;

import dopamine.backend.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM challenge ORDER BY RAND() LIMIT 3")
    List<Challenge> getRandomChallenges();
}
