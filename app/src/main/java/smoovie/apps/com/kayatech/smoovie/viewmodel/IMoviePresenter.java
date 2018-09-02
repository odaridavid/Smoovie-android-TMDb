package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.arch.lifecycle.LiveData;

import smoovie.apps.com.kayatech.smoovie.model.Movie;


public interface IMoviePresenter {

    //Method Overload for getMovies Details
      LiveData<Movie> getMovie(int movieId,final MovieDetailsCallback movieDetailsCallback);

    //Takes in page parameter to enable loading of multiple pages as user scrolls
   // void getMovies(int page, String sortBy, final OnMoviesCallback moviesCallback);
}
