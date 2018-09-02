package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import smoovie.apps.com.kayatech.smoovie.model.Movie;


public class MovieDetailViewModel extends AndroidViewModel {

    private LiveData<Movie> movie;
    private final MoviesRepository moviesRepository;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
         this.moviesRepository = new MoviesRepository();

    }


    public void init(int mMovieId, final MovieDetailsCallback movieDetailsCallback) {


        movie = moviesRepository.getMovie(mMovieId,movieDetailsCallback);


    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
