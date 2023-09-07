package dopamine.backend.member.service;

import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRespository;

    // CREATE
    public Member createMember(Member member) {
        return memberRespository.save(member);
    }

    // READ
    public Member findMember(long memberPk) {
        Member member = verifiedMember(memberPk);
        return member;
    }

    public Member findMemberByKakaoId(long kakaoId) {
        Optional<Member> member = memberRespository.findMemberByKakaoId(kakaoId);
        if (member.isPresent()) {
            return member.get();
        } else {
            Member newMember = new Member();
            newMember.setKakaoId(kakaoId);
            newMember.setLevel(1);
            newMember.setBadge("새싹지킴이");
            newMember.setChallengeCnt(0);
            return createMember(newMember);
        }
    }

    // UPDATE
    public Member patchMember(Member member) {
        Member findMember = verifiedMember(member.getId());
        Optional.ofNullable(member.getKakaoId()).ifPresent(findMember::setKakaoId);
        Optional.ofNullable(member.getNickname()).ifPresent(findMember::setNickname);
        Optional.ofNullable(member.getBadge()).ifPresent(findMember::setBadge);
        Optional.ofNullable(member.getRefreshToken()).ifPresent(findMember::setRefreshToken);

        return memberRespository.save(findMember);
    }

    // DELETE
    public void deleteMember(long memberPk) {
        Member member = verifiedMember(memberPk);
        memberRespository.delete(member);
    }

    // 검증
    public Member verifiedMember(long memberPk) {
        Optional<Member> member = memberRespository.findById(memberPk);
        return member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
