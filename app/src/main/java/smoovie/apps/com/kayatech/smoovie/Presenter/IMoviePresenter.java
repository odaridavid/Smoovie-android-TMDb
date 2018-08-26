package smoovie.apps.com.kayatech.smoovie.Presenter;

import android.arch.lifecycle.LiveData;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Network.OnMoviesCallback;

public interface IMoviePresenter {

    //Method Overload for getMovies Details
      LiveData<Movie> getMovie(int movieId,final MovieDetailsCallback movieDetailsCallback);

    //Takes in page parameter to enable loading of multiple pages as user scrolls
   // void getMovies(int page, String sortBy, final OnMoviesCallback moviesCallback);
}
