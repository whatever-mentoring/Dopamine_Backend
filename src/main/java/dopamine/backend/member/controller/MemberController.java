package dopamine.backend.member.controller;

import dopamine.backend.jwt.dto.KakaoUserInfo;
import dopamine.backend.jwt.response.JwtResponse;
import dopamine.backend.jwt.service.JwtService;
import dopamine.backend.member.request.MemberEditDto;
import dopamine.backend.member.request.MemberRequestDto;
import dopamine.backend.member.response.MemberResponseDto;
import dopamine.backend.member.service.MemberService;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final JwtService jwtService;

    // CREATE : 생성
    @PostMapping
    public ResponseEntity createMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        // todo : memberNum 중복되지 않도록 만들어야 함
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
    @GetMapping("/{member-id}")
    public MemberResponseDto getMember(@Positive @PathVariable("member-id") Long memberId) {
        return memberService.getMember(memberId);
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
