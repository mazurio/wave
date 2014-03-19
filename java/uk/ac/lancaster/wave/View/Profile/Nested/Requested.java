package uk.ac.lancaster.wave.View.Profile.Nested;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Data.Adapter.AppointmentAdapter;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.GCM.GCMAppointment;
import uk.ac.lancaster.wave.Networking.Jobs.Appointment.CancelAppointment.CancelAppointmentDone;
import uk.ac.lancaster.wave.Networking.Jobs.Appointment.ConfirmAppointment.ConfirmAppointmentDone;
import uk.ac.lancaster.wave.R;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class Requested extends Fragment implements OnRefreshListener {
    private JobManager jobManager;
    private Repository repository;

    private PullToRefreshLayout mPullToRefreshLayout;
    private LinearLayout tooltip;
    private ListView listView;

    private ArrayList<Appointment> list;
    private AppointmentAdapter adapter;

    public static Fragment newInstance() {
        return new Requested();
    }

    public Requested() {
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.repository = Repository.getInstance();

        return (ViewGroup) inflater.inflate(R.layout.profile_requested, null);
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
                .theseChildrenArePullable(R.id.listView)
                .theseChildrenArePullable(R.id.tooltip)
                .listener(this)
                .setup(mPullToRefreshLayout);

        this.tooltip = (LinearLayout) view.findViewById(R.id.tooltip);
        this.listView = (ListView) view.findViewById(R.id.listView);

        this.list = (ArrayList) this.repository.getAppointmentsRequests();
        if(this.list != null) {
            if(!(this.list.isEmpty())) {
                this.tooltip.setVisibility(View.GONE);

                this.adapter = new AppointmentAdapter(getActivity(), R.layout.profile_today_item, this.list);
                this.listView.setAdapter(this.adapter);
            }
        }
    }

    @Override
    public void onRefreshStarted(View view) {
        mPullToRefreshLayout.setRefreshComplete();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GCMAppointment event) {
        ArrayList list = (ArrayList) this.repository.getAppointmentsToday();

        if(this.list != null && this.adapter != null) {
            this.adapter.setList(list);

            if(this.list.isEmpty()) {
                this.tooltip.setVisibility(View.VISIBLE);
            }

            this.adapter.notifyDataSetChanged();
        }
    }

    /**
     * Appointment confirmed.
     * Update the adapter.
     */
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ConfirmAppointmentDone event) {
        ArrayList list = (ArrayList) this.repository.getAppointmentsToday();

        if(this.list != null && this.adapter != null) {
            this.adapter.setList(list);

            if(this.list.isEmpty()) {
                this.tooltip.setVisibility(View.VISIBLE);
            }

            this.adapter.notifyDataSetChanged();
        }
    }

    /**
     * Appointment cancelled.
     * Update the adapter.
     */
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CancelAppointmentDone event) {
        ArrayList list = (ArrayList) this.repository.getAppointmentsToday();

        if(this.list != null && this.adapter != null) {
            this.adapter.setList(list);

            if(this.list.isEmpty()) {
                this.tooltip.setVisibility(View.VISIBLE);
            }

            this.adapter.notifyDataSetChanged();
        }
    }
}
