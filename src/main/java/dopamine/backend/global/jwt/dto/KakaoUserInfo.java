package dopamine.backend.global.jwt.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoUserInfo {
    private String kakaoId;
}