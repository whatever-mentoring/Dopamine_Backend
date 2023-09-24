
package dopamine.backend.global.exception;
import lombok.Getter;

import java.util.Arrays;

public enum ExceptionCode {

    // 400 Bad Request
    NICKNAME_DUPLICATE(400, "이미 있는 이름이에요."),
    TOKEN_NOT_VALID(400, "AccessToken이 유효하지 않습니다."),
    KAKAO_CODE_NOT_VALID(400, "코드가 카카오 서버에서 유효하지 않습니다."),
    KAKAO_TOKEN_NOT_VALID(400, "카카오 토큰의 만료기한이 지났습니다.(또는 유효하지 않습니다.)."),
    MISSING_REQUEST_PARAM(400, "필수 파라미터(token)를 작성해야 합니다."),
    MISSING_MONTH_REQUEST_PARAM(400, "필수 파라미터(month)를 작성해야 합니다."),
    MISSING_YEAR_REQUEST_PARAM(400, "필수 파라미터(year)를 작성해야 합니다."),
    MISSING_REDIRECT_REQUEST_PARAM(400, "필수 파라미터(redirect_url)를 작성해야 합니다."),
    CHALLENGE_ALREADY_CERTIFIED(400, "이미 완료한 챌린지입니다."),

    EXP_MIN_NOT_VALID(400, "레벨 경험치는 이전 레벨보다 높은 기준으로 설정해주세요."),
    EXP_MAX_NOT_VALID(400, "레벨 경험치는 이후 레벨보다 낮은 기준으로 설정해주세요."),
    LOGOUT_MEMBER(400, "이미 로그아웃한 유저입니다."),
    // 404 Not Found
    MEMBER_NOT_FOUND(404, "Member not found"),
    LEVEL_NOT_FOUND(404, "Level not found"),
    CHALLENGEMEMBER_NOT_FOUND(404, "유효하지 않은 챌린지입니다."),
    CHALLENGE_NOT_FOUND(404, "존재하지 않는 챌린지입니다."),
    CHALLENGE_MEMBER_NOT_FOUND(404, "참여 신청하지 않은 챌린지입니다."),
    FEED_NOT_FOUND(404, "존재하지 않는 인증글입니다."),
    DELETE_FEED_NOT_FOUND(404, "삭제된 게시글입니다."),
    FEEDLIKE_NOT_FOUND(404, "유효하지 않은 좋아요입니다."),
    FEEDLIKE_ALREADY_FOUND(404, "이미 존재하는 좋아요입니다."),

    // 402 UnAuthorized
    AUTHORIZATION_HEADER_NOT_VALID(402, "Authorization Header Bad Request"),

    // 403 UnAuthorized
    FEED_FULFILL_NOT_VALID(403, "기준이 미달된 인증글입니다. 접근할 수 없습니다."),

    // 500 Internal Server Error
    IMAGE_UPLOAD_FAILED(500, "이미지 업로드를 실패하였습니다.");

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
