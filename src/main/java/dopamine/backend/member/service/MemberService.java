package dopamine.backend.member.service;

import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.level.entity.Level;
import dopamine.backend.level.service.LevelService;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.repository.MemberRepository;
import dopamine.backend.member.request.MemberEditDto;
import dopamine.backend.member.request.MemberRequestDto;
import dopamine.backend.member.response.MemberResponseDto;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final LevelService levelService;

    /**
     * CREATE : 생성
     * @param memberRequestDto
     */
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {
        // create
        Level level = levelService.verifiedLevel(memberRequestDto.getLevelId());
        Member member = Member.builder()
                .memberRequestDto(memberRequestDto)
                .level(level)
                .build();
        memberRepository.save(member);

        // Member -> MemberResponseDto
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
        return memberResponseDto;
    }

    /**
     * DELTE : 삭제
     * @param memberId
     */
    public void deleteMember(Long memberId) {
        Member member = verifiedMember(memberId);
        memberRepository.delete(member);
    }

    /**
     * GET : 조회
     * @param memberId
     * @return memberResponseDto
     */
    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long memberId) {
        Member member = verifiedMember(memberId);
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
        return memberResponseDto;
    }

    /**
     * UPDATE : 수정
     * @param memberId memberEditDto
     * @return memberResponseDto
     */
    public MemberResponseDto editMember(Long memberId, MemberEditDto memberEditDto) {
        // edit
        Level level = levelService.verifiedLevel(memberEditDto.getLevelId());
        Member member = verifiedMember(memberId);
        member.changeMember(memberEditDto, level);

        // member -> responseDto
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
        return memberResponseDto;
    }

    /**
     * 검증 -> memberId 입력하면 관련 Member Entity가 있는지 확인
     * @param memberId
     * @return member
     */
    public Member verifiedMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

}
