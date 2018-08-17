package smoovie.apps.com.kayatech.smoovie.Presenter;

import smoovie.apps.com.kayatech.smoovie.Model.MovieDetailsCallback;
import smoovie.apps.com.kayatech.smoovie.Network.OnMoviesCallback;

public interface IMoviePresenter {

    //Method Overload for getMovies Details
    void getMovies(int movieId, MovieDetailsCallback movieDetailsCallback);

    //Takes in page parameter to enable loading of multiple pages as user scrolls
    void getMovies(int page, String sortBy, final OnMoviesCallback moviesCallback);
}
