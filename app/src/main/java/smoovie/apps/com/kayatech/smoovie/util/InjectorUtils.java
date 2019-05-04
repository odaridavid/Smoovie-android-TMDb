package smoovie.apps.com.kayatech.smoovie.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;
import smoovie.apps.com.kayatech.smoovie.R;
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

    private static MoviesRepository provideRepository(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieApiServices vApiServices = NetworkAdapter
                .getRetrofitInstance()
                .create(MovieApiServices.class);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sp.getString(context.getString(R.string.pref_language_key), "");
        sp.registerOnSharedPreferenceChangeListener(changeListener);
        return MoviesRepository.getInstance(database.movieDAO(), vApiServices, executors, language);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MoviesRepository repository = provideRepository(context.getApplicationContext(), changeListener);
        return new MainViewModelFactory(repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MoviesRepository repository = provideRepository(context.getApplicationContext(), changeListener);
        return new DetailViewModelFactory(repository);
    }
}
