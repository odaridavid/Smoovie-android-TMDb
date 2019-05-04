package smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.responses.MovieReviewResponse;
import smoovie.apps.com.kayatech.smoovie.model.responses.MovieTrailerResponse;

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

    public MovieTrailerResponse getMovieTrailersResponse(int movieId, String language) {
        return mMoviesRepository
                .getMovieTrailers(movieId, language);
    }

    public MovieReviewResponse getMovieReviewsResponse(int movieId, String language) {
        return mMoviesRepository
                .getMovieReviews(movieId, language);
    }

    public void favouriteMovie(Movie movie) {
        mMoviesRepository.favouriteMovieOps(movie);
    }

    public LiveData<Movie> getFav(int movieId) {
        return mMoviesRepository.getSpecificFavouriteMovie(movieId);
    }
}
