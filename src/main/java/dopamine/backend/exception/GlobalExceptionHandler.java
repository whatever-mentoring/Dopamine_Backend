package dopamine.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * ExceptionHandler - handleBusinessLogicException
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex, HttpServletRequest request){
        log.error("BusinessLogicException", ex);

        int status = ex.getExceptionCode().getStatus();
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = ex.getClass().getName();
        String message = ex.getExceptionCode().getMessage();
        String path = request.getServletPath();

        ErrorResponse response = new ErrorResponse(status, error, exception, message, path);
        return new ResponseEntity<>(response, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("MethodArgumentNotValidException", ex);

        int status = 400;
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = ex.getClass().getName();
        String message = ex.getAllErrors().get(0).getDefaultMessage();
        String path = request.getServletPath();

        ErrorResponse response = new ErrorResponse(status, error, exception, message, path);
        return new ResponseEntity<>(response, HttpStatus.valueOf(status));
    }

}