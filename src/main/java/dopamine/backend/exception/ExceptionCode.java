
package dopamine.backend.exception;
import lombok.Getter;

import java.util.Arrays;

public enum ExceptionCode {

    // 400 Bad Request
    NICKNAME_DUPLICATE(400, "닉네임이 중복되었습니다."),
    TOKEN_NOT_VALID(400, "Token Not Valid"),
    KAKAO_CODE_NOT_VALID(400, "코드가 카카오 서버에서 유효하지 않습니다."),
    MISSING_REQUEST_PARAM(400, "필수 파라미터를 작성해야 합니다."),

    // 404 Not Found
    MEMBER_NOT_FOUND(404, "Member not found"),
    LEVEL_NOT_FOUND(404, "Level not found"),
    CHALLENGEMEMBER_NOT_FOUND(404, "ChallengeMember not found"),

    // 402 UnAuthorized
    AUTHORIZATION_HEADER_NOT_VALID(402, "Authorization Header Bad Request");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }

    public static ExceptionCode findByMessage(String message) {
        return Arrays.stream(ExceptionCode.values())
                .filter(exceptionCode -> exceptionCode.getMessage().equals(message))
                .findFirst()
                .orElse(null);
    }
}
