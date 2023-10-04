package dopamine.backend.domain.member.service;

import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.global.exception.BusinessLogicException;
import dopamine.backend.global.exception.ExceptionCode;
import dopamine.backend.domain.level.repository.LevelRepository;
import dopamine.backend.domain.member.request.MemberEditDto;
import dopamine.backend.domain.member.request.MemberRequestDto;
import dopamine.backend.domain.member.response.MemberResponseDto;
import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.domain.member.mapper.MemberMapper;
import dopamine.backend.domain.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final LevelRepository levelRepository;

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
     * CREATE : 생성
     *
     * @param memberRequestDto
     */
    public Member createMember(MemberRequestDto memberRequestDto) {

        Member member = Member.builder().kakaoId(memberRequestDto.getKakaoId()).build();    // member 생성
        setMemberExpAndLevel(member);                                                // 경험치 설정

        return memberRepository.save(member);
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

        // member 수정
        member.changeMember(memberEditDto.getKakaoId(), memberEditDto.getNickname(), memberEditDto.getRefreshToken());
        setMemberExpAndLevel(member);

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
     * 닉네임 중복 검사
     *
     * @param member
     * @param nickname
     */
    @Transactional(readOnly = true)
    public void checkNicknameDuplication(Member member, String nickname) {
        // 기존 사용자 정보이면 중복 검사 진행x
        if (member != null && member.getNickname() != null) {
            if (member.getNickname().equals(nickname)) {
                return;
            }
        }
        // nickname 값이 입력되어 있으면, 중복 검사 진행
        if (nickname != null) {
            memberRepository.findMemberByNickname(nickname).ifPresent(a -> {
                throw new BusinessLogicException(ExceptionCode.NICKNAME_DUPLICATE);
            });
        }
    }

    /**
     * member의 Feed에 따라 exp와 Level변경
     * @param member
     */
    public void setMemberExpAndLevel(Member member) {
        int exp = 0;

        // 사용자의 Feed를 기준으로 exp 계산
        for (Feed f : member.getFeeds()) {
            if (!f.getFulfillYn()) continue;
            exp += f.getChallenge().getChallengeLevel().getExp();
        }

        // exp와 level 변경
        member.setExpAndLevel(exp, levelRepository.findTopByExpLessThanEqualOrderByExpDesc(exp));
    }


    /**
     * 검증 -> memberId 입력하면 관련 Member Entity가 있는지 확인
     *
     * @param memberId
     * @return member
     */
    @Transactional(readOnly = true)
    public Member verifiedMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}