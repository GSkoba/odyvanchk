package pet.odyvanck.petclinic.domain.error;

public class EntityNotFoundException extends RuntimeException {

    private final static String ERROR_MESSAGE = "%s with %s '%s' not found";

    public EntityNotFoundException(String entity, String field, String val) {
        super(String.format(ERROR_MESSAGE, entity, field, val));
    }

}

