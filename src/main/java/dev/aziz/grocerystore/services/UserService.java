package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.config.UserAuthProvider;
import dev.aziz.grocerystore.dtos.CredentialsDto;
import dev.aziz.grocerystore.dtos.SignUpDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.exceptions.AppException;
import dev.aziz.grocerystore.mappers.UserMapper;
import dev.aziz.grocerystore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        String password = new String(user.getPassword());

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), password)) {
            return userMapper.toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.findByLogin(signUpDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())).toCharArray());

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(user);
    }


    public void logOut(String name) {
        User user = userRepository.findByLogin(name)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        SecurityContextHolder.clearContext();
    }
}
