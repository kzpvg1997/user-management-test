package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi;


import co.com.globalogic.usermanagement.domain.user.Phone;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.UserAuthentication;
import co.com.globalogic.usermanagement.service.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
public class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImp userServiceImp;

    private User user;
    private UserAuthentication auth;

    @BeforeEach
    public void setup() {
        var phones = List.of(Phone.builder().build());
        auth = UserAuthentication.builder()
                .id(123L)
                .token("any-token")
                .lastLogin(LocalDateTime.now())
                .build();

        user = User.builder()
                .name("User")
                .password("Agc4mf9sdl")
                .email("usuario@dominio.com")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .phones(phones)
                .authentication(auth)
                .build();
    }

    @Test
    void shouldReturnOkWhenLogin() throws Exception {

        Mockito.when(userServiceImp.login(auth.getToken())).thenReturn(user);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\""+auth.getToken()+"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isActive").value(user.getIsActive()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldReturnCreatedWhenSignUp() throws Exception {

        Mockito.when(userServiceImp.singUpUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(post("/users/sing-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" +user.getName()+"\" , " +
                                "\"email\": \"" +user.getEmail()+"\" , " +
                                "\"password\": \"" +user.getPassword()+"\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
