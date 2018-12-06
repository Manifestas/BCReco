package com.example.manifest.bcreco.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.manifest.bcreco.R;
import com.example.manifest.bcreco.data.DbConnectionParams;

import androidx.preference.PreferenceManager;

public class PrefHelper {

    private static DbConnectionParams dbConnectionParams;

    public static DbConnectionParams getDbConnectionParams(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ipPref = sharedPreferences.getString(context.getString(R.string.pref_ip_key),
                context.getString(R.string.pref_ip_default));
        String portPref = sharedPreferences.getString(context.getString(R.string.pref_port_key),
                context.getString(R.string.pref_port_default));
        String loginPref = sharedPreferences.getString(context.getString(R.string.pref_login_key),
                context.getString(R.string.pref_login_default));
        String passwordPref = sharedPreferences.getString(context.getString(R.string.pref_password_key),
                context.getString(R.string.pref_password_default));
        return new DbConnectionParams(ipPref, portPref, loginPref, passwordPref);
    }
}
