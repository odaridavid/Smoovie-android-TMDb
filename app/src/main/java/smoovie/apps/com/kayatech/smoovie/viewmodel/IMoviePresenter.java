package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.arch.lifecycle.LiveData;

import smoovie.apps.com.kayatech.smoovie.model.Movie;


public interface IMoviePresenter {

    //Method for getMovies Details
    LiveData<Movie> getMovie(int movieId, final IMovieDetailsCallback iMovieDetailsCallback);

    /**
     *
     * @param movieId Clicked Movie Id
     * @param iMovieVideosCallback Trailer Callback for handling on success and error
     */
    void getMovieVideos(int movieId, final IMovieVideosCallback iMovieVideosCallback);

    /**
     *
     * @param page Current Page
     * @param movieId Clicked Movie Id
     * @param iMovieReviewsCallback Callback for handling on success and on error for Reviews
     */
    void getMovieReviews(int page,int movieId, final IMovieReviewsCallback iMovieReviewsCallback);


    //Takes in page parameter to enable loading of multiple pages as user scrolls
    // void getMovies(int page, String sortBy, final OnMoviesCallback moviesCallback);
}
