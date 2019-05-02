package smoovie.apps.com.kayatech.smoovie.ui.main.async;

import android.os.AsyncTask;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieNetworkLite;
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.MovieListCallBack;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModel;

import static smoovie.apps.com.kayatech.smoovie.util.Constants.LANGUAGE;

/**
 * Created By blackcoder
 * On 01/05/19
 **/
public final class MovieListAsync extends AsyncTask<Void, Void, List<MovieNetworkLite>> {
    private MainViewModel mMainViewModel;
    private MovieListCallBack mMovieListCallBack;
    private Category mCategory;

    public MovieListAsync(MainViewModel mainViewModel, Category category, MovieListCallBack movieListCallBack) {
        mMainViewModel = mainViewModel;
        mMovieListCallBack = movieListCallBack;
        mCategory = category;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieListCallBack.inProgress();
    }

    @Override
    protected List<MovieNetworkLite> doInBackground(Void... voids) {
//                TODO Load Language From Preferences
//                TODO Add Pagination on Scrolling page to page
//                TODO Sort By Category
        return mMainViewModel.getMovies(mCategory, LANGUAGE, 1);
    }

    @Override
    protected void onPostExecute(List<MovieNetworkLite> movies) {
        super.onPostExecute(movies);
        mMovieListCallBack.onFinished(movies, mCategory);
    }
}
