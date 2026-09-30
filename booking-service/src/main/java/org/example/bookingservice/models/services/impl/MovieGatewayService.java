package org.example.bookingservice.models.services.impl;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.example.bookingservice.clients.MovieClient;
import org.example.bookingservice.exceptions.MovieNotFoundException;
import org.example.bookingservice.exceptions.MovieServiceException;
import org.example.bookingservice.models.dto.responses.MovieResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieGatewayService {

    private final MovieClient movieClient;

    @CircuitBreaker(name = "moviesService" , fallbackMethod = "heyMovieByFallback")


    public MovieResponse getMovieById(Long movieId) {
        try {
            MovieResponse movie = movieClient.getMovieById(movieId);
            if (movie == null) {
                throw new MovieNotFoundException(movieId);
            }
            return movie;
        } catch (MovieNotFoundException e) {
            throw new MovieNotFoundException(movieId);
        } catch (FeignException e) {
            throw new UnsupportedOperationException("Not supported yet." + e);
        }

    }

    private  void getMovieByIdFallback(Long movieId) {

        throw new MovieServiceException("Movie not found");
    }


}
