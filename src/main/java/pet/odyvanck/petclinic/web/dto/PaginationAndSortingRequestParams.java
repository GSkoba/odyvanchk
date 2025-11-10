package pet.odyvanck.petclinic.web.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Map;

@Setter
public class PaginationAndSortingRequestParams {

    @PositiveOrZero
    private Integer page;

    @Positive
    private Integer size;

    @Getter
    private String[] sortBy;

    private Sort.Direction direction;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;


    public int getPage() {
        return page != null ? page : DEFAULT_PAGE;
    }

    public int getSize() {
        return size != null ? size : DEFAULT_SIZE;
    }

    public Sort.Direction getDirection() {
        return direction != null ? direction : DEFAULT_SORT_DIRECTION;
    }

    public PageRequest buildPageRequest(Map<String, String> transformations) {
        if (getSortBy() != null && getSortBy().length > 0) {
            Sort sort = Sort.by(getDirection(), Arrays.stream(getSortBy())
                    .map(sortField ->
                            transformations.getOrDefault(sortField, sortField))
                    .toArray(String[]::new)
            );
            return PageRequest.of(getPage(), getSize(), sort);
        }
        return PageRequest.of(getPage(), getSize());
    }

}
