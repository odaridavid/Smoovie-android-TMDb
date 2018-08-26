package smoovie.apps.com.kayatech.smoovie.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;
import smoovie.apps.com.kayatech.smoovie.Model.Movie;

public class MoviesViewModel extends ViewModel {

    private LiveData<List<Movie>> movieList;

    public LiveData<List<Movie>> getMovieList() {
        return movieList;
    }



}
