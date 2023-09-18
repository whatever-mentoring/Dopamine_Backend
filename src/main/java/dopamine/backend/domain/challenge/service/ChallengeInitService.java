package dopamine.backend.domain.challenge.service;

import dopamine.backend.domain.challenge.entity.ChallengeLevel;
import dopamine.backend.domain.challenge.request.ChallengeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ChallengeInitService implements CommandLineRunner {

    private final ChallengeService challengeService;

    @Override
    public void run(String... args) throws Exception {

        ChallengeRequestDTO challengeRequestDTO1 = ChallengeRequestDTO.builder().title("에어컨 온도 / 난방기 온도 조절")
                .challengeGuide("test").challengeLevel(ChallengeLevel.HIGH).subtitle("test").build();
        ChallengeRequestDTO challengeRequestDTO2 = ChallengeRequestDTO.builder().title("이 닦을 때 수도꼭지를 잠그기")
                .challengeGuide("test").challengeLevel(ChallengeLevel.HIGH).subtitle("test").build();
        ChallengeRequestDTO challengeRequestDTO3 = ChallengeRequestDTO.builder().title("대중교통 이용하기")
                .challengeGuide("test").challengeLevel(ChallengeLevel.MID).subtitle("test").build();
        ChallengeRequestDTO challengeRequestDTO4 = ChallengeRequestDTO.builder().title("세탁망 활용하기")
                .challengeGuide("test").challengeLevel(ChallengeLevel.MID).subtitle("test").build();
        ChallengeRequestDTO challengeRequestDTO5 = ChallengeRequestDTO.builder().title("세탁할 때 저온수를 선택하고, 탈수 횟수는 적게로 선택하기")
                .challengeGuide("test").challengeLevel(ChallengeLevel.LOW).subtitle("test").build();
        ChallengeRequestDTO challengeRequestDTO6 = ChallengeRequestDTO.builder().title("요리할 때 냄비나 팬의 뚜껑을 덮기")
                .challengeGuide("test").challengeLevel(ChallengeLevel.LOW).subtitle("test").build();

        List<ChallengeRequestDTO> challengeRequestDTOs = List.of(challengeRequestDTO1, challengeRequestDTO2, challengeRequestDTO3, challengeRequestDTO4, challengeRequestDTO5, challengeRequestDTO6);

        for(ChallengeRequestDTO dto : challengeRequestDTOs){
            challengeService.createChallenge(dto);
        }
    }
}