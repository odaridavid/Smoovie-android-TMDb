package smoovie.apps.com.kayatech.smoovie.util;

import android.content.Context;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;
import smoovie.apps.com.kayatech.smoovie.db.MovieDatabase;
import smoovie.apps.com.kayatech.smoovie.network.MovieApiServices;
import smoovie.apps.com.kayatech.smoovie.network.NetworkAdapter;
import smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel.DetailViewModelFactory;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModelFactory;
import smoovie.apps.com.kayatech.smoovie.util.threads.AppExecutors;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class InjectorUtils {

    private static MoviesRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieApiServices vApiServices = NetworkAdapter
                .getRetrofitInstance()
                .create(MovieApiServices.class);
        return MoviesRepository.getInstance(database.movieDAO(), vApiServices, executors);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository);
    }
}
