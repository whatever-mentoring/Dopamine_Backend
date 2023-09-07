package dopamine.backend.member.controller;

import dopamine.backend.member.dto.MemberRequestDto;
import dopamine.backend.member.dto.MemberResponseDto;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping("/api/member")
@Validated
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberRequestDto.Post post) {
        Member member = memberService.createMember(memberMapper.memberPostDtoToMember(post));
        MemberResponseDto.Response response = memberMapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{member-pk}")
    public ResponseEntity getMember(@Positive @PathVariable("member-pk") long memberPk) {
        Member member = memberService.findMember(memberPk);
        MemberResponseDto.Response response = memberMapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchMember(@Valid @RequestBody MemberRequestDto.Patch patch) {
        Member member = memberService.patchMember(memberMapper.memberPatchDtoToMember(patch));
        MemberResponseDto.Response response = memberMapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{member-pk}")
    public ResponseEntity deleteMember(@Positive @PathVariable("member-pk") long memberPk) {
        memberService.deleteMember(memberPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
