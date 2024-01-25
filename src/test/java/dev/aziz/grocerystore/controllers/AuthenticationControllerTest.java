package dev.aziz.grocerystore.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.grocerystore.config.UserAuthProvider;
import dev.aziz.grocerystore.dtos.CredentialsDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.services.BasketItemService;
import dev.aziz.grocerystore.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public UserService userService;

    @MockBean
    public UserAuthProvider userAuthProvider;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    void login() throws Exception {
        //given

        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("azizdev")
                .firstName("Aziz")
                .lastName("Abdukarimov")
                .token("There should be token?")
                .build();
        userDto.setToken("my-token");

        char[] password = {'a', 's', 'd'};
        CredentialsDto credentialsDto = CredentialsDto.builder()
                .login("azizdev")
                .password(password)
                .build();
        String credentialsRequest = objectMapper.writeValueAsString(credentialsDto);

        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);
        String token = JWT.create()
                .withIssuer(credentialsDto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256("my-token"));
        userDto.setToken(token);

        //when
        when(userService.login(credentialsDto)).thenReturn(userDto);
        when(userAuthProvider.createToken(credentialsDto.getLogin())).thenReturn(token);

        //then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsRequest)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void register() {
        //given
        //when
        //then
    }

    @Test
    void logOut() {
        //given
        //when
        //then
    }
}
