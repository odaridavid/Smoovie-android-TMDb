package smoovie.apps.com.kayatech.smoovie.Network;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;

public interface OnMoviesCallback {

    //On Success Gets Movie List and page depending on sort by option
    void onSuccess(int page,List<Movie> movies);

    void onFailure();

}
