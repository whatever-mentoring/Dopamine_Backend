package dopamine.backend.jwt.service;


import dopamine.backend.jwt.provider.JwtProvider;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.request.MemberEditDto;
import dopamine.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final MemberService memberService;

    @Value("${jwt.secret}")
    private String secretKey;

    public String getAccessToken(Member member){

        // accessToken, refreshToken 발급
        String accessToken = "Bearer " + JwtProvider.createAccessToken(member.getMemberId(), secretKey);
        String refreshToken = "Bearer " + JwtProvider.createRefreshToken(member.getMemberId(), secretKey);

        // member 엔티티에 refreshToken 저장
        MemberEditDto memberEditDto = new MemberEditDto();
        memberEditDto.setRefreshToken(refreshToken);
        memberService.editMember(member.getMemberId(), memberEditDto);

        return "Bearer " + JwtProvider.createAccessToken(member.getMemberId(), secretKey);
    }

    public Member getMemberFromAccessToken(String accessToken) {
        accessToken = accessToken.split(" ")[1];
        Long userId = JwtProvider.getUserId(accessToken, secretKey);
        return memberService.verifiedMember(userId);
    }
}
