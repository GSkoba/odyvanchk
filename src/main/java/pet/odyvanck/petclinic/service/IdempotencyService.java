package pet.odyvanck.petclinic.service;

import java.util.function.Supplier;

/**
 * Idempotency service allows executing operations safely,
 * ensuring that repeated calls with the same Idempotency-Key
 * return the same response instead of re-executing the action.
 */
public interface IdempotencyService {

    /**
     * Executes an idempotent action.
     *
     * @param key           Unique Idempotency-Key (provided by client)
     * @param action        Action to execute if key was not used before
     * @param responseType  Expected response class type for deserialization
     * @param <T>           Response object type
     * @return Previously stored response (if key exists) or newly created response
     */
    <T> T execute(String key, Supplier<T> action, Class<T> responseType);
}
