package pet.odyvanck.petclinic.web.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private Object details;
    private LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message, Object details) {
        this(status, message);
        this.details = details;
    }

}
