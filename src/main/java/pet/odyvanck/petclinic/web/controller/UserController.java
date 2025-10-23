package pet.odyvanck.petclinic.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.odyvanck.petclinic.service.UserService;
import pet.odyvanck.petclinic.web.dto.UserRegistrationDto;
import pet.odyvanck.petclinic.web.dto.UserResponseDto;
import pet.odyvanck.petclinic.web.mapper.UserMapper;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegistrationDto request) {
        var user = userMapper.toEntity(request);
        var response = userMapper.toDto(userService.register(user, request.password()));

        URI location = URI.create("/api/v1/users/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

}
