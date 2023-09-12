package dopamine.backend.challenge.service;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challenge.mapper.ChallengeMapper;
import dopamine.backend.challenge.repository.ChallengeRepository;
import dopamine.backend.challenge.request.ChallengeEditDTO;
import dopamine.backend.challenge.request.ChallengeRequestDTO;
import dopamine.backend.challenge.response.ChallengeResponseDTO;
import dopamine.backend.challengemember.entity.ChallengeMember;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    private final MemberService memberService;

    private Challenge verifiedChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));
    }

    public void createChallenge(ChallengeRequestDTO challengeRequestDTO) {

        Challenge challenge = challengeMapper.challengeRequestDtoToChallenge(challengeRequestDTO);

        challengeRepository.save(challenge);
    }

    public void deleteChallenge(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        challenge.changeDelYn(true);
    }

    public void deleteChallengeHard(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        challengeRepository.delete(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO getChallenge(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(challenge);

        return challengeResponseDTO;
    }

    public void editChallenge(Long challengeId, ChallengeEditDTO challengeEditDTO) {
        Challenge challenge = verifiedChallenge(challengeId);

        challenge.changeChallenge(challengeEditDTO);
    }

    public List<ChallengeResponseDTO> todayChallenge(Long userId) {

        Member member = memberService.verifiedMember(userId);

        // 기존에 챌린지 받은 적 없음
        LocalDateTime existRefreshDate = member.getChallengeRefreshDate();
        if(existRefreshDate == null){

        }

        // todo 오늘의 챌린지 3개 아니면 재발급?

        // 오늘 날짜 조회
        LocalDateTime today = LocalDateTime.now();

        String todayInfo = today.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        String existInfo = existRefreshDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        List<ChallengeResponseDTO> challengeResponseList;
        // member 오늘의 챌린지 갱신일 != 오늘 날짜 -> 새로운 챌린지 3개 매핑 (member 엔티티의 challengeMembers 갱신)
        // 이때 필터는 기존 challenge x, 난이도별로 1개 씩 -> 상, 중, 하 대신 3개 뽑으려면 쿼리 3번
        if(!todayInfo.equals(existInfo)){
            List<Challenge> randomChallenges = challengeRepository.getRandomChallenges();
            challengeResponseList = randomChallenges.stream().map(challengeMapper::challengeToChallengeResponseDTO).collect(Collectors.toList());
        }

        // member 오늘의 챌린지 갱신일 = 오늘 날짜 -> challengeMembers에 저장된 챌린지 리턴
        else {
            List<ChallengeMember> challengeMembers = member.getChallengeMembers();
            challengeResponseList = challengeMembers.stream().map(challengeMember ->
                    challengeMapper.challengeToChallengeResponseDTO(challengeMember.getChallenge())
            ).collect(Collectors.toList());
        }

        return challengeResponseList;
    }
}
