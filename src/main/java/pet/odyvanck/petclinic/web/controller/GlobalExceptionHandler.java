package pet.odyvanck.petclinic.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;
import pet.odyvanck.petclinic.web.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                EntityAlreadyExistsException.ERROR,
                ex.getMessage()
        ) {
        };
        return ResponseEntity.badRequest().body(error);
    }

}
