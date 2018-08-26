package smoovie.apps.com.kayatech.smoovie.Presenter;


import smoovie.apps.com.kayatech.smoovie.Model.Movie;

public interface MovieDetailsCallback {
        //On Success movie details received
        void onSuccess(Movie movie);

        void onError();

}
