package dopamine.backend.member.controller;

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

    // CREATE : 생성
    @PostMapping
    public MemberResponseDto createMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        // todo : memberNum 중복되지 않도록 만들어야 함
        return memberService.createMember(memberRequestDto);
    }

    // DELETE : 삭제
    @DeleteMapping("/{member-id}")
    public void deleteMember(@Positive @PathVariable("member-id") Long memberId) {
        memberService.deleteMember(memberId);
    }

    // GET : 조회
    @GetMapping("/{member-id}")
    public MemberResponseDto getMember(@Positive @PathVariable("member-id") Long memberId) {
        return memberService.getMember(memberId);
    }

    // UPDATE : 수정
    @PutMapping("/{member-id}")
    public MemberResponseDto editMember(@Positive @PathVariable("member-id") Long memberId,
                                      @Valid @RequestBody MemberEditDto memberEditDto) {
        return memberService.editMember(memberId, memberEditDto);
    }
}
