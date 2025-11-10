package pet.odyvanck.petclinic.web.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> elements,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static <T, V> PageResponse<T> from(Page<V> page, Function<List<V>, List<T>> function) {
        return new PageResponse<>(
                function.apply(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}

