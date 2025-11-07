package pet.odyvanck.petclinic.domain.error;

public class IdempotencySerializationException extends RuntimeException {

    public IdempotencySerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
