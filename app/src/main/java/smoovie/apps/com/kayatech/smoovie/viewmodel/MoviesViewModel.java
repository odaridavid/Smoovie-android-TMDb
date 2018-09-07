package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Movie;

public class MoviesViewModel extends ViewModel {

    private LiveData<List<Movie>> movieList;

    public LiveData<List<Movie>> getMovieList() {
        return movieList;
    }


}
