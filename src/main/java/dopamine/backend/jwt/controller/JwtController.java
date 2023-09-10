package dopamine.backend.jwt.controller;

import com.google.gson.JsonObject;
import dopamine.backend.jwt.dto.KakaoUserInfo;
import dopamine.backend.jwt.response.JwtResponse;
import dopamine.backend.jwt.service.JwtService;
import dopamine.backend.jwt.service.OauthService;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.response.MemberResponseDto;
import dopamine.backend.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@AllArgsConstructor
@Slf4j
public class JwtController {

    private final OauthService oauthService;
    private final MemberService memberService;
    private final JwtService jwtService;
    private final MemberMapper memberMapper;

    @GetMapping("/login")
    public ResponseEntity kakaoCallback(@RequestParam String code) {

        // 유저 정보 얻기
        String kakaoAccessToken = oauthService.getKakaoAccessToken(code);
        KakaoUserInfo kakaoUserInfo = oauthService.getKakaoUserInfo(kakaoAccessToken);

        // 해당 kakao ID를 가진 Member 반환
        Member member = memberService.findMemberByKakaoId(kakaoUserInfo.getKakaoId());

        // accessToken과 refreshToken발급
        String accessToken = jwtService.getAccessToken(member);
        String refreshToken = member.getRefreshToken();

        // 응답
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @GetMapping("/member")
    public ResponseEntity returnMemberDetail(@RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        MemberResponseDto response = memberMapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
