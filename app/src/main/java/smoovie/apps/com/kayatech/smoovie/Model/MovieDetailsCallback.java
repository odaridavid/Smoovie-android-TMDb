package smoovie.apps.com.kayatech.smoovie.Model;

public interface MovieDetailsCallback {
        //On Success movie details received
        void onSuccess(Movie movie);

        void onError();

}
