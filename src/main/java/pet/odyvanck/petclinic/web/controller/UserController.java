package pet.odyvanck.petclinic.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.odyvanck.petclinic.service.UserService;
import pet.odyvanck.petclinic.web.mapper.UserMapper;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

}
