package springboot.Junit.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import springboot.Junit.model.Movie;
import springboot.Junit.repository.MovieRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseURL = "http://localhost";

    private static RestTemplate restTemplate;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }


    @BeforeEach
    public void beforeSetup() {
        baseURL = baseURL + ":" + port + "/movies";

         avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romantic");
        titanicMovie.setReleaseDate(LocalDate.of(2003, Month.MARCH, 12));

       avatarMovie = movieRepository.save(avatarMovie);
       titanicMovie = movieRepository.save(titanicMovie);
    }

    @AfterEach
    public void afterSetup() {
        movieRepository.deleteAll();
    }

    ////////////////////////////////////////////////////////////
    @Test
    void shouldCreateMovieTest() {
     /*   Movie avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));*/

        Movie newMovie = restTemplate.postForObject(baseURL, avatarMovie, Movie.class);

        assertNotNull(newMovie);
        assertThat(newMovie.getId()).isNotNull();
    }

    ////////////////////////////////////////////////////////////
    @Test
    public void shouldFetchMovieTest() {
      /*  Movie avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        Movie titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romantic");
        titanicMovie.setReleaseDate(LocalDate.of(2003, Month.MARCH, 12));

        restTemplate.postForObject(baseURL, avatarMovie, Movie.class);
        restTemplate.postForObject(baseURL, titanicMovie, Movie.class);*/

        List<Movie> movieList = restTemplate.getForObject(baseURL, List.class);

        assertThat(movieList.size()).isEqualTo(2);
    }

    //////////////////////////////////////////////////////////////////////
    @Test
    public void shouldFetchOneMovieTest() {

      /*  Movie avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        Movie titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romantic");
        titanicMovie.setReleaseDate(LocalDate.of(2003, Month.MARCH, 12));

        avatarMovie = restTemplate.postForObject(baseURL, avatarMovie, Movie.class);
        titanicMovie = restTemplate.postForObject(baseURL, titanicMovie, Movie.class);*/

        Movie existingMovie = restTemplate.getForObject(baseURL + "/" + avatarMovie.getId(), Movie.class);

        assertNotNull(existingMovie);
        assertEquals("Avatar", existingMovie.getName());
    }

    /////////////////////////////////////////////////////////////////////////
    @Test
    public void shouldDeleteMovieTest() {
       /* Movie avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        Movie titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romantic");
        titanicMovie.setReleaseDate(LocalDate.of(2003, Month.MARCH, 12));

        avatarMovie = restTemplate.postForObject(baseURL, avatarMovie, Movie.class);
        titanicMovie = restTemplate.postForObject(baseURL, titanicMovie, Movie.class);*/

        restTemplate.delete(baseURL + "/" + avatarMovie.getId());

        int count = movieRepository.findAll().size();

        assertEquals(1, count);
    }

    /////////////////////////////////////////////////////////////////////////
    @Test
    public void shouldUpdateMovieTest() {
       /* Movie avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        Movie titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romantic");
        titanicMovie.setReleaseDate(LocalDate.of(2003, Month.MARCH, 12));

        avatarMovie = restTemplate.postForObject(baseURL, avatarMovie, Movie.class);
        titanicMovie = restTemplate.postForObject(baseURL, titanicMovie, Movie.class);*/

        avatarMovie.setGenera("Fantasy");

        restTemplate.put(baseURL +"/{id}",avatarMovie,avatarMovie.getId());

        Movie existingMovie = restTemplate.getForObject(baseURL+"/"+ avatarMovie.getId(), Movie.class);

        assertNotNull(existingMovie);
        assertEquals("Fantasy", existingMovie.getGenera());
    }
}