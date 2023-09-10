package dopamine.backend.member.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MemberResponseDto {
    private Long memberId;
    private String kakaoId;
    private String nickname;
    private String refreshToken;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String delYn;
}
