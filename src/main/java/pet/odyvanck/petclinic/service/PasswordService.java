package pet.odyvanck.petclinic.service;

public interface PasswordService {

    String hashPassword(String plainPassword);

    boolean verifyPassword(String raw, String hashed);
}
