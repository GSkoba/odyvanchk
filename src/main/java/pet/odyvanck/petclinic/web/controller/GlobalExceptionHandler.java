package pet.odyvanck.petclinic.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.web.dto.ErrorResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles entity already exists exception.
     * @param ex
     * @return
     */
    @ExceptionHandler({EntityAlreadyExistsException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestErrors(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        ) {
        };
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handles validation errors.
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        ErrorResponse errorResponse = new ErrorResponse(
          HttpStatus.BAD_REQUEST,
                "Validation errors in request data",
                errors
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles not found entity exceptions
     * @param ex
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles internal server errors.
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        //add logs
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
