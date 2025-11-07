package pet.odyvanck.petclinic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.odyvanck.petclinic.dao.IdempotencyKeyRepository;
import pet.odyvanck.petclinic.domain.IdempotencyKey;
import pet.odyvanck.petclinic.domain.error.IdempotencySerializationException;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyKeyRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public <T> T execute(String key, Supplier<T> action, Class<T> responseType) {
        if (key == null || key.isBlank()) {
            return action.get();
        }
        return repository.findByKey(key)
                .map(saved -> deserialize(saved.getResponse(), responseType))
                .orElseGet(() -> {
                    T result = action.get();
                    save(key, result);
                    return result;
                });
    }

    private <T> void save(String key, T result) {
        try {
            String json = objectMapper.writeValueAsString(result);
            IdempotencyKey entity = new IdempotencyKey();
            entity.setKey(key);
            entity.setResponse(json);
            repository.save(entity);
        } catch (Exception e) {
            throw new IdempotencySerializationException("Failed to save idempotent response for type" + result.getClass(), e);
        }
    }

    private <T> T deserialize(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new IdempotencySerializationException("Failed to deserialize saved response for type" + type.getName(), e);
        }
    }
}

