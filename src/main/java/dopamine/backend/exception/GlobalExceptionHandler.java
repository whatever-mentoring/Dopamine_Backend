package dopamine.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex, HttpServletRequest request){
        log.error("BusinessLogicException", ex);

        int status = ex.getExceptionCode().getStatus();
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = ex.getClass().getName();
        String message = ex.getExceptionCode().getMessage();
        String path = request.getServletPath();

        ErrorResponse response = new ErrorResponse(status, error, exception, message, path);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getExceptionCode().getStatus()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception ex){
//        log.error("handleException",ex);
//        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}