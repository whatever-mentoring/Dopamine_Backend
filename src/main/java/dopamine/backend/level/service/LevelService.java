package dopamine.backend.level.service;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.level.entity.Level;
import dopamine.backend.level.mapper.LevelMapper;
import dopamine.backend.level.repository.LevelRepository;
import dopamine.backend.level.request.LevelEditDto;
import dopamine.backend.level.request.LevelRequestDto;
import dopamine.backend.level.response.LevelResponseDto;
import dopamine.backend.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {

    private final LevelRepository levelRepository;
    private final LevelMapper levelMapper;

    /**
     * CREATE : 생성
     * @param levelRequestDto
     */
    public Level createLevel(LevelRequestDto levelRequestDto) {
        // create
        Level level = Level.builder().levelRequestDto(levelRequestDto).build();
        levelRepository.save(level);
        return level;
    }

    /**
     * DELTE : 삭제
     * @param levelId
     */
    public void deleteLevel(Long levelId) {
        Level level = verifiedLevel(levelId);
        levelRepository.delete(level);
    }

    /**
     * GET : 조회
     * @param levelId
     * @return levelResponseDto
     */
    @Transactional(readOnly = true)
    public LevelResponseDto getLevel(Long levelId) {
        Level level = verifiedLevel(levelId);
        LevelResponseDto levelResponseDto = levelMapper.levelToLevelResponseDto(level);
        return levelResponseDto;
    }

    /**
     * UPDATE : 수정
     * @param levelId levelEditDto
     * @return levelResponseDto
     */
    public LevelResponseDto editLevel(Long levelId, LevelEditDto levelEditDto) {
        // edit
        Level level = verifiedLevel(levelId);
        level.changeLevel(levelEditDto);

        // level -> responseDto
        LevelResponseDto levelResponseDto = levelMapper.levelToLevelResponseDto(level);
        return levelResponseDto;
    }

    /**
     * 검증 -> levelId 입력하면 관련 ChallengeMember Entity가 있는지 확인
     * @param levelId
     * @return level
     */
    public Level verifiedLevel(Long levelId) {
        Optional<Level> level = levelRepository.findById(levelId);
        return level.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEVEL_NOT_FOUND));
    }

    public Level findMemberByLevelNum(int levelNum) {
        Optional<Level> level = levelRepository.findLevelByLevelNum(levelNum);
        return level.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEVEL_NOT_FOUND));
    }

}
