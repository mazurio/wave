package uk.ac.lancaster.wave.Application;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.path.android.jobqueue.JobManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.R;

public abstract class BaseActivity extends FragmentActivity {
    protected JobManager jobManager;
    protected AuthenticatorManager authenticatorManager;
    protected Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.authenticatorManager = WaveApplication.getInstance().getAuthenticatorManager();
        this.repository = Repository.getInstance();
    }

    /**
     * Check if version of Android is at least 19 in order to setup KitKat status bar.
     */
    protected void setupStatusBar() {
        if(Build.VERSION.SDK_INT >= 19) {
            Log.d("BaseActivity", "API Level 19: KitKat, setting up status bar.");

//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.branding_light));
        }
    }

    protected void setupActionBar(String title) {
        getActionBar().setTitle(title);
    }

    protected void setupActionBar(String title, boolean homeAsUpEnabled) {
        getActionBar().setTitle(title);
        getActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    protected void setupActionBar(String title, String subtitle) {
        getActionBar().setTitle(title);
        getActionBar().setSubtitle(subtitle);
    }

    protected void setupActionBar(String title, String subtitle, boolean homeAsUpEnabled) {
        getActionBar().setTitle(title);
        getActionBar().setSubtitle(subtitle);
        getActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }
}
