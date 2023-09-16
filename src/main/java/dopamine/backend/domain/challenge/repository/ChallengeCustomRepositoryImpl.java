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

    @Override
    public List<Challenge> getTodayChallenges(List<Challenge> existChallenges) {

        List<Predicate> predicates = new ArrayList<>();

        if(existChallenges != null){
            for(Challenge c : existChallenges){
                predicates.add(challenge.challengeId.ne(c.getChallengeId()));
            }
        }

        Predicate duplicatePredicate = ExpressionUtils.allOf(predicates.toArray(new Predicate[0]));

        Challenge challengeHigh = jpaQueryFactory.selectFrom(challenge)
                .where(challenge.challengeLevel.eq(ChallengeLevel.HIGH),
                        duplicatePredicate)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();

        Challenge challengeMid = jpaQueryFactory.selectFrom(challenge)
                .where(challenge.challengeLevel.eq(ChallengeLevel.MID),
                        duplicatePredicate)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();

        Challenge challengeLow = jpaQueryFactory.selectFrom(challenge)
                .where(challenge.challengeLevel.eq(ChallengeLevel.LOW),
                        duplicatePredicate)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();

        return List.of(challengeHigh, challengeMid, challengeLow);
    }
}
