package smoovie.apps.com.kayatech.smoovie.ui.main;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import smoovie.apps.com.kayatech.smoovie.R;

/**
 * Created By blackcoder
 * On 04/05/19
 **/
final class Language {
    static void setUpLocale(String language, Context context) {
        if (language.equals(context.getString(R.string.pref_language_val_chinese))) {
            Locale locale = new Locale("zh");
            configLocale(context, locale);
        } else if (language.equals(context.getString(R.string.pref_language_val_french))) {
            Locale locale = new Locale("fr");
            configLocale(context, locale);
        } else if (language.equals(context.getString(R.string.pref_language_val_german))) {
            Locale locale = new Locale("de");
            configLocale(context, locale);
        } else if (language.equals(context.getString(R.string.pref_language_val_english))) {
            Locale locale = new Locale("en");
            configLocale(context, locale);
        }
    }

    private static void configLocale(Context context, Locale locale) {
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
