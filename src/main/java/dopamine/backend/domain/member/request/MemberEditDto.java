package dopamine.backend.domain.member.request;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEditDto {
    private String kakaoId;

    @Size(min=2, max=10, message = "2~10자 이내로 입력해주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_ ]*$", message = "특수문자는 사용할 수 없어요.")
    private String nickname;

    private String refreshToken;

    private int exp;
}
