package smoovie.apps.com.kayatech.smoovie.Network;

import android.util.Log;

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
    private static final String TAG = TMDBMovies.class.getSimpleName();
    private static TMDBMovies tmdbMoviesRepo;
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    private static final String API_KEY = "6141807ba2f6a6767a84014c1f732c1d";
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
   //Takes in page parameter to enable loading of multiple pages as user scrolls
    public void getMovies(int page,String sortBy,final OnMoviesCallback moviesCallback) {
        //Put Your Api Key Here
      Callback<MovieListResponse> call  = new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response.isSuccessful()) {
                    MovieListResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMoviesResult() != null) {
                        //Gets Movies List
                        moviesCallback.onSuccess(moviesResponse.getPage(),moviesResponse.getMoviesResult());
                    } else {
                        moviesCallback.onFailure();
                    }
                } else {
                    moviesCallback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Log.d(TAG,"Movies Could Not Be Loaded:No Result");
            }
        };

        TMDBService.getUpcomingMovies(API_KEY, LANGUAGE, page).enqueue(call);


        switch (sortBy) {
            case TOP_RATED:
                TMDBService.getTopRatedMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;

            case POPULAR:
            default:
                TMDBService.getPopularMoviesLoaded(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
    }
}}
