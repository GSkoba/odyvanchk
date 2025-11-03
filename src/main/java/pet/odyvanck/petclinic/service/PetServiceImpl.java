package pet.odyvanck.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.dao.PetRepository;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.service.specification.OwnerSpecification;
import pet.odyvanck.petclinic.service.specification.PetSpecification;
import pet.odyvanck.petclinic.web.dto.pet.PetRequestParams;
import pet.odyvanck.petclinic.web.dto.pet.PetUpdateRequest;


@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    @Transactional
    @Override
    public Pet create(Pet pet) {
        Owner owner = ownerRepository.findById(pet.getOwner().getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner", "id", pet.getOwner().getId().toString()));

        pet.setOwner(owner);
        return petRepository.save(pet);
    }

    @Transactional
    @Override
    public Pet update(Long id, PetUpdateRequest request) {
        Pet pet = getById(id);
        pet.setName(request.name());
        pet.setColor(request.color());
        pet.setBirthDate(request.birthDate());
        pet.setWeight(request.weight());
        pet.setBreed(request.breed());
        pet.setType(request.type());
        return petRepository.save(pet);
    }

    @Transactional(readOnly = true)
    @Override
    public Pet getById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet", "id", id.toString()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Pet> getAll(PageRequest pageRequest, PetRequestParams filterFields) {
        var spec = Specification.<Pet>unrestricted().and(PetSpecification.hasOwner(filterFields.ownerId()))
                .and(PetSpecification.hasType(filterFields.type()));
        return petRepository.findAll(spec, pageRequest);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
        }
    }
}

