package uk.ac.lancaster.wave.View.Events.Nested;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;

import uk.ac.lancaster.wave.Data.Adapter.AppointmentAdapter;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.R;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class Week extends Fragment implements OnRefreshListener {
    private PullToRefreshLayout mPullToRefreshLayout;
    private RobotoTextView textView;
    private ListView listView;

    private ArrayList<Appointment> list;

    public static Fragment newInstance() {
        return new Week();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.events_week, null);
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
                .listener(this)
                .setup(mPullToRefreshLayout);

        this.textView = (RobotoTextView) view.findViewById(R.id.textView);
        this.listView = (ListView) view.findViewById(R.id.listView);

        this.list = new ArrayList<Appointment>();

        /**
         * Check if list is empty.
         * If it is not then hide the text view.
         */
        if(this.list != null) {
            if(!(this.list.isEmpty())) {
                this.textView.setVisibility(View.GONE);
            }
        }

        /**
         * Set adapter for this list.
         */
        this.listView.setAdapter(new AppointmentAdapter(getActivity(), R.layout.events_item, this.list));
    }

    @Override
    public void onRefreshStarted(View view) {
        mPullToRefreshLayout.setRefreshComplete();
    }
}
