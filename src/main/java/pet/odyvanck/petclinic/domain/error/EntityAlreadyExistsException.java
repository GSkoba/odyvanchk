package pet.odyvanck.petclinic.domain.error;


public class EntityAlreadyExistsException extends RuntimeException {

    private final static String ERROR_MESSAGE = "%s with %s '%s' already exists";

    public EntityAlreadyExistsException(String entityName, String fieldName, String value) {
        super(String.format(ERROR_MESSAGE, entityName, fieldName, value));
    }
}
