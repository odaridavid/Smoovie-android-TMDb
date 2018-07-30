package smoovie.apps.com.kayatech.smoovie.Network;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;

public interface OnMoviesCallback {
    //TODO 1.WHAT IS A CALLBACK
    //On Success Gets Movie List
    void onSuccess(int page,List<Movie> movies);

    void onFailure();
}
