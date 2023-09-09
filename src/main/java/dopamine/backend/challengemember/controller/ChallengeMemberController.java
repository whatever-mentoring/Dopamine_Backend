package dopamine.backend.challengemember.controller;

import dopamine.backend.challengemember.request.ChallengeMemberEditDto;
import dopamine.backend.challengemember.request.ChallengeMemberRequestDto;
import dopamine.backend.challengemember.response.ChallengeMemberResponseDto;
import dopamine.backend.challengemember.service.ChallengeMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenge-members")
public class ChallengeMemberController {

    private final ChallengeMemberService challengeMemberService;

    // CREATE : 생성
    @PostMapping
    public ChallengeMemberResponseDto createChallengeMember(@Valid @RequestBody ChallengeMemberRequestDto challengeMemberRequestDto) {
        return challengeMemberService.createChallengeMember(challengeMemberRequestDto);
    }

    // DELETE : 삭제
    @DeleteMapping("/{challenge-member-id}")
    public void deleteChallengeMember(@Positive @PathVariable("challenge-member-id") Long challengeMemberId) {
        challengeMemberService.deleteChallengeMember(challengeMemberId);
    }

    // GET : 조회
    @GetMapping("/{challenge-member-id}")
    public ChallengeMemberResponseDto getChallengeMember(@Positive @PathVariable("challenge-member-id") Long challengeMemberId) {
        return challengeMemberService.getChallengeMember(challengeMemberId);
    }

    // UPDATE : 수정
    @PutMapping("/{challenge-member-id}")
    public ChallengeMemberResponseDto editChallengeMember(@Positive @PathVariable("challenge-member-id") Long challengeMemberId,
                                                @Valid @RequestBody ChallengeMemberEditDto challengeMemberEditDto) {
        return challengeMemberService.editChallengeMember(challengeMemberId, challengeMemberEditDto);
    }
}
