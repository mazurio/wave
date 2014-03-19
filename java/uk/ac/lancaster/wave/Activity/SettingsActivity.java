package uk.ac.lancaster.wave.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.FragmentActivity;

import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.R;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setupStatusBar();
        this.setupActionBar("Settings", true);

        /**
         * Check if session still exists, if not then launch authenticator activity.
         */
        this.authenticatorManager.checkSession();

        /**
         * Setup Fragment
         */
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Check if session still exists, if not then launch authenticator activity.
         */
        this.authenticatorManager.checkSession();
    }

    private class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.addPreferencesFromResource(R.xml.settings);

            EditTextPreference editTextPreference = (EditTextPreference) findPreference("email");
            editTextPreference.setSummary(authenticatorManager.getEmail());

            Preference preference = (Preference) findPreference("logout");
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.preferences_alert_title)
                            .setMessage(R.string.preferences_alert_message)
                            .setPositiveButton(R.string.preferences_alert_positive, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    authenticatorManager.logoutUser();
                                }
                            })
                            .setNegativeButton(R.string.preferences_alert_negative, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * Do nothing.
                                     */
                                }
                            })
                            .show();
                    return true;
                }
            });
        }
    }
}
