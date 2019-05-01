package smoovie.apps.com.kayatech.smoovie.ui.main.menu;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.ui.main.MovieListAsync;
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.MovieListCallBack;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModel;

/**
 * Created By blackcoder
 * On 02/05/19
 **/
public class CategoryMenuListener implements PopupMenu.OnMenuItemClickListener {

    private String language;
    private MovieListCallBack mCallbackContext;
    private int page = 1;
    private MainViewModel mMainViewModel;

    public CategoryMenuListener(MainViewModel mainViewModel, String language, MovieListCallBack callbackContext) {
        this.language = language;
        this.mCallbackContext = callbackContext;
        this.mMainViewModel = mainViewModel;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_top_rated:
                new MovieListAsync(mMainViewModel, Category.TOP_RATED, mCallbackContext).execute();
                return true;
            case R.id.action_sort_upcoming:
                new MovieListAsync(mMainViewModel, Category.UPCOMING, mCallbackContext).execute();
                return true;
            case R.id.action_sort_most_popular:
                new MovieListAsync(mMainViewModel, Category.POPULAR, mCallbackContext).execute();
                return true;
            case R.id.action_sort_favourites:
//                TODO Implement Favourites with room
                return true;
        }
        return false;
    }
}
