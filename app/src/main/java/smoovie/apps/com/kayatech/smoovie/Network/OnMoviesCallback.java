package smoovie.apps.com.kayatech.smoovie.Network;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;

public interface OnMoviesCallback {
    //On Success Gets Movie List
    void onSuccess(List<Movie> movies);

    void onFailure();
}
