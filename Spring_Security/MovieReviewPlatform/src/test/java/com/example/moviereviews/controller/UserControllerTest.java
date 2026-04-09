package com.example.moviereviews.controller;

import com.example.moviereviews.dto.UserDto;
import com.example.moviereviews.enums.Role;
import com.example.moviereviews.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles = "USER")
    void return_info_of_user() throws Exception {
        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "xxx",
                "000",
                "XoX",
                OffsetDateTime.of(LocalDate.now(), LocalTime.now(),ZoneOffset.ofHours(3)),
                Role.ROLE_USER
        );
        Mockito.when(userService.getByName(any(String.class)))
                .thenReturn(dto);

        mvc.perform(get("/api/users/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("xxx"))
                .andExpect(jsonPath("$.displayName").value("XoX"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));


    }
}
