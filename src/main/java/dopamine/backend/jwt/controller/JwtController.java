package dopamine.backend.jwt.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dopamine.backend.exception.BusinessLogicException;
import dopamine.backend.exception.ExceptionCode;
import dopamine.backend.jwt.dto.KakaoUserInfo;
import dopamine.backend.jwt.response.JwtResponse;
import dopamine.backend.jwt.response.TokenResponse;
import dopamine.backend.jwt.service.JwtService;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.response.MemberResponseDto;
import dopamine.backend.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@AllArgsConstructor
@Slf4j
public class JwtController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final MemberMapper memberMapper;

    @GetMapping("/login")
    public ResponseEntity kakaoCallback(@RequestParam(value = "code", required = false) String code,
                                        @RequestParam(value = "redirect_url", required = false) String redirect_url) {

        if(StringUtils.isEmpty(code)) {
            throw new BusinessLogicException(ExceptionCode.MISSING_REQUEST_PARAM);
        }

        if(StringUtils.isEmpty(redirect_url)) {
            throw new BusinessLogicException(ExceptionCode.MISSING_REQUEST_PARAM);
        }

        log.info("redirect_url : " + redirect_url);

        // 유저 정보 얻기
        String kakaoAccessToken = jwtService.getKakaoAccessToken(code, redirect_url);
        KakaoUserInfo kakaoUserInfo = jwtService.getKakaoUserInfo(kakaoAccessToken);

        // 해당 kakao ID를 가진 Member 반환
        log.info(kakaoUserInfo.getKakaoId());
        Member member = memberService.findMemberByKakaoId(kakaoUserInfo.getKakaoId());

        // accessToken과 refreshToken발급
        String accessToken = jwtService.getAccessToken(member); // 에러 발생
        String refreshToken = member.getRefreshToken();

        // 응답
        MemberResponseDto memberResponse = memberMapper.memberToMemberResponseDto(member);
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(tokenResponse)
                .member(memberResponse).build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }
}
