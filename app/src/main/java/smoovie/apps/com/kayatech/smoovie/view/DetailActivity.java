package smoovie.apps.com.kayatech.smoovie.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviews;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideos;
import smoovie.apps.com.kayatech.smoovie.db.AppExecutors;
import smoovie.apps.com.kayatech.smoovie.db.MovieDatabase;
import smoovie.apps.com.kayatech.smoovie.viewmodel.DetailViewModel;
import smoovie.apps.com.kayatech.smoovie.viewmodel.FavouritesViewModelFactory;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieDetailsCallback;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieReviewsCallback;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieVideosCallback;
import smoovie.apps.com.kayatech.smoovie.viewmodel.MovieDetailViewModel;

import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_REVIEWS;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_REVIEW_PAGE;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_VIDEOS;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.MOVIE_ID;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.MOVIE_ID_ROOM_DB;

public class DetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private ReviewsAdapter mReviewsAdapter;
    private VideoAdapter mVideoAdapter;
    private IVideoClickHandler iVideoClickHandler;
    private IShareClickHandler iShareClickHandler;
    private MovieDetailViewModel movieDetailViewModel;
    private MovieDatabase movieDatabase;
    private StyleableToast.Builder mToast;
    private static final int DEFAULT_TASK_ID = -1;
    private FavouritesViewModelFactory factory;
    private DetailViewModel detailViewModel;
    private Typeface customTypeface;
    private static Movie movieInstance = null;
    private static int movieReviewsPage;
    private static List<MovieReviews> movieReviewsList;
    private static List<MovieVideos> movieVideosList;

    @BindView(R.id.tv_rating_value)
    TextView mRatingValueTextView;
    @BindView(R.id.tv_label_release_date)
    TextView mLabelReleasedTextView;
    @BindView(R.id.tv_label_overview)
    TextView mLabelOverviewTextView;
    @BindView(R.id.tv_release_date)
    TextView mMovieReleaseDateTextView;
    @BindView(R.id.tv_overview)
    TextView mMovieOverviewTextView;
    @BindView(R.id.tv_label_movie_title)
    TextView mMovieTitleTextView;
    @BindView(R.id.rb_movie_rating)
    RatingBar mMovieRatingRatingBar;
    @BindView(R.id.iv_backdrop)
    ImageView mMovieBackdropImageView;
    @BindView(R.id.iv_poster_image)
    ImageView mMoviePosterImageView;
    @BindView(R.id.tv_label_reviews)
    TextView mMovieReviewLabelTextView;
    @BindView(R.id.tv_label_trailers)
    TextView mMovieTrailerLabelTextView;
    @BindView(R.id.tv_no_review)
    TextView mMovieReviewEmptyTextView;
    @BindView(R.id.tv_no_trailer)
    TextView mMovieTrailerEmptyTextView;
    @BindView(R.id.rv_movies_review)
    RecyclerView mMoviesReviewRecyclerView;
    @BindView(R.id.rv_movies_trailer)
    RecyclerView mMoviesTrailerRecyclerView;
    @BindView(R.id.iv_favcon)
    ImageView mFavouritesIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Bind Views
        ButterKnife.bind(this);
        movieDatabase = MovieDatabase.getMovieDatabaseInstance(getApplicationContext());
        //Custom Font For Labels
        customTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setupLabelsTypeface(customTypeface);
        //setup actionbar
        setupToolbar();
        //Check if intent contains extras
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            //on rotate
            if (savedInstanceState != null && intent.hasExtra(MOVIE_ID)) {
                //saved instance state for movies loaded over network
                Log.d(TAG, "movie saved instance");
                if (savedInstanceState.containsKey(KEY_MOVIE)) {
                    setUpRecyclerView();
                    //saved movie
                    Movie movieInstance = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE));
                    updateInterface(movieInstance);
                    List<MovieReviews> movieReviewsList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_REVIEWS));
                    int movieReviewsPage = savedInstanceState.getInt(KEY_MOVIE_REVIEW_PAGE);
                    List<MovieVideos> movieVideosList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_VIDEOS));
                    setupReviewAapter(movieReviewsPage, movieReviewsList);
                    setupVideoAdapter(movieVideosList);
                    detailViewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
                    detectDrawableChange();
                }
            } else if (intent.hasExtra(MOVIE_ID)) {
                //get movie clicked from intent
                final int mMovieId = Parcels.unwrap(getIntent().getParcelableExtra(MOVIE_ID));
                Log.d(TAG, "Movie Being Passed" + mMovieId);
                //For Room Database will be used to compare loaded movie and  that in Database
                factory = new FavouritesViewModelFactory(movieDatabase, mMovieId);
                detailViewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
                //Handles Loading movie
                movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
                movieDetailViewModel.init(mMovieId, 1,
                        //Movie Details
                        new IMovieDetailsCallback() {
                            @Override
                            public void onSuccess(Movie movie) {
                                if (movie != null) {
                                    setUpRecyclerView();
                                    updateInterface(movie);
                                    //save movie for on rotate
                                    movieInstance = movie;
                                }
                            }

                            @Override
                            public void onError() {
                                displayError();
                            }
                        },
                        //Movie Trailers
                        new IMovieVideosCallback() {
                            @Override
                            public void onSuccess(List<MovieVideos> movieVideos) {
                                setupVideoAdapter(movieVideos);
                                //save movie  video list for on rotate
                                movieVideosList = movieVideos;
                            }

                            @Override
                            public void onError() {
                            }
                        },
                        //Movie Reviewa
                        new IMovieReviewsCallback() {
                            @Override
                            public void onSuccess(int page, List<MovieReviews> movieReviews) {
                                setupReviewAapter(page, movieReviews);
                                //save movie reviews for on rotate
                                movieReviewsList = movieReviews;
                                movieReviewsPage = page;
                            }

                            @Override
                            public void onFailure() {
                            }
                        });
                //Observe live data changes
                movieDetailViewModel.getMovie().observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movie) {
                        if (movie != null) {
                            Log.d(TAG, "TITLE::" + movie.toString());
                            setUpRecyclerView();
                            updateInterface(movie);
                        }
                    }
                });
                //on touch favourites icon update
                detectDrawableChange();
            }
            //for favourites view
            else if (intent.hasExtra(MOVIE_ID_ROOM_DB)) {
                int movieIdIn;
                movieIdIn = intent.getIntExtra(MOVIE_ID_ROOM_DB, DEFAULT_TASK_ID);
                //View Model
                factory = new FavouritesViewModelFactory(movieDatabase, movieIdIn);
                detailViewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
                detailViewModel.getMovieLiveData().observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movie) {
                        detailViewModel.getMovieLiveData().removeObserver(this);
                        if (movie != null) {
                            setUpRecyclerView();
                            populateUIfromRoomDatabase(movie);
                        }
                    }
                });
                detectDrawableChange();
            }
        }
    }


    /**
     * Method to setup up reviewa
     *
     * @param page         page of review
     * @param movieReviews list of movie reviews that will be set to adapter
     */
    void setupReviewAapter(int page, List<MovieReviews> movieReviews) {
        if (mReviewsAdapter == null) {
            //Attach Reviews Adapter to Recycler View
            mReviewsAdapter = new ReviewsAdapter(movieReviews);
            mMoviesReviewRecyclerView.setAdapter(mReviewsAdapter);
        } else {
            if (page == 1) {
                mReviewsAdapter.clearMovies();
            }
            //appends movie results to list and updates recycler view
            mReviewsAdapter.setmMovieReviewsList(movieReviews);
        }
        if (!(movieReviews.size() > 0)) {
            final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
            mMoviesReviewRecyclerView.setVisibility(View.GONE);
            mMovieReviewEmptyTextView.setTypeface(custom_font);
            mMovieReviewEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method to setup video adapter
     *
     * @param movieVideos list of movie videos to setup video adapter
     */
    void setupVideoAdapter(List<MovieVideos> movieVideos) {
        //Attach Video Adapter to Recycler View
        if (mVideoAdapter == null) {
            mVideoAdapter = new VideoAdapter(movieVideos, iVideoClickHandler, iShareClickHandler);
            mMoviesTrailerRecyclerView.setAdapter(mVideoAdapter);
        } else {
            mVideoAdapter.clearMovies();
        }
        //appends movie results to list and updates recycler view
        mVideoAdapter.setMovieVideoList(movieVideos);
        if (!(movieVideos.size() > 0)) {
            mMoviesTrailerRecyclerView.setVisibility(View.GONE);
            final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
            mMovieTrailerEmptyTextView.setTypeface(custom_font);
            mMovieTrailerEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * method to detect favourites icon change
     */
    private void detectDrawableChange() {
        detailViewModel.isFavourites().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null)
                    if (movie.isFavourite()) {
                        mFavouritesIconImageView.setImageDrawable(getDrawable(R.drawable.ic_favorite_true));
                    }
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    void setUpRecyclerView() {
        //Movies Review Recycler View
        mMoviesReviewRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerReview = new LinearLayoutManager(this);
        mMoviesReviewRecyclerView.setLayoutManager(linearLayoutManagerReview);
        mMoviesReviewRecyclerView.setItemViewCacheSize(2);
        mMoviesReviewRecyclerView.setDrawingCacheEnabled(true);
        mMoviesReviewRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        //Movie Video Recycler View
        mMoviesTrailerRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerVideo = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mMoviesTrailerRecyclerView.setLayoutManager(linearLayoutManagerVideo);
        mMoviesTrailerRecyclerView.setItemViewCacheSize(2);
        mMoviesTrailerRecyclerView.setDrawingCacheEnabled(true);
        mMoviesTrailerRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
    }

    /**
     * @param movieResult Movie to be bound to views from intent
     */
    private void updateInterface(final Movie movieResult) {
        buildUi(movieResult);
        shareVideo();
        //Add to Favourites
        mFavouritesIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click");
                addToFavouritesDatabaseOperations(movieResult);
            }
        });
    }

    /**
     * @param movie movie to be bound to views from  room local database
     */
    private void populateUIfromRoomDatabase(final Movie movie) {
        buildUi(movie);
        //Disable Reviewa and Trailers
        mMovieReviewLabelTextView.setVisibility(View.GONE);
        mMovieTrailerLabelTextView.setVisibility(View.GONE);
        mFavouritesIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click");
                addToFavouritesDatabaseOperations(movie);
            }
        });
    }

    //method to build ui -reused
    private void buildUi(Movie movie) {
        String IMAGE_BASE_URL_BACKDROP = "http://image.tmdb.org/t/p/w780";
        String IMAGE_BASE_URL_POSTER = "http://image.tmdb.org/t/p/w185";
        //Setup Typeface
        final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        mMovieReleaseDateTextView.setTypeface(custom_font);
        mMovieTitleTextView.setTypeface(custom_font);
        mMovieOverviewTextView.setTypeface(custom_font);
        //Setup Visibility
        mMovieRatingRatingBar.setVisibility(View.VISIBLE);
        //Set Data
        mMovieRatingRatingBar.setRating(movie.getVoterAverage() / 2);
        mMovieTitleTextView.setText(movie.getMovieTitle());
        float movieavg = movie.getVoterAverage();
        String movieAvgString = Float.toString(movieavg);
        mRatingValueTextView.setText(movieAvgString);
        mMovieReleaseDateTextView.setText(movie.getMovieReleaseDate());
        if (movie.getMovieOverview() == null || movie.getMovieOverview().equals("")) {
            mMovieOverviewTextView.setText(getString(R.string.label_default));
        } else {
            mMovieOverviewTextView.setText(movie.getMovieOverview());
        }

        if (!isFinishing()) {
            Picasso.with(DetailActivity.this)
                    .load(IMAGE_BASE_URL_BACKDROP + movie.getBackdrop())
                    .error(R.drawable.test_back)
                    .placeholder(R.drawable.test_back)
                    .into(mMovieBackdropImageView);
        }
        if (!isFinishing()) {
            Picasso.with(DetailActivity.this)
                    .load(IMAGE_BASE_URL_POSTER + movie.getMoviePoster())
                    .error(R.drawable.test)
                    .placeholder(R.drawable.test)
                    .into(mMoviePosterImageView);
        }
    }

    //Share Video intent and open video in youtube
    private void shareVideo() {
        final String YOUTUBE_VIDEO_BASE_URL = "http://www.youtube.com/watch?v=%s";
        //Click Handling Sharing for text
        iShareClickHandler = new IShareClickHandler() {
            @Override
            public void onClick(MovieVideos movieVideos) {
                String YoutubeLink = String.format(YOUTUBE_VIDEO_BASE_URL, movieVideos.getKeyTrailer());
                openShareIntent(YoutubeLink);
            }
        };
        //Click Handling Youtube Intent
        iVideoClickHandler = new IVideoClickHandler() {
            @Override
            public void onClick(MovieVideos movieVideos) {
                String YoutubeLink = String.format(YOUTUBE_VIDEO_BASE_URL, movieVideos.getKeyTrailer());
                Log.d("ON CLICK", YoutubeLink);
                openYoutubeIntent(YoutubeLink);
            }
        };
    }

    /**
     * @param youtubeVideoLink youtube link to video passed in an  intent to open in browser or youtube app
     */
    private void openYoutubeIntent(String youtubeVideoLink) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoLink));
        if (youtubeIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(youtubeIntent);
        }
    }

    /**
     * @param linkToShare shared as text
     */
    private void openShareIntent(String linkToShare) {
        String mimeType = "text/plain";
        String title = getString(R.string.label_share);
        ShareCompat
                .IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(linkToShare)
                .startChooser();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void displayError() {
        new StyleableToast
                .Builder(DetailActivity.this)
                .text(getString(R.string.error_network_message))
                .textColor(Color.WHITE)
                .backgroundColor(Color.rgb(66, 165, 245))
                .length(Toast.LENGTH_LONG)
                .show();
        finish();
    }

    /**
     * @param movieToAdd movie to be added or removed from database if exists
     */
    public void addToFavouritesDatabaseOperations(final Movie movieToAdd) {
        //Values To Be Saved to database
        int movieId = movieToAdd.getMovieId();
        final String movieTitle = movieToAdd.getMovieTitle();
        String movieOverview = movieToAdd.getMovieOverview();
        String moviePoster = movieToAdd.getMoviePoster();
        String movieBackdrop = movieToAdd.getBackdrop();
        float movieAverage = movieToAdd.getVoterAverage();
        String movieReleaseDate = movieToAdd.getMovieReleaseDate();
        final Movie favMovie = new Movie(movieId, movieTitle, movieOverview, movieReleaseDate, moviePoster, movieBackdrop, movieAverage, true);
        //Executor Background Thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //To Avoid Unique Constraint Error
                //If it doesnt exist in the database add it
                if (movieDatabase.movieDAO().loadMovie(movieTitle) == null) {
                    movieDatabase.movieDAO().saveMovieAsFavourite(favMovie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast = new StyleableToast.Builder(DetailActivity.this);
                            mToast.text(getString(R.string.label_added_favs))
                                    .length(Toast.LENGTH_SHORT)
                                    .textColor(getResources().getColor(R.color.colorWhite))
                                    .backgroundColor(getResources().getColor(R.color.colorAlternate))
                                    .show();
                            mFavouritesIconImageView.setImageDrawable(getDrawable(R.drawable.ic_favorite_true));
                        }
                    });
                } else {
                    //Remove and display toast and change drawable
                    movieDatabase.movieDAO().removeMovieFromFavourites(favMovie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast = new StyleableToast.Builder(DetailActivity.this);
                            mToast.text(getString(R.string.label_removed_favs))
                                    .length(Toast.LENGTH_SHORT)
                                    .textColor(getResources().getColor(R.color.colorWhite))
                                    .backgroundColor(getResources().getColor(R.color.colorAccent))
                                    .show();
                            mFavouritesIconImageView.setImageDrawable(getDrawable(R.drawable.ic_favorite_false));
                        }
                    });
                }
            }
        });
    }

    /**
     * @param typeface custom font for labels
     */
    private void setupLabelsTypeface(Typeface typeface) {
        mLabelReleasedTextView.setTypeface(typeface);
        mLabelOverviewTextView.setTypeface(typeface);
        mMovieReviewLabelTextView.setTypeface(typeface);
        mMovieTrailerLabelTextView.setTypeface(typeface);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_MOVIE, Parcels.wrap(movieInstance));
        outState.putParcelable(KEY_MOVIE_REVIEWS, Parcels.wrap(movieReviewsList));
        outState.putInt(KEY_MOVIE_REVIEW_PAGE, movieReviewsPage);
        outState.putParcelable(KEY_MOVIE_VIDEOS, Parcels.wrap(movieVideosList));
    }
}
