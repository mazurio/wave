package uk.ac.lancaster.wave.View.Profile.Nested;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoTextView;
import com.path.android.jobqueue.JobManager;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Networking.Jobs.Sync.Event.Synced;
import uk.ac.lancaster.wave.Networking.Jobs.Sync.Event.Syncing;
import uk.ac.lancaster.wave.Networking.Jobs.Sync.SyncJob;
import uk.ac.lancaster.wave.R;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class Home extends Fragment implements OnRefreshListener {
    private AuthenticatorManager authenticatorManager;
    private JobManager jobManager;

    private PullToRefreshLayout mPullToRefreshLayout;

    public static Fragment newInstance() {
        return new Home();
    }

    public Home() {
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.authenticatorManager = WaveApplication.getInstance().getAuthenticatorManager();

        return (ViewGroup) inflater.inflate(R.layout.profile_home, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) view;

        // As we're using a ListFragment we create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)
                // Here we mark just the ListView and it's Empty View as pullable
                .theseChildrenArePullable(R.id.scrollView)
                .listener(this)
                .setup(mPullToRefreshLayout);

        RobotoTextView firstname = (RobotoTextView) view.findViewById(R.id.firstname);
        firstname.setText(this.authenticatorManager.getFirstname());

        RobotoTextView lastname = (RobotoTextView) view.findViewById(R.id.lastname);
        lastname.setText(this.authenticatorManager.getLastname());

        RobotoTextView description = (RobotoTextView) view.findViewById(R.id.description);
        description.setText(this.authenticatorManager.getReadableDescription());
    }

    @Override
    public void onRefreshStarted(View view) {
        jobManager.addJobInBackground(new SyncJob(authenticatorManager.getUsername(), this.getActivity()));
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(Syncing event) {
        Toast.makeText(getActivity(), "Sync in progress", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(Synced event) {
        mPullToRefreshLayout.setRefreshComplete();
        Toast.makeText(getActivity(), "Sync done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            //this may crash if registration did not go through. just be safe
        }
    }
}
