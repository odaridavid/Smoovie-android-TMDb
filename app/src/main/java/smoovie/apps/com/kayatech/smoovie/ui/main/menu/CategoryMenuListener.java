package smoovie.apps.com.kayatech.smoovie.ui.main.menu;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.ui.main.async.MovieListAsync;
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.FavouriteMoviesCallback;
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.MovieListCallBack;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModel;

/**
 * Created By blackcoder
 * On 02/05/19
 **/
public class CategoryMenuListener implements PopupMenu.OnMenuItemClickListener {

    private MovieListCallBack mMovieListCallBackContext;
    private FavouriteMoviesCallback mFavouriteMoviesCallbackContext;
    private MainViewModel mMainViewModel;

    public CategoryMenuListener(MainViewModel mainViewModel, MovieListCallBack movieListCallBackContext, FavouriteMoviesCallback favouriteMoviesCallbackContext) {
        this.mMovieListCallBackContext = movieListCallBackContext;
        this.mMainViewModel = mainViewModel;
        this.mFavouriteMoviesCallbackContext = favouriteMoviesCallbackContext;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_top_rated:
                new MovieListAsync(mMainViewModel, Category.TOP_RATED, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_upcoming:
                new MovieListAsync(mMainViewModel, Category.UPCOMING, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_most_popular:
                new MovieListAsync(mMainViewModel, Category.POPULAR, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_favourites:
                mFavouriteMoviesCallbackContext.loadFavouriteMovies();
                return true;
        }
        return false;
    }
}
