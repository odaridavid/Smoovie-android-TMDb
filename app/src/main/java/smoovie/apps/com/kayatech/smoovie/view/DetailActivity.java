package smoovie.apps.com.kayatech.smoovie.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviews;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieDetailsCallback;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieReviewsCallback;
import smoovie.apps.com.kayatech.smoovie.viewmodel.MovieDetailViewModel;
import smoovie.apps.com.kayatech.smoovie.viewmodel.MoviesRepository;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String MOVIE_ID = "movie_id";
    public static MoviesRepository moviesRepository;
    private ReviewsAdapter mReviewsAdapter;


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

    @BindView(R.id.iv_favcon)
    ImageView mFavconImageView;

    @BindView(R.id.tv_label_reviews)
    TextView mMovieReviewLabel;

    @BindView(R.id.rv_movies_review)
    RecyclerView mMoviesReviewRecyclerView;

    MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Bind Views
        ButterKnife.bind(this);




        Typeface custom_font_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        mLabelReleasedTextView.setTypeface(custom_font_thin);
        mLabelOverviewTextView.setTypeface(custom_font_thin);
        mMovieReviewLabel.setTypeface(custom_font_thin);

        //Check if intent contains extras
        if (getIntent().getExtras() != null) {
            final int mMovieId = Parcels.unwrap(getIntent().getParcelableExtra(MOVIE_ID));
            Log.d("Movie Being Passed", "" + mMovieId);
            setupToolbar();
            int page = 1;
            movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
            movieDetailViewModel.init(mMovieId,page, new IMovieDetailsCallback() {
                @Override
                public void onSuccess(Movie movie) {
                    if (movie != null) {
                        setUpRecyclerView();
                        updateInterface(movie);

                        // Toast.makeText(DetailActivity.this,""+movie.getMovieTitle(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError() {
                    displayError();

                }
            });
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
        }

        mFavconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourites(v);
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

   void setUpRecyclerView(){


           //Reference
           mMoviesReviewRecyclerView.setHasFixedSize(true);

           //Grid Layout Setup
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
           mMoviesReviewRecyclerView.setLayoutManager(linearLayoutManager);


           mMoviesReviewRecyclerView.setItemViewCacheSize(5);
           mMoviesReviewRecyclerView.setDrawingCacheEnabled(true);
           mMoviesReviewRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);



    }

    private void updateInterface(Movie movie) {
        String IMAGE_BASE_URL_BACKDROP = "http://image.tmdb.org/t/p/w780";
        String IMAGE_BASE_URL_POSTER = "http://image.tmdb.org/t/p/w185";

        final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        mMovieTitleTextView.setText(movie.getMovieTitle());
        mMovieTitleTextView.setTypeface(custom_font);
        mMovieOverviewTextView.setText(movie.getMovieOverview());
        mMovieOverviewTextView.setTypeface(custom_font);
        mMovieRatingRatingBar.setVisibility(View.VISIBLE);
        mMovieRatingRatingBar.setRating(movie.getVoterAverage() / 2);

        float movieavg = movie.getVoterAverage();
        String movieAvgString = Float.toString(movieavg);

        mRatingValueTextView.setText(movieAvgString);
        mMovieReleaseDateTextView.setText(movie.getMovieReleaseDate());
        mMovieReleaseDateTextView.setTypeface(custom_font);
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
        int movieId = movie.getMovieId();
       int page = 1;
        movieDetailViewModel.moviesRepository.getMovieReviews(page, movieId, new IMovieReviewsCallback() {
            @Override
            public void onSuccess(int page, List<MovieReviews> movieReviews) {

                if (mReviewsAdapter == null) {
                    mReviewsAdapter = new ReviewsAdapter(getApplicationContext(),movieReviews);
                    mMoviesReviewRecyclerView.setAdapter(mReviewsAdapter);

                }else {
                    if (page == 1) {
                        mReviewsAdapter.clearMovies();
                    }

                    //appends movie results to list and updates recycler view
                    mReviewsAdapter.setmMovieReviewsList(movieReviews);

                }
            }

            @Override
            public void onFailure() {

//             MovieReviews mr = new MovieReviews();
//             mr.setAuthor(getResources().getString(R.string.label_default));
//             mr.setContent(getResources().getString(R.string.label_default));
//             List<MovieReviews> mrlist = new ArrayList<>();
//             mrlist.add(mr);
//             mReviewsAdapter.setmMovieReviewsList(mrlist);

            }
        });
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

    private void addFavourites(View v) {
//         Drawable currentDrawable = mFavconImageView.getDrawable();
//         if(currentDrawable == getDrawable(R.drawable.ic_favorite_true)){
//             mFavconImageView.setImageDrawable(getDrawable(R.drawable.ic_favorite_false));
//             new StyleableToast
//                     .Builder(DetailActivity.this)
//                     .text("Removed from Favourites")
//                     .textColor(Color.WHITE)
//                     .backgroundColor(Color.rgb(66, 165, 245))
//                     .length(Toast.LENGTH_SHORT)
//                     .show();
//
//         }else {
//             mFavconImageView.setImageDrawable(getDrawable(R.drawable.ic_favorite_true));
//             new StyleableToast
//                     .Builder(DetailActivity.this)
//                     .text("Added to Favourites")
//                     .textColor(Color.WHITE)
//                     .backgroundColor(Color.rgb(66, 165, 245))
//                     .length(Toast.LENGTH_SHORT)
//                     .show();
//         }
    }
}
