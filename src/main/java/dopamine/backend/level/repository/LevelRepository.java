package dopamine.backend.level.repository;

import dopamine.backend.level.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.AbstractDocument;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    Optional<Level> findLevelByLevelNum(int levelNum);
}
