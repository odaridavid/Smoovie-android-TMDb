package smoovie.apps.com.kayatech.smoovie.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieListResponse;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviewResponse;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideoResponse;


public interface ApiGetServices {
    //Retrofit http requests
    //Query parameter appended to the URL.
    //Call - An invocation of a Retrofit method that sends a request to a webserver and returns a response
    //Response to display List on Main UI
    //Most Popular
    @GET("movie/popular")
    Call<MovieListResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page

    );

    //Highest Rated
    @GET("movie/top_rated")
    Call<MovieListResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page

    );

    //upcoming
    @GET("movie/upcoming")
    Call<MovieListResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page

    );

    //Response to get movie details to display on Details UI
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getMovieReviews(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<MovieVideoResponse> getMovieVideos(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

}
