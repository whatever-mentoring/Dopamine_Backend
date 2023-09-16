package dopamine.backend.domain.level.repository;

import dopamine.backend.domain.level.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    Optional<Level> findLevelByLevelNum(int levelNum);
    Optional<Level> findTopByOrderByLevelNumDesc();
    List<Level> findAllByOrderByLevelNumAsc();
    Level findTopByExpLessThanEqualOrderByExpDesc(int exp);
}
