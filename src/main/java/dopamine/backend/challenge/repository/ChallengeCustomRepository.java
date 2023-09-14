package dopamine.backend.challenge.repository;

import dopamine.backend.challenge.entity.Challenge;

import java.util.List;

public interface ChallengeCustomRepository {

    List<Challenge> getTodayChallenges(List<Challenge> existChallenges);
}
