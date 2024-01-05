package dev.aziz.grocerystore.controllers;

import dev.aziz.grocerystore.config.UserAuthProvider;
import dev.aziz.grocerystore.dtos.CredentialsDto;
import dev.aziz.grocerystore.dtos.SignUpDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user.getLogin()));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user.getLogin()));
        return ResponseEntity.created(URI.create("/users" + user.getId()))
                .body(user);
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> logOut(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.split(" ")[1];
        Authentication authentication = userAuthProvider.validateToken(token);
        UserDto userDto = (UserDto) authentication.getPrincipal();
        String login = userDto.getLogin();
        userService.logOut(login);
        return ResponseEntity.noContent().build();
    }

}
