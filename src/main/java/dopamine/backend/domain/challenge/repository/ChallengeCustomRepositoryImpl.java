package dopamine.backend.domain.challenge.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dopamine.backend.domain.challenge.entity.Challenge;
import dopamine.backend.domain.challenge.entity.ChallengeLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static dopamine.backend.domain.challenge.entity.QChallenge.challenge;


@Repository
@RequiredArgsConstructor
public class ChallengeCustomRepositoryImpl implements ChallengeCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 오늘의 챌린지
     * @param existChallenges
     * @return
     */
    @Override
    public List<Challenge> getTodayChallenges(List<Challenge> existChallenges) {

        List<Predicate> predicates = new ArrayList<>();

        // 기존 챌린지와 일치하지 않도록 조건 추가
        if(existChallenges != null){
            for(Challenge c : existChallenges){
                predicates.add(challenge.challengeId.ne(c.getChallengeId()));
            }
        }

        Predicate duplicatePredicate = ExpressionUtils.allOf(predicates.toArray(new Predicate[0]));

        // 기존 챌린지와 일치하지 않고, 난이도 상인 챌린지에서 랜덤으로 하나 가져오기
        Challenge challengeHigh = jpaQueryFactory.selectFrom(challenge)
                .where(challenge.challengeLevel.eq(ChallengeLevel.HIGH),
                        duplicatePredicate)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();

        // 기존 챌린지와 일치하지 않고, 난이도 중인 챌린지에서 랜덤으로 하나 가져오기
        Challenge challengeMid = jpaQueryFactory.selectFrom(challenge)
                .where(challenge.challengeLevel.eq(ChallengeLevel.MID),
                        duplicatePredicate)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();

        // 기존 챌린지와 일치하지 않고, 난이도 하인 챌린지에서 랜덤으로 하나 가져오기
        Challenge challengeLow = jpaQueryFactory.selectFrom(challenge)
                .where(challenge.challengeLevel.eq(ChallengeLevel.LOW),
                        duplicatePredicate)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();

        return List.of(challengeHigh, challengeMid, challengeLow);
    }
}
