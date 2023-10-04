package dopamine.backend.domain.level.service;

import dopamine.backend.domain.member.repository.MemberRepository;
import dopamine.backend.domain.member.service.MemberService;
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
public class LevelService {

    private final LevelRepository levelRepository;
    private final LevelMapper levelMapper;
    private final MemberRepository memberRepository;

    public static int INF = 1000000000;

    /**
     * CREATE : 생성(name, exp, badge 설정 & levelNum 자동 설정)
     *
     * @param levelRequestDto
     */
    public Level createLevel(LevelRequestDto levelRequestDto) {

        // levelNum : 하나씩 증가하도록
        int levelNum = createLevelNum();

        // 이전 레벨보다 작거나, 다음 레벨보다 큰지 검증
        verifiedExp(levelNum, levelRequestDto.getExp());

        // create
        Level level = Level.builder()
                .levelNum(levelNum)
                .name(levelRequestDto.getName())
                .badge(levelRequestDto.getBadge())
                .exp(levelRequestDto.getExp()).build();

        levelRepository.save(level);
        allMemberLevelChange();

        return level;
    }

    /**
     * DELTE : 삭제
     *
     * @param levelId
     */
    public void deleteLevel(Long levelId) {

        // 레벨 찾기
        Level level = verifiedLevel(levelId);
        Level levelOne = findLevelByLevelNum(1);

        // 1레벨 삭제 불가(모든 회원의 default)
        if (level == levelOne) throw new BusinessLogicException(ExceptionCode.LEVEL_1_CANNOT_DELETE);

        // 레벨에 포함된 회원을 1레벨로 변경 후 삭제
        level.getMembers().forEach(member -> member.setLevel(levelOne));
        levelRepository.delete(level);

        // levelNum 정렬 & 관련 멤버의 레벨 변경
        sortLevelNum();
        allMemberLevelChange();
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
     * GET : 레벨 디테일하게 조회
     * @param member
     * @return
     */
    @Transactional(readOnly = true)
    public LevelDetailResponseDto getMemberDetailLevel(Member member) {
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

        // 모든 멤버에 레벨 반영
        allMemberLevelChange();

        // level -> responseDto
        LevelResponseDto levelResponseDto = levelMapper.levelToLevelResponseDto(level);
        return levelResponseDto;
    }

    /**
     * 검증 -> levelId 입력하면 관련 Level Entity가 있는지 확인
     * @param levelId
     * @return level
     */
    @Transactional(readOnly = true)
    public Level verifiedLevel(Long levelId) {
        Optional<Level> level = levelRepository.findById(levelId);
        return level.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEVEL_NOT_FOUND));
    }

    /**
     * levelNum을 기준으로 Level Entity 찾기
     * @param levelNum
     * @return Level
     */
    @Transactional(readOnly = true)
    public Level findLevelByLevelNum(int levelNum) {
        Optional<Level> level = levelRepository.findLevelByLevelNum(levelNum);
        return level.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEVEL_NOT_FOUND));
    }

    /**
     * DB에서 가장 큰 levelNum 조회 + 1 (순차적으로)
     */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    private void sortLevelNum() {
        int levelNum = 0;
        for (Level level : levelRepository.findAllByOrderByLevelNumAsc()) {
            level.changeLevelNum(++levelNum);
        }
    }

    /**
     * Level 변경시 모든 멤버의 레벨에 반영
     */
    private void allMemberLevelChange(){
        memberRepository.findAll().forEach(member -> member.setLevel(getMemberLevel(member.getExp())));
    }

    /**
     * 경험치에 속하는 Level 반환
     * @param exp
     * @return Level
     */
    @Transactional(readOnly = true)
    public Level getMemberLevel(int exp) {
        return levelRepository.findTopByExpLessThanEqualOrderByExpDesc(exp);
    }

}
