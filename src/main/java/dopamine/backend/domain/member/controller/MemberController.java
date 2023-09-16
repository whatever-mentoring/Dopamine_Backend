package dopamine.backend.domain.member.controller;

import dopamine.backend.domain.level.service.LevelService;
import dopamine.backend.domain.member.request.MemberEditDto;
import dopamine.backend.domain.member.request.MemberRequestDto;
import dopamine.backend.domain.member.response.MemberResponseDto;
import dopamine.backend.domain.member.service.MemberService;
import dopamine.backend.global.jwt.service.JwtService;
import dopamine.backend.domain.level.response.LevelDetailResponseDto;
import dopamine.backend.domain.member.response.MemberDetailResponseDto;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.domain.member.mapper.MemberMapper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/members")
@Api(tags = "멤버 API")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final JwtService jwtService;
    private final LevelService levelService;



    // CREATE : 생성
    @PostMapping
    public ResponseEntity createMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        Member member = memberService.createMember(memberRequestDto);
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }

    // DELETE : 삭제
    @DeleteMapping
    public void deleteMember(@RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        memberService.deleteMember(member);
    }

    // GET : 조회
    @GetMapping
    public ResponseEntity getMember(@RequestHeader("Authorization") String accessToken) {

        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기

        LevelDetailResponseDto levelDetailResponseDto = levelService.memberDetailLevel(member);

        MemberDetailResponseDto response = MemberDetailResponseDto.builder()
                .memberId(member.getMemberId())
                .kakaoId(member.getKakaoId())
                .nickname(member.getNickname())
                .exp(member.getExp())
                .level(levelDetailResponseDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE : 수정
    @PutMapping
    public ResponseEntity editMember(@RequestHeader("Authorization") String accessToken,
                                      @Valid @RequestBody MemberEditDto memberEditDto) {

        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        memberService.editMember(member, memberEditDto); // 수정
        MemberResponseDto response = memberMapper.memberToMemberResponseDto(member); // response

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity returnMemberDetail(@RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        MemberResponseDto response = memberMapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
