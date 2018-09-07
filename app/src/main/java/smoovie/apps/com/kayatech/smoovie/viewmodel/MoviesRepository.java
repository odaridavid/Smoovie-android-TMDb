package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviewResponse;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviews;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideoResponse;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideos;
import smoovie.apps.com.kayatech.smoovie.network.TMDBMovies;


public class MoviesRepository implements IMoviePresenter {
    //Fetches The Data From Api

    //Details
    @Override
    public LiveData<Movie> getMovie(int movieId, final IMovieDetailsCallback IMovieDetailsCallback) {

        final MutableLiveData<Movie> movieDetailLiveData = new MutableLiveData<>();

        TMDBMovies.tmdbApiService
                .getMovie(movieId, TMDBMovies.API_KEY, TMDBMovies.LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movieDetailResponse = response.body();
                            if (movieDetailResponse != null) {
                                IMovieDetailsCallback.onSuccess(movieDetailResponse);
                                movieDetailLiveData.setValue(movieDetailResponse);
                                Log.d("MOVIE RESPONSE", movieDetailLiveData.toString());
                            } else {
                                IMovieDetailsCallback.onError();
                                Log.d("MOVIE ERRROR RESPONSE", "NULL");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {

                    }
                });
        return movieDetailLiveData;
    }


    //Videos
    @Override
    public LiveData<List<MovieVideos>> getMovieVideos(int movieId, final IMovieVideosCallback iMovieVideosCallback) {
        final MutableLiveData<List<MovieVideos>> movieVideoLiveData = new MutableLiveData<>();
        Callback<MovieVideoResponse> call = new Callback<MovieVideoResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieVideoResponse> call, @NonNull Response<MovieVideoResponse> response) {
                if (response.isSuccessful()) {
                    MovieVideoResponse movieVideoResponse = response.body();
                    if (movieVideoResponse != null && movieVideoResponse.getMoviesResult() != null) {
                        //Gets Movies List
                        iMovieVideosCallback.onSuccess(movieVideoResponse.getMoviesResult());
                        movieVideoLiveData.setValue(movieVideoResponse.getMoviesResult());

                    } else {
                        iMovieVideosCallback.onError();
                    }
                } else {
                    iMovieVideosCallback.onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieVideoResponse> call, @NonNull Throwable t) {

            }
        };


        TMDBMovies.tmdbApiService.getMovieVideos(movieId, TMDBMovies.API_KEY, TMDBMovies.LANGUAGE)
                .enqueue(call);
        return movieVideoLiveData;
    }


    //Reviews
    @Override
    public LiveData<List<MovieReviews>> getMovieReviews(int page, int movieId, final IMovieReviewsCallback iMovieReviewsCallback) {
        final MutableLiveData<List<MovieReviews>> movieReviewLiveData = new MutableLiveData<>();
        Callback<MovieReviewResponse> call = new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviewResponse> call, @NonNull Response<MovieReviewResponse> response) {
                if (response.isSuccessful()) {
                    MovieReviewResponse moviesReviewResponse = response.body();
                    if (moviesReviewResponse != null && moviesReviewResponse.getMoviesResult() != null) {
                        //Gets Movies List
                        iMovieReviewsCallback.onSuccess(moviesReviewResponse.getPage(), moviesReviewResponse.getMoviesResult());
                        movieReviewLiveData.setValue(moviesReviewResponse.getMoviesResult());

                    } else {
                        iMovieReviewsCallback.onFailure();
                    }
                } else {
                    iMovieReviewsCallback.onFailure();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReviewResponse> call, @NonNull Throwable t) {

            }
        };

        TMDBMovies.tmdbApiService.getMovieReviews(movieId, TMDBMovies.API_KEY, TMDBMovies.LANGUAGE)
                .enqueue(call);
        return movieReviewLiveData;

    }


}
