package dopamine.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class MemberRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long id;

        private Long kakaoId;

        private String nickname;

        private int level;

        private String badge;

        private int challengeCnt;
        private String refreshToken;

    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long id;

        private Long kakaoId;

        private String nickname;

        private int level;

        private String badge;

        private int challengeCnt;
        private String refreshToken;

    }
}
