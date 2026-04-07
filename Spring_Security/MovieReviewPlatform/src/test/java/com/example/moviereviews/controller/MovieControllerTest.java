package com.example.moviereviews.controller;

import com.example.moviereviews.dto.MovieDto;
import com.example.moviereviews.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieController.class)
class MovieControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MovieService movieService;

    @Test
    void list_returns_page_of_movies() throws Exception {
        MovieDto dto = new MovieDto(
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                "The Matrix",
                1999,
                List.of("Action","Sci-Fi"),
                "desc"
        );
        Mockito.when(movieService.list(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mvc.perform(get("/api/movies").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("The Matrix"));
    }
}
