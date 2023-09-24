package dopamine.backend.domain.challengemember.controller;

import dopamine.backend.domain.challengemember.request.ChallengeMemberEditDto;
import dopamine.backend.domain.challengemember.request.ChallengeMemberRequestDto;
import dopamine.backend.domain.challengemember.response.ChallengeMemberResponseDto;
import dopamine.backend.domain.challengemember.service.ChallengeMemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Api(tags = "챌린지 멤버 API : 챌린지에 참여하는 멤버 확인")
@RequestMapping("/api/challenge-members")
public class ChallengeMemberController {

    private final ChallengeMemberService challengeMemberService;

    // CREATE : 생성
    @PostMapping
    public void createChallengeMember(@Valid @RequestBody ChallengeMemberRequestDto challengeMemberRequestDto) {
        challengeMemberService.createChallengeMember(challengeMemberRequestDto);
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
