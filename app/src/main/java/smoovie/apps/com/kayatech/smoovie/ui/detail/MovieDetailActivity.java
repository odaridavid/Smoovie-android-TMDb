package smoovie.apps.com.kayatech.smoovie.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.Reviews;
import smoovie.apps.com.kayatech.smoovie.model.Trailers;
import smoovie.apps.com.kayatech.smoovie.ui.detail.adapters.IShareTrailerHandler;
import smoovie.apps.com.kayatech.smoovie.ui.detail.adapters.IWatchTrailerClickHandler;
import smoovie.apps.com.kayatech.smoovie.ui.detail.adapters.ReviewsAdapter;
import smoovie.apps.com.kayatech.smoovie.ui.detail.adapters.TrailersAdapter;
import smoovie.apps.com.kayatech.smoovie.ui.detail.async.MovieDetailsAsyncTask;
import smoovie.apps.com.kayatech.smoovie.ui.detail.async.MovieDetailsCallBack;
import smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel.DetailViewModel;
import smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel.DetailViewModelFactory;
import smoovie.apps.com.kayatech.smoovie.util.InjectorUtils;
import smoovie.apps.com.kayatech.smoovie.util.SmoovieBackdropImageView;
import smoovie.apps.com.kayatech.smoovie.util.SmooviePosterImageView;
import smoovie.apps.com.kayatech.smoovie.util.threads.AppExecutors;

import static smoovie.apps.com.kayatech.smoovie.util.Constants.BACKDROP_BASE_URL;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_ID;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_POSTER;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.POSTER_BASE_URL;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.YOUTUBE_TRAILER_BASE_URL;
import static smoovie.apps.com.kayatech.smoovie.util.PaletteExtractorUtil.getBitmapFromUrl;
import static smoovie.apps.com.kayatech.smoovie.util.PaletteExtractorUtil.getDarkVibrantColor;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsCallBack {
    private final String KEY_MOVIE_PERSISTENCE = "movie";

//    TODO 1.(Detail Activity) - Build Material Designed UI,Refactor Existing UI,Add Shimmer Effect
//    TODO 2.(Detail Activity) - Save Movie or Remove  Movie from favourites
//    TODO 3.(Detail Activity) - Ensures Content is loaded by language from shared pref

    @BindView(R.id.toolbar_details)
    Toolbar mToolbar;
    @BindView(R.id.iv_poster_image_details)
    SmooviePosterImageView vSmooviePosterImageView;
    @BindView(R.id.backdrop_image_view)
    SmoovieBackdropImageView mSmoovieBackdropImageView;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout vCollapsingToolbarLayout;
    @BindView(R.id.text_view_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.rating_bar_movie_avg)
    RatingBar rbMovieRating;
    @BindView(R.id.text_view_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.text_view_overview)
    TextView tvOverview;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.recycler_view_trailers)
    RecyclerView rvTrailers;
    @BindView(R.id.tv_no_review)
    TextView tvNoReview;
    @BindView(R.id.tv_no_trailer)
    TextView tvNoTrailer;

    private int mMovieId;
    private String mMoviePoster;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        supportPostponeEnterTransition();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        vSmooviePosterImageView.setTransitionName("poster");
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mMovieId = intent.getExtras().getInt(KEY_MOVIE_ID, -1);
            mMoviePoster = intent.getStringExtra(KEY_MOVIE_POSTER);
        }
        posterImageTransition();
        DetailViewModelFactory vDetailViewModelFactory = InjectorUtils.provideDetailViewModelFactory(this);
        DetailViewModel mDetailViewModel = ViewModelProviders.of(this, vDetailViewModelFactory).get(DetailViewModel.class);
        if (savedInstanceState != null) {
            mMovie = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_PERSISTENCE));
            Log.d(MovieDetailActivity.class.getSimpleName(), "Saved Instance: " + mMovie.toString());
            complete(mMovie);
        } else {
            Log.d(MovieDetailActivity.class.getSimpleName(), "Network");
            loadMovie(mDetailViewModel);
        }
    }

    private void loadMovie(DetailViewModel mDetailViewModel) {
        new MovieDetailsAsyncTask(mDetailViewModel, "en-US", this).execute(mMovieId);
    }

    private void posterImageTransition() {
        Picasso.with(this)
                .load(POSTER_BASE_URL + mMoviePoster)
                .into(vSmooviePosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null)
            outState.putParcelable(KEY_MOVIE_PERSISTENCE, Parcels.wrap(mMovie));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupReviews(List<Reviews> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            ReviewsAdapter vReviewsAdapter = new ReviewsAdapter(reviews);
            rvReviews.setHasFixedSize(true);
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.setAdapter(vReviewsAdapter);
        } else {
            rvReviews.setVisibility(View.GONE);
            tvNoReview.setVisibility(View.VISIBLE);
        }
    }

    private void setupTrailers(List<Trailers> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            TrailersAdapter vTrailersAdapter = new TrailersAdapter(trailers, new IWatchTrailerClickHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    viewTrailer(trailers);
                }
            }, new IShareTrailerHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    shareTrailerLink(trailers);
                }
            });
            rvTrailers.setHasFixedSize(true);
            rvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvTrailers.setAdapter(vTrailersAdapter);
        } else {
            rvTrailers.setVisibility(View.GONE);
            tvNoTrailer.setVisibility(View.VISIBLE);
        }
    }

    private void viewTrailer(Trailers trailers) {
        String trailerLink = String.format(YOUTUBE_TRAILER_BASE_URL, trailers.getKey());
        Intent videoPlayerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerLink));
        if (videoPlayerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(videoPlayerIntent);
        }
    }

    private void shareTrailerLink(Trailers trailers) {
        String trailerLink = String.format(YOUTUBE_TRAILER_BASE_URL, trailers.getKey());
        String mimeType = "text/plain";
        String title = getString(R.string.label_share);
        ShareCompat
                .IntentBuilder
                .from(MovieDetailActivity.this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText("Check Out This Trailer \n" + trailerLink + " \n For More Reviews and Trailers,check out Smoovie at {Smoovie Google Play Link Here} ")
                .startChooser();
    }

    public void setStatusBarColorFromBackdrop(final String url) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap sBitmap = getBitmapFromUrl(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int backGroundColor = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
                        if (getDarkVibrantColor(sBitmap) != null) {
                            backGroundColor = getDarkVibrantColor(sBitmap).getRgb();
                        }
                        getWindow().setStatusBarColor(backGroundColor);
                        vCollapsingToolbarLayout.setContentScrimColor(backGroundColor);
                    }
                });
            }
        });
    }

    @Override
    public void loading() {

    }

    @Override
    public void complete(Movie movie) {
        setStatusBarColorFromBackdrop(BACKDROP_BASE_URL + movie.getBackdrop());
        Picasso.with(MovieDetailActivity.this)
                .load(BACKDROP_BASE_URL + movie.getBackdrop())
                .into(mSmoovieBackdropImageView);
        vCollapsingToolbarLayout.setTitleEnabled(false);
        tvMovieTitle.setText(movie.getMovieTitle());
        rbMovieRating.setRating(movie.getVoterAverage() / 2);
        tvReleaseDate.setText(movie.getMovieReleaseDate());
        tvOverview.setText(movie.getMovieOverview());
        setupTrailers(movie.getTrailers());
        setupReviews(movie.getReviews());
        mMovie = movie;
    }
}
