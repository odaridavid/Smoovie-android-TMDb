package smoovie.apps.com.kayatech.smoovie.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.util.SmooviePosterImageView;

import static smoovie.apps.com.kayatech.smoovie.util.Constants.IMAGE_BASE_URL;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_ID;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_POSTER;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.toolbar_details)
    Toolbar mToolbar;
    @BindView(R.id.iv_poster_image_details)
    SmooviePosterImageView vSmooviePosterImageView;

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
        int x = intent.getExtras().getInt(KEY_MOVIE_ID, -1);
        String url = intent.getStringExtra(KEY_MOVIE_POSTER);

        Picasso.with(this)
                .load(IMAGE_BASE_URL + url)
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
}
