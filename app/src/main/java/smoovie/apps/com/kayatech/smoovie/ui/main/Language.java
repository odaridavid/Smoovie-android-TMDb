package smoovie.apps.com.kayatech.smoovie.ui.main;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import smoovie.apps.com.kayatech.smoovie.R;

/**
 * Created By blackcoder
 * On 04/05/19
 **/
public final class Language {
    public static void setUpLocale(String language, Context context) {
        if (language.equals(context.getString(R.string.pref_language_val_chinese))) {
            Locale locale = new Locale("zh");
            Configuration config = context.getResources().getConfiguration();
            Locale.setDefault(locale);
            config.setLocale(locale);
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        if (language.equals(context.getString(R.string.pref_language_val_french))) {
            Locale locale = new Locale("fr");
            Configuration config = context.getResources().getConfiguration();
            Locale.setDefault(locale);
            config.setLocale(locale);
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        if (language.equals(context.getString(R.string.pref_language_val_german))) {
            Locale locale = new Locale("de");
            Locale.setDefault(locale);
            Configuration config = context.getResources().getConfiguration();
            config.setLocale(locale);
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        if (language.equals(context.getString(R.string.pref_language_val_english))) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = context.getResources().getConfiguration();
            config.setLocale(locale);
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }
}
