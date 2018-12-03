package com.example.manifest.bcreco.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.manifest.bcreco.R;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        // set summary for all EditTextPreference
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (p instanceof EditTextPreference) {
                String value = sharedPreferences.getString(p.getKey(), "");
                p.setSummary(value);
            }
        }

        // for validate correct input
        int[] prefKeys = {R.string.pref_ip_key, R.string.pref_port_key, R.string.pref_store_id_key};
        for (int key: prefKeys) {
            Preference preference = findPreference(getString(key));
            preference.setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // which preference was changed
        Preference preference = findPreference(key);
        if (preference != null) {
            if (preference instanceof EditTextPreference) {
                // set summary equals preference value
                String value = sharedPreferences.getString(preference.getKey(), "");
                preference.setSummary(value);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String preferenceKey = preference.getKey();
        if (preferenceKey.equals(getString(R.string.pref_ip_key))) {
            String ipAddress = (String) newValue;
            if (!PreferenceValidator.isIPV4(ipAddress)) {
                showErrorToast(getString(R.string.toast_message_incorrect_ip_address));
                return false;
            }
        } else if (preferenceKey.equals(getString(R.string.pref_port_key))) {
            String port = (String) newValue;
            if (!PreferenceValidator.isPortValid(port)) {
                showErrorToast(getString(R.string.toast_message_incorrect_port));
                return false;
            }
        } else if (preferenceKey.equals(getString(R.string.pref_store_id_key))) {
            String storeId = (String) newValue;
            if (!PreferenceValidator.isStoreIdValid(storeId)) {
                showErrorToast(getString(R.string.toast_message_incorrect_store_id));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void showErrorToast(String toastMessage) {
        Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

}
