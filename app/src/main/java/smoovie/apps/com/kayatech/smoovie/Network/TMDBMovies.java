package smoovie.apps.com.kayatech.smoovie.Network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBMovies {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static TMDBMovies tmdbMoviesRepo;

    private ApiGetServices TMDBService;

    //Singleton-one instance of class at a time
    //No multiple instances of TMDB.

    private TMDBMovies(ApiGetServices TMDBService) {
        this.TMDBService = TMDBService;
    }

    public static TMDBMovies getInstance() {
        if (tmdbMoviesRepo == null) {

            //OKHTTP CLIENT
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

    public void getMovies(final OnMoviesCallback moviesCallback) {
        //Put Your Api Key Here
        TMDBService.getPopularMoviesLoaded("Put API Key Here", LANGUAGE, 1).enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response.isSuccessful()) {
                    MovieListResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMoviesResult() != null) {
                        //Gets Movies List
                        moviesCallback.onSuccess(moviesResponse.getMoviesResult());
                    } else {
                        moviesCallback.onFailure();
                    }
                } else {
                    moviesCallback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {

            }
        });
    }
}
