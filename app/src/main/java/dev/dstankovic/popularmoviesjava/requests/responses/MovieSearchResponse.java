package dev.dstankovic.popularmoviesjava.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dev.dstankovic.popularmoviesjava.models.Movie;

public class MovieSearchResponse {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieSearchResponse() {
    }

    /**
     *
     * @param totalResults
     * @param totalPages
     * @param page
     * @param movies
     */
    public MovieSearchResponse(int page, int totalResults, int totalPages, List<Movie> movies) {
        super();
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.movies = movies;
    }

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Movie> getMovies() {
        return movies;
    }

}

