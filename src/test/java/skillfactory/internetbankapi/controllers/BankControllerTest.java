package skillfactory.internetbankapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import skillfactory.internetbankapi.entities.User;
import skillfactory.internetbankapi.services.UserService;

import java.util.Optional;

import static org.hamcrest.Matchers.*;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;


    @MockBean
    UserService userService;

    User USER_1 = new User(1L, "Ivan", "Ivanov", 15000.00);
    User USER_2 = new User(2L, "Petr", "Petrov", 10000.00);
    User USER_3 = new User(3L, "Yuriy", "Sidorov", 25000.00);

    @Test
    void getBalance_success() throws Exception {
        Mockito.when(userService.findUserById(USER_1.getId())).thenReturn(Optional.of(USER_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/balance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is(15000.00)));
    }

    @Test
    void takeMoney() throws Exception {
        USER_2.setBalance(USER_2.getBalance() - 500.00);
        Mockito.when(userService.findUserById(USER_2.getId())).thenReturn(Optional.of(USER_2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/balance/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is(9500.00)));
    }

    @Test
    void putMoney() throws Exception {
        USER_3.setBalance(USER_3.getBalance() + 1000.00);
        Mockito.when(userService.findUserById(USER_3.getId())).thenReturn(Optional.of(USER_3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/balance/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is(26000.00)));

    }
}