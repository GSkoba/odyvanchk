package pet.odyvanck.petclinic.domain.error;


public class EntityAlreadyExistsException extends RuntimeException {

    public final static String ERROR = "Entity already exists";
    private final static String MESSAGE = "%s with %s '%s' already exists";

    public EntityAlreadyExistsException(String entityName, String fieldName, String value) {
        super(String.format(MESSAGE, entityName, fieldName, value));
    }
}
