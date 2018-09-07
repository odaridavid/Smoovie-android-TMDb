package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviews;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideoResponse;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideos;


public class MovieDetailViewModel extends AndroidViewModel {

    private LiveData<Movie> movie;
    private LiveData<List<MovieVideos>> movieVideosLiveData;
    private LiveData<List<MovieReviews>> movieReviewsLiveData;
    public final MoviesRepository moviesRepository;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        this.moviesRepository = new MoviesRepository();

    }

    public void init(int mMovieId,int page, final IMovieDetailsCallback IMovieDetailsCallback,final IMovieVideosCallback iMovieVideosCallback,final IMovieReviewsCallback iMovieReviewsCallback) {
        movie = moviesRepository.getMovie(mMovieId, IMovieDetailsCallback);
        movieVideosLiveData = moviesRepository.getMovieVideos(mMovieId,iMovieVideosCallback);
        movieReviewsLiveData = moviesRepository.getMovieReviews(page,mMovieId,iMovieReviewsCallback);

    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public LiveData<List<MovieVideos>> getMovieVideosLiveData() {
        return movieVideosLiveData;
    }

    public LiveData<List<MovieReviews>> getMovieReviewsLiveData() {
        return movieReviewsLiveData;
    }
}
