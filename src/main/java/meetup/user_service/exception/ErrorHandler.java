package meetup.user_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        Map<String, String> error = Map.of("error", e.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse(error, HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        log.error(e.getLocalizedMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        Map<String, String> error = Map.of("error", e.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse(error, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        log.error(e.getLocalizedMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllException(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getLocalizedMessage());
        errors.put("stackTrace", getStackTraceAsString(e));
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        log.error(e.getLocalizedMessage());
        return errorResponse;
    }

    private String getStackTraceAsString(Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

}
