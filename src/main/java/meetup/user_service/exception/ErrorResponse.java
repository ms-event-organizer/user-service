package meetup.user_service.exception;

import lombok.Getter;

public class ErrorResponse {
    @Getter
    private final String error;
    @Getter
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
