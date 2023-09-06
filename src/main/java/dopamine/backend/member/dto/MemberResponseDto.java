package dopamine.backend.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberResponseDto {
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long id;
        private Long kakaoId;
        private String nickname;
        private int level;
        private String badge;
        private int challengeCnt;
        private String refreshToken;
    }
}
