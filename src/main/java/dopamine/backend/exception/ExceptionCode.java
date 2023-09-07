
package dopamine.backend.exception;
import lombok.Getter;

import java.util.Arrays;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    AUTHORIZATION_HEADER_NOT_FOUND(400, "Authorization Header Bad Request"),
    TOKEN_NOT_VALID(400, "Token Not Valid");


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
