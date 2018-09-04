package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import smoovie.apps.com.kayatech.smoovie.model.Movie;


public class MovieDetailViewModel extends AndroidViewModel {

    private LiveData<Movie> movie;
    public final MoviesRepository moviesRepository;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        this.moviesRepository = new MoviesRepository();

    }

    public void init(int mMovieId, final IMovieDetailsCallback IMovieDetailsCallback) {
        movie = moviesRepository.getMovie(mMovieId, IMovieDetailsCallback);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
