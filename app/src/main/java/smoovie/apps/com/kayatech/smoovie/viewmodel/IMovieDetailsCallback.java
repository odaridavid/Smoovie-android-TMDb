package smoovie.apps.com.kayatech.smoovie.viewmodel;


import smoovie.apps.com.kayatech.smoovie.model.Movie;

public interface IMovieDetailsCallback {
        //On Success movie details received
        void onSuccess(Movie movie);

        void onError();

}
