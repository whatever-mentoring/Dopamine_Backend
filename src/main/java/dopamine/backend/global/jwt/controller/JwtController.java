package dopamine.backend.global.jwt.controller;

import dopamine.backend.domain.exception.BusinessLogicException;
import dopamine.backend.domain.exception.ExceptionCode;
import dopamine.backend.global.jwt.dto.KakaoUserInfo;
import dopamine.backend.global.jwt.response.JwtResponse;
import dopamine.backend.global.jwt.service.JwtService;
import dopamine.backend.global.jwt.response.TokenResponse;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.domain.member.mapper.MemberMapper;
import dopamine.backend.domain.member.response.MemberResponseDto;
import dopamine.backend.domain.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "로그인 API")
@Validated
@AllArgsConstructor
@Slf4j
public class JwtController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final MemberMapper memberMapper;

    @GetMapping("/login")
    public ResponseEntity kakaoCallback(@RequestParam(value = "token", required = false) String token,
                                        @RequestParam(value = "code", required = false) String code) {

        // 유저 정보 얻기
        if(!StringUtils.isEmpty(code)) {
            token = jwtService.getKakaoAccessToken(code);
        } else if(StringUtils.isEmpty(token)) {
            throw new BusinessLogicException(ExceptionCode.MISSING_REQUEST_PARAM);
        }
        KakaoUserInfo kakaoUserInfo = jwtService.getKakaoUserInfo(token);

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