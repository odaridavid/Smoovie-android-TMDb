package smoovie.apps.com.kayatech.smoovie.network;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import smoovie.apps.com.kayatech.smoovie.BuildConfig;
import smoovie.apps.com.kayatech.smoovie.model.MovieListResponse;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieListCallback;

public class TMDBMovies {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public static String LANGUAGE ;
    private final String TAG = TMDBMovies.class.getSimpleName();
    private static TMDBMovies tmdbMoviesRepo;
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    public static final String API_KEY = BuildConfig.API_KEY;
    public static ApiGetServices tmdbApiService;
    private static final Object LOCK = new Object();

    //Singleton-one instance of class
    //No multiple instances of TMDB.

    private TMDBMovies(ApiGetServices TMDBService) {
        tmdbApiService = TMDBService;
    }

    public static TMDBMovies getInstance() {
        if (tmdbMoviesRepo == null) {
               synchronized (LOCK){
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
        }

        return tmdbMoviesRepo;
    }



    public void getMovies(int page, String sortBy, final IMovieListCallback moviesCallback) {

        Callback<MovieListResponse> call = new Callback<MovieListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieListResponse> call, Response<MovieListResponse> response) {
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
            public void onFailure(@NonNull Call<MovieListResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Movies Could Not Be Loaded:No Result");
            }
        };

        switch (sortBy) {
            //called when menu option selected
            case TOP_RATED:
                tmdbApiService.getTopRatedMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;

            case POPULAR:
                tmdbApiService.getPopularMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                tmdbApiService.getUpcomingMovies(API_KEY, LANGUAGE, page).enqueue(call);
                break;

        }


    }


}
