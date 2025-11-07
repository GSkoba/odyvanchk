package pet.odyvanck.petclinic.domain.error;

public class InvalidStateException extends RuntimeException  {

    public InvalidStateException(String message) {
        super(message);
    }
}
