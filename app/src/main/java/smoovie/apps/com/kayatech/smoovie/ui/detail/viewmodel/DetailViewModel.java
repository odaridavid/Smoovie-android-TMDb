package smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel;

import android.arch.lifecycle.ViewModel;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;
import smoovie.apps.com.kayatech.smoovie.model.Movie;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class DetailViewModel extends ViewModel {
    private MoviesRepository mMoviesRepository;

    DetailViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public Movie getMovieDetails(int movieId, String language) {
        return mMoviesRepository
                .getMovieDetails(movieId, language);
    }

}
