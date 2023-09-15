package dopamine.backend.member.controller;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challengemember.entity.ChallengeMember;
import dopamine.backend.challengemember.service.ChallengeMemberService;
import dopamine.backend.feed.repository.FeedRepository;
import dopamine.backend.jwt.dto.KakaoUserInfo;
import dopamine.backend.jwt.response.JwtResponse;
import dopamine.backend.jwt.service.JwtService;
import dopamine.backend.level.entity.Level;
import dopamine.backend.level.mapper.LevelMapper;
import dopamine.backend.level.repository.LevelRepository;
import dopamine.backend.level.response.LevelDetailResponseDto;
import dopamine.backend.level.response.LevelResponseDto;
import dopamine.backend.level.service.LevelService;
import dopamine.backend.member.request.MemberEditDto;
import dopamine.backend.member.request.MemberRequestDto;
import dopamine.backend.member.response.MemberDetailResponseDto;
import dopamine.backend.member.response.MemberResponseDto;
import dopamine.backend.member.service.MemberService;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.mapper.MemberMapper;
import dopamine.backend.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

import static dopamine.backend.level.service.LevelService.INF;


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
