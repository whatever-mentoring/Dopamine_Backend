package dopamine.backend.domain.level.service;

import dopamine.backend.global.exception.BusinessLogicException;
import dopamine.backend.global.exception.ExceptionCode;
import dopamine.backend.domain.level.repository.LevelRepository;
import dopamine.backend.domain.level.request.LevelRequestDto;
import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.level.mapper.LevelMapper;
import dopamine.backend.domain.level.request.LevelEditDto;
import dopamine.backend.domain.level.response.LevelDetailResponseDto;
import dopamine.backend.domain.level.response.LevelResponseDto;
import dopamine.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LevelService {

    private final LevelRepository levelRepository;
    private final LevelMapper levelMapper;

    public static int INF = 1000000000;

    /**
     * CREATE : 생성(name, exp, badge 설정 & levelNum 자동 설정)
     *
     * @param levelRequestDto
     */
    public Level createLevel(LevelRequestDto levelRequestDto) {

        int levelNum = createLevelNum();
        verifiedExp(levelNum, levelRequestDto.getExp());

        // create
        Level level = Level.builder()
                .levelNum(levelNum)
                .name(levelRequestDto.getName())
                .badge(levelRequestDto.getBadge())
                .exp(levelRequestDto.getExp()).build();
        levelRepository.save(level);
        return level;
    }

    /**
     * DELTE : 삭제
     *
     * @param levelId
     */
    public void deleteLevel(Long levelId) {
        Level level = verifiedLevel(levelId);
        levelRepository.delete(level);
        orderLevelNum();
    }

    /**
     * GET : 조회
     *
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
     * UPDATE : 수정 (name, exp, badge설정 가능)
     *
     * @param levelId levelEditDto
     * @return levelResponseDto
     */
    public LevelResponseDto editLevel(Long levelId, LevelEditDto levelEditDto) {

        // edit
        Level level = verifiedLevel(levelId);
        if(levelEditDto.getExp() != 0) verifiedExp(level.getLevelNum(), levelEditDto.getExp());
        level.changeLevel(level.getLevelNum(), levelEditDto.getName(), levelEditDto.getBadge(), levelEditDto.getExp());

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

    /**
     * DB에서 가장 큰 levelNum 조회 + 1 (순차적으로)
     */
    public int createLevelNum(){
        return levelRepository.findTopByOrderByLevelNumDesc()
                .map(level -> level.getLevelNum() + 1)
                .orElse(1);
    }

    /**
     * 입력된 경험치 유효성 검증
     * @param levelNum
     * @param exp
     */
    public void verifiedExp(int levelNum, int exp) {
        // 이전 기준 경험치보다 큰지 검증
        int preExp = levelRepository.findLevelByLevelNum(levelNum - 1).map(Level::getExp).orElse(-1);
        if (preExp >= exp) {
            throw new BusinessLogicException(ExceptionCode.EXP_MIN_NOT_VALID);
        }

        // 이후 기준 경험치보다 작은지 검증
        int nextExp = levelRepository.findLevelByLevelNum(levelNum + 1).map(Level::getExp).orElse(INF);
        if (nextExp <= exp) {
            throw new BusinessLogicException(ExceptionCode.EXP_MAX_NOT_VALID);
        }
    }

    /**
     * levelNum연속으로 이어지도록 설정(삭제시 levelNum한칸씩 앞으로)
     */
    public void orderLevelNum() {
        List<Level> levels = levelRepository.findAllByOrderByLevelNumAsc();
        for (int i=0; i < levels.size() ; i++) {
            System.out.println(levels.get(i).getLevelId());
            levels.get(i).changeLevel(i+1, null, null, 0);
        }
    }

    public LevelDetailResponseDto memberDetailLevel(Member member) {
        int expRange = levelRepository.findLevelByLevelNum(member.getLevel().getLevelNum() + 1).map(Level::getExp).orElse(INF) - member.getLevel().getExp();
        int expMember = levelRepository.findLevelByLevelNum(member.getLevel().getLevelNum() + 1).map(Level::getExp).orElse(INF) - member.getExp() - 1;
        int expPercent = (int)Math.round((double) expMember/expRange * 100);

        return LevelDetailResponseDto.builder()
                .levelId(member.getLevel().getLevelId())
                .levelNum(member.getLevel().getLevelNum())
                .name(member.getLevel().getName())
                .badge(member.getLevel().getBadge())
                .expRange(expRange)
                .expMember(expMember)
                .expPercent(expPercent).build();
    }


}
