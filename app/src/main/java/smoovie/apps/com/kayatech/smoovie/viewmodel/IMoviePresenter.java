package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.arch.lifecycle.LiveData;

import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviews;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideos;


public interface IMoviePresenter {

    //Method Overload for getMovies Details
    LiveData<Movie> getMovie(int movieId, final IMovieDetailsCallback iMovieDetailsCallback);

    LiveData<MovieVideos> getMovieVideos(int movieId, final IMovieTrailersCallback iMovieTrailersCallback);

    void getMovieReviews(int page,int movieId, final IMovieReviewsCallback iMovieReviewsCallback);


    //Takes in page parameter to enable loading of multiple pages as user scrolls
    // void getMovies(int page, String sortBy, final OnMoviesCallback moviesCallback);
}
