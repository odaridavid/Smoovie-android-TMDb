package smoovie.apps.com.kayatech.smoovie.Network;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Model.MovieDetailsCallback;

public class TMDBMovies {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private final String TAG = TMDBMovies.class.getSimpleName();
    private static TMDBMovies tmdbMoviesRepo;
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    private static final String API_KEY = "";
    private ApiGetServices TMDBService;

    //Singleton-one instance of class
    //No multiple instances of TMDB.

    private TMDBMovies(ApiGetServices TMDBService) {
        this.TMDBService = TMDBService;
    }

    public static TMDBMovies getInstance() {
        if (tmdbMoviesRepo == null) {

            //OKHTTP CLIENT-handles http requests
            final OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            //REST ADAPTER
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            tmdbMoviesRepo = new TMDBMovies(retrofit.create(ApiGetServices.class));
        }

        return tmdbMoviesRepo;
    }

    //Takes in page parameter to enable loading of multiple pages as user scrolls
    public void getMovies(int page, String sortBy, final OnMoviesCallback moviesCallback) {

        Callback<MovieListResponse> call = new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response.isSuccessful()) {
                    MovieListResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMoviesResult() != null) {
                        //Gets Movies List
                        moviesCallback.onSuccess(moviesResponse.getPage(), moviesResponse.getMoviesResult());
                    } else {
                        moviesCallback.onFailure();
                    }
                } else {
                    moviesCallback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Log.d(TAG, "Movies Could Not Be Loaded:No Result");
            }
        };

        switch (sortBy) {
            //called when menu option selected
            case TOP_RATED:
                TMDBService.getTopRatedMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;

            case POPULAR:
                TMDBService.getPopularMoviesLoaded(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                TMDBService.getUpcomingMovies(API_KEY, LANGUAGE, page).enqueue(call);
                break;

        }


    }

    //Method Overload for getMovie Details
    public void getMovie(int movieId, final MovieDetailsCallback movieDetailsCallback) {
        TMDBService
                .getMovie(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                             @Override
                             public void onResponse(Call<Movie> call, Response<Movie> response) {
                                 if (response.isSuccessful()) {
                                     Movie movie = response.body();
                                     if (movie != null) {
                                         movieDetailsCallback.onSuccess(movie);
                                     } else {
                                         movieDetailsCallback.onError();
                                     }
                                 } else {
                                     movieDetailsCallback.onError();
                                 }
                             }

                             @Override
                             public void onFailure(Call<Movie> call, Throwable t) {


                                 movieDetailsCallback.onError();
                                 Log.d(TAG, "Movie Details Error");


                             }
                         }
                );

    }

}
