package dopamine.backend.domain.challenge.entity;

import lombok.Getter;

@Getter
public enum ChallengeLevel {
    HIGH(20), MID(10), LOW(5);

    private int exp;

    private ChallengeLevel(Integer exp) {
        this.exp = exp;
    }
}
