package smoovie.apps.com.kayatech.smoovie;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import smoovie.apps.com.kayatech.smoovie.db.dao.IFavouriteMovieDao;
import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.responses.MovieListResponse;
import smoovie.apps.com.kayatech.smoovie.model.responses.MovieReviewResponse;
import smoovie.apps.com.kayatech.smoovie.model.responses.MovieTrailerResponse;
import smoovie.apps.com.kayatech.smoovie.network.MovieApiServices;
import smoovie.apps.com.kayatech.smoovie.util.threads.AppExecutors;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class MoviesRepository {

//    TODO 1.(MoviesRepository) - Use MutableLiveData with Network Calls and set Value

    private static MoviesRepository sMoviesRepository;
    private MovieApiServices mMovieApiServices;
    private IFavouriteMovieDao mMovieDao;
    private AppExecutors mThreadAppExecutors;
    private static final Object LOCK = new Object();
    private MovieListResponse mMovieListResponse;
    private Movie mMovie;
    private final String TAG = MoviesRepository.class.getSimpleName();
    private MovieReviewResponse mMovieReviewResponse;
    private MovieTrailerResponse mMMovieTrailerResponse;


    private MoviesRepository(MovieApiServices movieApiServices, IFavouriteMovieDao movieDao, AppExecutors threadAppExecutors) {
        mMovieApiServices = movieApiServices;
        mMovieDao = movieDao;
        mThreadAppExecutors = threadAppExecutors;
    }

    public synchronized static MoviesRepository getInstance(IFavouriteMovieDao movieDao, MovieApiServices movieApiServices, AppExecutors threadAppExecutors) {
        if (sMoviesRepository == null) {
            synchronized (LOCK) {
                sMoviesRepository = new MoviesRepository(movieApiServices, movieDao, threadAppExecutors);
            }
        }
        return sMoviesRepository;
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return mMovieDao.loadAll();
    }

    public LiveData<List<Movie>> getSpecificFavouriteMovieById(int id) {
        return mMovieDao.loadById(id);
    }

    public LiveData<List<Movie>> getSpecificFavouriteMovieByTitle(String title) {
        return mMovieDao.loadByTitle(title);
    }

    public void addToFavouriteMovies(final Movie movie) {
        mThreadAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieDao.addToFavourites(movie);
            }
        });
        Log.d(TAG, "Movie Saved");
    }

    public void removeFromFavouriteMovies(final Movie movie) {
        mThreadAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieDao.removeFromFavourites(movie);
            }
        });
        Log.d(TAG, "Movie Deleted");
    }

    public MovieListResponse getMovies(Category category, final String language, final int page) {
        switch (category) {
            case POPULAR:
                try {
                    mMovieListResponse = mMovieApiServices
                            .getPopularMovies(BuildConfig.API_KEY, language, page)
                            .execute()
                            .body();
                    Log.d(TAG, "Popular Movies Loaded");
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            case UPCOMING:
                try {
                    mMovieListResponse = mMovieApiServices
                            .getUpcomingMovies(BuildConfig.API_KEY, language, page)
                            .execute()
                            .body();
                    Log.d(TAG, "Top Rated Movies Loaded");
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            case TOP_RATED:
                try {
                    mMovieListResponse = mMovieApiServices
                            .getTopRatedMovies(BuildConfig.API_KEY, language, page)
                            .execute()
                            .body();
                    Log.d(TAG, "Upcoming Movies Loaded");
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
        }
        return mMovieListResponse != null ? mMovieListResponse : new MovieListResponse();
    }

    public Movie getMovieDetails(final int movieId, final String language) {
        try {
            mMovie = mMovieApiServices
                    .getMovie(movieId, BuildConfig.API_KEY, language)
                    .execute()
                    .body();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mMovie != null ? mMovie : getDefaultMovie();
    }

    private Movie getDefaultMovie() {
        mMovie = new Movie();
        return mMovie;
    }

    public MovieReviewResponse getMovieReviews(final int movieId, final String language) {
        mThreadAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mMovieReviewResponse = mMovieApiServices
                            .getMovieReviews(movieId, BuildConfig.API_KEY, language)
                            .execute()
                            .body();
                    Log.e(TAG, "Reviews Loaded");
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        return mMovieReviewResponse != null ? mMovieReviewResponse : new MovieReviewResponse();
    }

    public MovieTrailerResponse getMovieTrailers(final int movieId, final String language) {
        mThreadAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mMMovieTrailerResponse = mMovieApiServices
                            .getMovieTrailers(movieId, BuildConfig.API_KEY, language)
                            .execute()
                            .body();
                    Log.e(TAG, "Trailers Loaded");
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        return mMMovieTrailerResponse != null ? mMMovieTrailerResponse : new MovieTrailerResponse();
    }

}
