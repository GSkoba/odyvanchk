package pet.odyvanck.petclinic.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerRequestParams;
import pet.odyvanck.petclinic.web.dto.owner.OwnerUpdateRequest;

import java.util.UUID;

public interface OwnerService {

    /**
     * Registers owner in the system.
     *
     * @param owner    owner information.
     * @param user     user related information.
     * @param password raw password.
     * @return created owner.
     */
    Owner register(@NotNull Owner owner, @NotNull User user, @NotNull String password);

    /**
     * Gets information about owners according to request.
     *
     * @param pageRequest page request for getting owner.
     * @param filter      filter params for owner.
     * @return owner list according to params.
     */
    Page<Owner> getAll(@NotNull PageRequest pageRequest, @NotNull OwnerRequestParams filter);

    /**
     * Updates owner fields.
     *
     * @param id      unique owner id.
     * @param request fields to update.
     * @return updated entity.
     */
    Owner update(UUID id, @Valid @NotNull OwnerUpdateRequest request);

    /**
     * Gets owner by id
     *
     * @param id unique owner id.
     * @return owner entity.
     */
    Owner getById(@NotNull UUID id);

    /**
     * Deletes owner by id.
     *
     * @param id unique owner id.
     */
    void deleteById(@NotNull UUID id);

}
