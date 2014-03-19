package uk.ac.lancaster.wave.Application;

import android.app.Application;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;

public class WaveApplication extends Application {
    private static WaveApplication instance;
    private JobManager jobManager;
    private AuthenticatorManager authenticatorManager;

    public WaveApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.configureJobManager();
        this.configureAuthenticatorManager();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    private void configureAuthenticatorManager() {
        authenticatorManager = new AuthenticatorManager(this);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public AuthenticatorManager getAuthenticatorManager() {
        return authenticatorManager;
    }

    public static WaveApplication getInstance() {
        return instance;
    }
}
