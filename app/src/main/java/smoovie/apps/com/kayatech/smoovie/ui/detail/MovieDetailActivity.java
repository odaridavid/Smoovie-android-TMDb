package smoovie.apps.com.kayatech.smoovie.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
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
import static smoovie.apps.com.kayatech.smoovie.util.PaletteExtractorUtil.getBitmapFromUrl;
import static smoovie.apps.com.kayatech.smoovie.util.PaletteExtractorUtil.getDarkVibrantColor;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsCallBack {

//    TODO 1.(Detail Activity) - Build Material Designed UI,Refactor Existing UI,Add Shimmer Effect and Pick Color from poster image
//    TODO 2.(Detail Activity) - Save Movie or Remove  Movie from favourites
//    TODO 3.(Detail Activity) - Ensures Content is loaded by language,Pass in Intent

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.toolbar_details)
    Toolbar mToolbar;
    @BindView(R.id.iv_poster_image_details)
    SmooviePosterImageView vSmooviePosterImageView;
    @BindView(R.id.backdrop_image_view)
    SmoovieBackdropImageView mSmoovieBackdropImageView;
    private int mMovieId;
    private String mMoviePoster;
    private AsyncTask mAsync;

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
        mAsync = new MovieDetailsAsyncTask(mDetailViewModel, "en-US", this).execute(mMovieId);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loading() {

    }

    @Override
    public void complete(final Movie movie) {
        setStatusBarColor(BACKDROP_BASE_URL + movie.getBackdrop());
        Picasso.with(this)
                .load(BACKDROP_BASE_URL + movie.getBackdrop())
                .into(mSmoovieBackdropImageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAsync.cancel(true);
    }

    public void setStatusBarColor(final String url) {
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
                    }
                });
            }
        });

    }
}
