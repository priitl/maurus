package com.priitlaht.ppwebtv.frontend.movie;

import com.codahale.metrics.annotation.Timed;
import com.priitlaht.ppwebtv.backend.service.MovieService;
import com.priitlaht.ppwebtv.frontend.common.util.PaginationUtil;
import com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/movies/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieResource {
  @Inject
  private MovieService movieService;

  @Timed
  @RequestMapping(value = "popular", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MediaBasicDTO>> findPopularMovies(Pageable pageable) throws URISyntaxException {
    log.debug("REST request to get a page of popular movies");
    Page<MediaBasicDTO> page = movieService.findPopularMovies(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movies/popular");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @Timed
  @RequestMapping(value = "{imdbId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MovieDetailsDTO> getMovieDetails(@PathVariable String imdbId) throws URISyntaxException {
    log.debug("REST request get movie details: {}", imdbId);
    return movieService.getMovieDetails(imdbId)
      .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}
