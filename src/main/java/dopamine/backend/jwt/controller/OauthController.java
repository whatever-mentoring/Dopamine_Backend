package dopamine.backend.jwt.controller;

import dopamine.backend.jwt.dto.KakaoUserInfo;
import dopamine.backend.jwt.service.JwtService;
import dopamine.backend.jwt.service.OauthService;
import dopamine.backend.member.dto.MemberResponseDto;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
@AllArgsConstructor
public class OauthController {

    private final OauthService oauthService;
    private final MemberService memberService;
    private final JwtService jwtService;
    private final MemberMapper memberMapper;

    @GetMapping("/oauth/kakao")
    public ResponseEntity kakaoCallback(@RequestParam String code) {

        // 유저 정보 얻기
        String kakaoAccessToken = oauthService.getKakaoAccessToken(code);
        KakaoUserInfo kakaoUserInfo = oauthService.getKakaoUserInfo(kakaoAccessToken);

        // 해당 kakao ID를 가진 Member 반환
        Member member = memberService.findMemberByKakaoId(kakaoUserInfo.getId());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", jwtService.getAccessToken(member));
        return new ResponseEntity<>(jwtService.getAccessToken(member), responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping("/getMember")
    public ResponseEntity returnMemberDetail(@RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        MemberResponseDto.Response response = memberMapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
