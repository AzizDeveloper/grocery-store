package dev.aziz.grocerystore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.grocerystore.config.UserAuthProvider;
import dev.aziz.grocerystore.dtos.CredentialsDto;
import dev.aziz.grocerystore.dtos.SignUpDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public UserService userService;

    @Autowired
    public UserAuthProvider userAuthProvider;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    void login() throws Exception {
        //given
        UserDto userDto = UserDto.builder().id(1L).login("azizdev").firstName("Aziz").lastName("Abdukarimov").build();
        String token1 = userAuthProvider.createToken(userDto.getLogin());
        userDto.setToken(token1);

        char[] password = {'a', 's', 'd'};
        CredentialsDto credentialsDto = CredentialsDto.builder().login("azizdev").password(password).build();
        String credentialsRequest = objectMapper.writeValueAsString(credentialsDto);

        //when
        when(userService.login(credentialsDto)).thenReturn(userDto);

        //then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsRequest)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
        ;
    }

    @Test
    void register() throws Exception {
        //given
        UserDto userDto = UserDto.builder().id(1L).login("azizdev").firstName("Aziz").lastName("Abdukarimov").build();
        String token1 = userAuthProvider.createToken(userDto.getLogin());
        userDto.setToken(token1);

        char[] password = {'1', '2', '3', '4'};
        SignUpDto signUpDto = SignUpDto.builder()
                .login("azizdev")
                .firstName("Aziz")
                .lastName("Abdukarimov")
                .password(password)
                .build();
        String signUpDtoRequest = objectMapper.writeValueAsString(signUpDto);

        //when
        when(userService.register(signUpDto)).thenReturn(userDto);

        //then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
        ;
    }

    @Test
    void logOut() throws Exception {
        //given
        UserDto userDto = UserDto.builder().id(1L).login("azizdev").firstName("Aziz").lastName("Abdukarimov").build();
        String token1 = userAuthProvider.createToken(userDto.getLogin());
        userDto.setToken(token1);

        //when then
        mockMvc.perform(post("/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Bearer Token", token1)
                )
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
    }
}
