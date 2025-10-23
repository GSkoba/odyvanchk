package pet.odyvanck.petclinic.web.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);

    public ErrorResponse(HttpStatus status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }


}
