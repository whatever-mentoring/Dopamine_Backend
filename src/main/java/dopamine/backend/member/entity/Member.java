package dopamine.backend.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    private Long kakaoId;

    private String nickname;

    private int level;

    private String badge;

    private int challengeCnt;

    private String refreshToken;

}