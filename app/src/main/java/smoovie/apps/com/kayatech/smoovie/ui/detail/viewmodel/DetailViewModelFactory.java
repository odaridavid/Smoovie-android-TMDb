package smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MoviesRepository mRepository;

    public DetailViewModelFactory(MoviesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mRepository);
    }
}
