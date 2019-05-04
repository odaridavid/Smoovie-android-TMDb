package smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;
import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieNetworkLite;
import smoovie.apps.com.kayatech.smoovie.model.responses.MovieListResponse;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class MainViewModel extends ViewModel {
    private MoviesRepository mMoviesRepository;

    MainViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public List<MovieNetworkLite> getMovies(Category category, int page) {
        MovieListResponse vMovieListResponse = mMoviesRepository
                .getMovies(category, page);
        return page <= vMovieListResponse.getTotalPages() ? vMovieListResponse.getMoviesResult() : null;
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return mMoviesRepository.getFavouriteMovies();
    }
}
