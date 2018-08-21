package smoovie.apps.com.kayatech.smoovie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences.Editor se = sp.edit();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (preference instanceof ListPreference) {

                se.putString(preference.getKey(),((ListPreference) preference).getValue());
                se.apply();
                String language = sp.getString(preference.getKey(), "");
                setLanguagePreferenceSummary(preference, language);
            }
        }
    }

    private void setLanguagePreferenceSummary(Preference preference, String Language) {
        ListPreference mLanguageListPreference = (ListPreference) preference;
        int preferenceIndex = mLanguageListPreference.findIndexOfValue(Language);
        if (preferenceIndex >= 0) {
            mLanguageListPreference.setSummary(mLanguageListPreference.getEntries()[preferenceIndex]);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (null != pref) {
            if (pref instanceof ListPreference) {
                String languageValue = sharedPreferences.getString(pref.getKey(), "");

                setLanguagePreferenceSummary(pref, languageValue);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
