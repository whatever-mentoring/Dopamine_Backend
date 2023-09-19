//package dopamine.backend.domain.challenge.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dopamine.backend.domain.challenge.entity.Challenge;
//import dopamine.backend.domain.challenge.entity.ChallengeLevel;
//import dopamine.backend.domain.challenge.repository.ChallengeRepository;
//import dopamine.backend.domain.challenge.response.ChallengeResponseDTO;
//import dopamine.backend.domain.level.entity.Level;
//import dopamine.backend.domain.level.repository.LevelRepository;
//import dopamine.backend.domain.level.request.LevelRequestDto;
//import dopamine.backend.domain.level.service.LevelService;
//import dopamine.backend.domain.member.entity.Member;
//import dopamine.backend.domain.member.repository.MemberRepository;
//import dopamine.backend.domain.member.request.MemberRequestDto;
//import dopamine.backend.domain.member.service.MemberService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class ChallengeServiceTest {
//
//    @Autowired
//    private ChallengeService challengeService;
//    @Autowired
//    private ChallengeRepository challengeRepository;
//    @Autowired
//    private LevelRepository levelRepository;
//    @Autowired
//    private LevelService levelService;
//    @Autowired
//    private MemberService memberService;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void clean(){
//        levelRepository.deleteAll();
//        challengeRepository.deleteAll();
//    }
//
//    @DisplayName("오늘의 챌린지 테스트 - 신규 발급일때")
//    @Test
//    void todayChallengeNew() throws JsonProcessingException {
//
//        // given
//        LevelRequestDto levelRequestDto = LevelRequestDto.builder().name("testLev").exp(5).build();
//        Level level = levelService.createLevel(levelRequestDto);
//        MemberRequestDto requestDto = MemberRequestDto.builder().nickname("test").exp(5).build();
//        Member member = memberService.createMember(requestDto);
//
//        Challenge challenge1 = Challenge.builder().title("test1").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.HIGH).build();
//        Challenge challenge2 = Challenge.builder().title("test2").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.MID).build();
//        Challenge challenge3 = Challenge.builder().title("test3").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.LOW).build();
//
//        levelRepository.save(level);
//        memberRepository.save(member);
//        challengeRepository.saveAll(List.of(challenge1, challenge2, challenge3));
//
//        // when
//        Member findMember = memberRepository.findMemberByNickname("test").orElseThrow(() -> new RuntimeException("찾을 수 없음"));
//        Long memberId = findMember.getMemberId();
//
//        List<ChallengeResponseDTO> challengeResponseDTOS = challengeService.todayChallenge(memberId);
//
//        // then
//        Assertions.assertEquals(challengeResponseDTOS.size(), 3);
//        String json = objectMapper.writeValueAsString(challengeResponseDTOS);
//        System.out.println(json);
//    }
//
//    @DisplayName("오늘의 챌린지 테스트 - 어제 발급 받았을때")
//    @Test
//    void todayChallengeYesterday() throws JsonProcessingException {
//
//        // given
//        LevelRequestDto levelRequestDto = LevelRequestDto.builder().name("testLev").exp(5).build();
//        Level level = levelService.createLevel(levelRequestDto);
//        MemberRequestDto requestDto = MemberRequestDto.builder().nickname("test").exp(5).build();
//        Member member = memberService.createMember(requestDto);
//        levelRepository.save(level);
//        memberRepository.save(member);
//
//        Challenge challenge1 = Challenge.builder().title("test1").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.HIGH).build();
//        Challenge challenge2 = Challenge.builder().title("test2").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.MID).build();
//        Challenge challenge3 = Challenge.builder().title("test3").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.LOW).build();
//
//        challengeRepository.saveAll(List.of(challenge1, challenge2, challenge3));
//
//        Member findMember = memberRepository.findMemberByNickname("test").orElseThrow(() -> new RuntimeException("찾을 수 없음"));
//        Long memberId = findMember.getMemberId();
//
//        challengeService.todayChallenge(memberId);
//        member.setChallengeRefreshDate(LocalDateTime.now().minusDays(1));
//
//        Challenge challenge4 = Challenge.builder().title("test4").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.HIGH).build();
//        Challenge challenge5 = Challenge.builder().title("test5").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.MID).build();
//        Challenge challenge6 = Challenge.builder().title("test6").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.LOW).build();
//        challengeRepository.saveAll(List.of(challenge4, challenge5, challenge6));
//
//        // when
//        List<ChallengeResponseDTO> challengeResponseDTOS = challengeService.todayChallenge(memberId);
//
//        // then
//        Assertions.assertEquals(challengeResponseDTOS.size(), 3);
//        assertThat(challengeResponseDTOS.stream().map(ChallengeResponseDTO::getTitle)).contains("test4").contains("test5").contains("test6");
//        String json = objectMapper.writeValueAsString(challengeResponseDTOS);
//        System.out.println(json);
//    }
//
//    @DisplayName("오늘의 챌린지 테스트 - 이미 오늘 발급 받았을때")
//    @Test
//    void todayChallengeAlready() throws JsonProcessingException {
//
//        // given
//        LevelRequestDto levelRequestDto = LevelRequestDto.builder().name("testLev").exp(5).build();
//        Level level = levelService.createLevel(levelRequestDto);
//        MemberRequestDto requestDto = MemberRequestDto.builder().nickname("test").exp(5).build();
//        Member member = memberService.createMember(requestDto);
//
//        Challenge challenge1 = Challenge.builder().title("test1").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.HIGH).build();
//        Challenge challenge2 = Challenge.builder().title("test2").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.MID).build();
//        Challenge challenge3 = Challenge.builder().title("test3").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.LOW).build();
//
//        levelRepository.save(level);
//        memberRepository.save(member);
//        challengeRepository.saveAll(List.of(challenge1, challenge2, challenge3));
//
//        Member findMember = memberRepository.findMemberByNickname("test").orElseThrow(() -> new RuntimeException("찾을 수 없음"));
//        Long memberId = findMember.getMemberId();
//
//        challengeService.todayChallenge(memberId);
//
//        Challenge challenge4 = Challenge.builder().title("test4").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.HIGH).build();
//        Challenge challenge5 = Challenge.builder().title("test5").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.MID).build();
//        Challenge challenge6 = Challenge.builder().title("test6").challengeGuide(".").subtitle("1").image("i1").challengeLevel(ChallengeLevel.LOW).build();
//        challengeRepository.saveAll(List.of(challenge4, challenge5, challenge6));
//
//        // when
//        List<ChallengeResponseDTO> challengeResponseDTOS = challengeService.todayChallenge(memberId);
//
//        // then
//        Assertions.assertEquals(challengeResponseDTOS.size(), 3);
//        assertThat(challengeResponseDTOS.stream().map(ChallengeResponseDTO::getTitle)).contains("test1").contains("test2").contains("test3");
//        String json = objectMapper.writeValueAsString(challengeResponseDTOS);
//        System.out.println(json);
//    }
//}