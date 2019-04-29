package smoovie.apps.com.kayatech.smoovie;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class SmoovieApplication extends Application {
    private SmoovieApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (LeakCanary.isInAnalyzerProcess(instance)) {
            return;
        }
        LeakCanary.install(instance);
    }

    public SmoovieApplication getInstance() {
        return instance;
    }
}
