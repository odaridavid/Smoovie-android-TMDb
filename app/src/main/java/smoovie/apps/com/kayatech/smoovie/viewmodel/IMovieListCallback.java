package smoovie.apps.com.kayatech.smoovie.viewmodel;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Movie;

public interface IMovieListCallback {

    //On Success Gets Movie List and page depending on sort by option
    void onSuccess(int page,List<Movie> movies);

    void onFailure();

}
