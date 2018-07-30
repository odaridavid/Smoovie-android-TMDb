package smoovie.apps.com.kayatech.smoovie.Network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiGetServices {
    //Retrofit http requests
    //Query parameter appended to the URL.
    //Call - An invocation of a Retrofit method that sends a request to a webserver and returns a response
    @GET("movie/popular")
    Call<MovieListResponse> getPopularMoviesLoaded(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    //Most Popular
    @GET("/discover/movie?sort_by=popularity.desc")
    Call<MovieListResponse> getPopularMovies();

    //Highest Rated
    @GET("/discover/movie?sort_by=vote_average.desc")
    Call<MovieListResponse> getHighRatedMovies();
}
