package dopamine.backend.member.service;

import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.level.entity.Level;
import dopamine.backend.level.repository.LevelRepository;
import dopamine.backend.level.service.LevelService;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.repository.MemberRepository;
import dopamine.backend.member.request.MemberEditDto;
import dopamine.backend.member.request.MemberRequestDto;
import dopamine.backend.member.response.MemberResponseDto;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final LevelService levelService;
    private final LevelRepository levelRepository;

    /**
     * CREATE : 생성
     *
     * @param memberRequestDto
     */
    public Member createMember(MemberRequestDto memberRequestDto) {
        // 닉네임 중복 검사
        checkNicknameDuplication(null, memberRequestDto.getNickname());

        // exp에 해당하는 레벨 생성
        Level level = getMemberLevel(memberRequestDto.getExp());

        // member 생성
        Member member = Member.builder()
                .kakaoId(memberRequestDto.getKakaoId())             // kakaoId
                .nickname(memberRequestDto.getNickname())           // nickname
                .refreshToken(memberRequestDto.getRefreshToken())   // refreshToken
                .level(level)                                       // level
                .exp(memberRequestDto.getExp()).build();            // exp

        memberRepository.save(member);

        return member;
    }

    /**
     * DELETE : 삭제
     *
     * @param member
     */
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    /**
     * GET : 조회
     *
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
     *
     * @param member
     * @param memberEditDto
     * @return
     */
    public Member editMember(Member member, MemberEditDto memberEditDto) {

        // 닉네임 중복 검사
        checkNicknameDuplication(member, memberEditDto.getNickname());

        // exp에 해당하는 레벨 생성
        Level level = getMemberLevel(memberEditDto.getExp());

        // member 수정
        member.changeMember(
                memberEditDto.getKakaoId(),         // kakaoId
                memberEditDto.getNickname(),        // nickname
                memberEditDto.getRefreshToken(),    // refreshToken
                memberEditDto.getExp(),             // exp
                level                               // level
        );

        return member;
    }

    /**
     * 검증 -> memberId 입력하면 관련 Member Entity가 있는지 확인
     *
     * @param memberId
     * @return member
     */

    public Member verifiedMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    //== jwt 인증 부분==//

    /**
     * kakaoId를 가진 Member가 없으면 새로운 Member생성, 아니면 기존 Member반환
     *
     * @param kakaoId
     * @return
     */
    public Member findMemberByKakaoId(String kakaoId) {
        return memberRepository.findMemberByKakaoId(kakaoId).orElseGet(() -> createMember(MemberRequestDto.builder()
                .kakaoId(kakaoId)
                .build()));
    }

    /**
     * 닉네임 중복 검사<p>
     * 1. 기존 사용자 정보이면, 중복 검사 진행 X<p>
     * 2. nickname 값이 입력되어 있으면, 중복 검사 진행
     *
     * @param member
     * @param nickname
     */
    public void checkNicknameDuplication(Member member, String nickname) {
        if (member != null && member.getNickname() != null) {
            if (member.getNickname().equals(nickname)) {
                return;
            }
        }

        if (nickname != null) {
            memberRepository.findMemberByNickname(nickname).ifPresent(a -> {
                throw new BusinessLogicException(ExceptionCode.NICKNAME_DUPLICATE);
            });
        }
    }

    /**
     * exp에 해당하는 Level 반환
     *
     * @param exp
     * @return Level
     */
    public Level getMemberLevel(int exp) {
        return levelRepository.findTopByExpLessThanEqualOrderByExpDesc(exp);
    }
}