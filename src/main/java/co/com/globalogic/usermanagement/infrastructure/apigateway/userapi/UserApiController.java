package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi;

import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.builders.UserApiBuilders;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto.LoginRequestDTO;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto.LoginResponseDTO;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto.SingUpResponseDTO;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto.UserDTO;
import co.com.globalogic.usermanagement.service.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserApiController {

    private final UserServiceImp userServiceImp;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> getUser(@Valid @RequestBody LoginRequestDTO request) {
        var userAuthenticated = userServiceImp.login(request.getToken());
        return ResponseEntity.ok(UserApiBuilders.buildLoginResponse(userAuthenticated));
    }

    @PostMapping("/sing-up")
    public ResponseEntity<SingUpResponseDTO> createUser(@Valid @RequestBody UserDTO request) {
        var userRegistered = userServiceImp.singUpUser(request.toModel());
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserApiBuilders.buildSingUpResponse(userRegistered));
    }
}
