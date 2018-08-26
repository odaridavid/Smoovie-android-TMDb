package smoovie.apps.com.kayatech.smoovie.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Presenter.MovieDetailsCallback;


public class MovieDetailViewModel extends AndroidViewModel {

    private LiveData<Movie> movie;
    private MoviesRepository moviesRepository;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
         this.moviesRepository = new MoviesRepository();

    }


    public void init(int mMovieId, final MovieDetailsCallback movieDetailsCallback) {


        LiveData<Movie> movie = moviesRepository.getMovie(mMovieId,movieDetailsCallback);
        this.movie = movie;

    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
