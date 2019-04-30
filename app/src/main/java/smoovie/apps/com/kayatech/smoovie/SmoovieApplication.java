package smoovie.apps.com.kayatech.smoovie;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class SmoovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SmoovieApplication vInstance = this;
        if (LeakCanary.isInAnalyzerProcess(vInstance)) {
            return;
        }
        LeakCanary.install(vInstance);
    }
}
