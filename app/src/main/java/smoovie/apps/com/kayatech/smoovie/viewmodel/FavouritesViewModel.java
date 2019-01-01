package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.db.MovieDatabase;

public class FavouritesViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> movies;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase db = MovieDatabase.getMovieDatabaseInstance(this.getApplication());
        movies = db.movieDAO().loadAllMovies();

    }

    public LiveData<List<Movie>> getMovies() {

        return movies;
    }
}
