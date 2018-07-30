package smoovie.apps.com.kayatech.smoovie.Network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiGetServices {
    //Retrofit http requests
    //Query parameter appended to the URL.
    //Call - An invocation of a Retrofit method that sends a request to a webserver and returns a response

    //Most Popular
    @GET("movie/popular")
    Call<MovieListResponse> getPopularMoviesLoaded(
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

    @GET("movie/latest")
    Call<MovieListResponse> getLatestMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}
