package com.example.moviereviews.service;

import com.example.moviereviews.domain.Movie;
import com.example.moviereviews.dto.MovieDto;
import com.example.moviereviews.dto.requests.CreateMovieRequest;
import com.example.moviereviews.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    private MovieRepository movieRepository;
    private MovieService service;

    @BeforeEach
    void setUp() {
        movieRepository = Mockito.mock(MovieRepository.class);
        service = new MovieService(movieRepository);
    }

    @Test
    void create_valid_saves_and_returns_dto() {
        CreateMovieRequest req = new CreateMovieRequest();
        req.setTitle("Inception");
        req.setYear(2010);
        req.setGenres(List.of("Action","Sci-Fi"));
        req.setDescription("Mind-bending");

        when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> {
            Movie m = inv.getArgument(0);
            m.setId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
            return m;
        });

        MovieDto dto = service.create(req);

        ArgumentCaptor<Movie> captor = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository).save(captor.capture());
        Movie saved = captor.getValue();

        assertThat(saved.getTitle()).isEqualTo("Inception");
        assertThat(saved.getYear()).isEqualTo(2010);
        assertThat(saved.getGenres()).containsExactly("Action","Sci-Fi");

        assertThat(dto.getId()).isEqualTo(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        assertThat(dto.getTitle()).isEqualTo("Inception");
    }

    @Test
    void create_invalidYear_throws() {
        CreateMovieRequest req = new CreateMovieRequest();
        req.setTitle("Old");
        req.setYear(1500);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("year is invalid");
        verifyNoInteractions(movieRepository);
    }

    @Test
    void list_maps_to_dto() {
        Movie m = new Movie();
        m.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
        m.setTitle("The Matrix");
        m.setYear(1999);
        m.setGenres(new String[] {"Action","Sci-Fi"});
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(m)));

        Page<MovieDto> page = service.list(Pageable.ofSize(10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getTitle()).isEqualTo("The Matrix");
    }
}
