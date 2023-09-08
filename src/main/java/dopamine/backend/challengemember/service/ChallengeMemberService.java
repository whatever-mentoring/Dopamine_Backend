package dopamine.backend.challengemember.service;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challenge.repository.ChallengeRepository;
import dopamine.backend.challenge.service.ChallengeService;
import dopamine.backend.challengemember.entity.ChallengeMember;
import dopamine.backend.challengemember.mapper.ChallengeMemberMapper;
import dopamine.backend.challengemember.repository.ChallengeMemberRepository;
import dopamine.backend.challengemember.request.ChallengeMemberEditDto;
import dopamine.backend.challengemember.request.ChallengeMemberRequestDto;
import dopamine.backend.challengemember.response.ChallengeMemberResponseDto;
import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeMemberService {

    private final ChallengeMemberRepository challengeMemberRepository;
    private final ChallengeMemberMapper challengeMemberMapper;
    private final MemberService memberService;
    private final ChallengeRepository challengeRepository;

    /**
     * CREATE : 생성
     * @param challengeMemberRequestDto
     */
    public ChallengeMemberResponseDto createChallengeMember(ChallengeMemberRequestDto challengeMemberRequestDto) {
        // create
        Member member = memberService.verifiedMember(challengeMemberRequestDto.getMemberId());
        Challenge challenge = challengeRepository.findById(challengeMemberRequestDto.getChallengeId()).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다.")); // todo : 나중에 메서드로 바꾸기
        ChallengeMember challengeMember = ChallengeMember.builder()
                .member(member)
                .challenge(challenge)
                .build();
        challengeMemberRepository.save(challengeMember);

        // ChallengeMember -> MemberResponseDto
        ChallengeMemberResponseDto challengeMemberResponseDto = challengeMemberMapper.challengeMemberToChallengeMemberResponseDto(challengeMember);
        return challengeMemberResponseDto;
    }

    /**
     * DELTE : 삭제
     * @param challengeMemberId
     */
    public void deleteChallengeMember(Long challengeMemberId) {
        ChallengeMember challengeMember = verifiedChallengeMember(challengeMemberId);
        challengeMemberRepository.delete(challengeMember);
    }

    /**
     * GET : 조회
     * @param challengeMemberId
     * @return challengeMemberResponseDto
     */
    @Transactional(readOnly = true)
    public ChallengeMemberResponseDto getChallengeMember(Long challengeMemberId) {
        ChallengeMember challengeMember = verifiedChallengeMember(challengeMemberId);
        ChallengeMemberResponseDto challengeMemberResponseDto = challengeMemberMapper.challengeMemberToChallengeMemberResponseDto(challengeMember);
        return challengeMemberResponseDto;
    }

    /**
     * UPDATE : 수정
     * @param challengeMemberId ChallengeMemberEditDto
     * @return challengeMemberResponseDto
     */
    public ChallengeMemberResponseDto editChallengeMember(Long challengeMemberId, ChallengeMemberEditDto challengeMemberEditDto) {

        // edit
        ChallengeMember challengeMember = verifiedChallengeMember(challengeMemberId);
        Member member = memberService.verifiedMember(challengeMemberEditDto.getMemberId());
        Challenge challenge = challengeRepository.findById(challengeMemberEditDto.getChallengeId()).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다.")); // todo : 나중에 메서드로 바꾸기

        challengeMember.changeChallengeMember(member, challenge);

        // challengeMember -> responseDto
        ChallengeMemberResponseDto challengeMemberResponseDto = challengeMemberMapper.challengeMemberToChallengeMemberResponseDto(challengeMember);
        return challengeMemberResponseDto;
    }

    /**
     * 검증 -> challengeMemberId 입력하면 관련 ChallengeMember Entity가 있는지 확인
     * @param challengeMemberId
     * @return challengeMember
     */
    public ChallengeMember verifiedChallengeMember(Long challengeMemberId) {
        Optional<ChallengeMember> challengeMember = challengeMemberRepository.findById(challengeMemberId);
        return challengeMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHALLENGEMEMBER_NOT_FOUND));
    }

}
