package dopamine.backend.domain.challenge.repository;

import dopamine.backend.domain.challenge.entity.Challenge;

import java.util.List;

public interface ChallengeCustomRepository {

    List<Challenge> getTodayChallenges(List<Challenge> existChallenges);
}
