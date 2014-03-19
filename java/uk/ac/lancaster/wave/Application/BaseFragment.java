package uk.ac.lancaster.wave.Application;

import android.support.v4.app.Fragment;

import com.path.android.jobqueue.JobManager;

import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Database.Repository;

public class BaseFragment extends Fragment {
    protected JobManager jobManager;
    protected AuthenticatorManager authenticatorManager;
    protected Repository repository;

    public BaseFragment() {
        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.authenticatorManager = WaveApplication.getInstance().getAuthenticatorManager();
        this.repository = Repository.getInstance();
    }
}
