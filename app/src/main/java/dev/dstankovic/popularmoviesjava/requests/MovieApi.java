package dev.dstankovic.popularmoviesjava.requests;

import dev.dstankovic.popularmoviesjava.models.MovieDetails;
import dev.dstankovic.popularmoviesjava.requests.responses.MovieSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    // SEARCH
    @GET("search/movie")
    Call<MovieSearchResponse> searchMovie(@Query("api_key") String api_key,
                                          @Query("query") String query,
                                          @Query("page") int page);

    // SEARCH MOVIES BY GENRE
    @GET("discover/movie")
    Call<MovieSearchResponse> searchMoviesByCategory(@Query("api_key") String api_key,
                                                        @Query("with_genres") int genreId,
                                                        @Query("page") int page);

    // GET MOVIE DETAILS
    @GET("movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") int movie_id,
                                       @Query("api_key") String api_key);

}
