package uk.ac.lancaster.wave.View.Contacts.Nested;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;

import uk.ac.lancaster.wave.Activity.ContactsActivity;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Data.Adapter.ContactAdapter;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.R;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class Contacts extends Fragment implements OnRefreshListener {
    private JobManager jobManager;
    private Repository repository;

    private PullToRefreshLayout mPullToRefreshLayout;
    private LinearLayout tooltip;
    private ListView listView;

    private ArrayList<Contact> list;
    private ContactAdapter adapter;

    public static Fragment newInstance() {
        return new Contacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.repository = Repository.getInstance();

//        EventBus.getDefault().register(this);

        return (ViewGroup) inflater.inflate(R.layout.contacts_contacts, null);
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

        this.tooltip = (LinearLayout) view.findViewById(R.id.tooltip);
        this.listView = (ListView) view.findViewById(R.id.listView);

        this.list = (ArrayList) this.repository.getAllContacts();

        if(this.list != null) {
            if(!(this.list.isEmpty())) {
                this.tooltip.setVisibility(View.GONE);

                this.adapter = new ContactAdapter(getActivity(), R.layout.contacts_contacts_item, this.list);

                this.listView.setAdapter(this.adapter);
                this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle contact = new Bundle();
                        contact.putSerializable("contact", list.get(position));

                        Intent intent = new Intent(getActivity(), ContactsActivity.class);
                        intent.putExtras(contact);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     * Handle action bar pull to refresh here.
     *
     * @param view
     */
    @Override
    public void onRefreshStarted(View view) {
        Log.d("onRefreshStarted", "Dupa");
//        jobManager.addJobInBackground(new FetchContactsJob());
    }

//    @SuppressWarnings("UnusedDeclaration")
//    public void onEventMainThread(FetchingContacts ignored) {
////        Toast.makeText(getActivity(), "Fetching modules..", Toast.LENGTH_SHORT).show();
//    }
//
//    @SuppressWarnings("UnusedDeclaration")
//    public void onEventMainThread(FetchedContacts ignored) {
//        Log.d("refreshComplete", "Dupa");
//        mPullToRefreshLayout.setRefreshComplete();
////        Toast.makeText(getActivity(), "Done.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            EventBus.getDefault().unregister(this);
//        } catch (Throwable t) {
//            //this may crash if registration did not go through. just be safe
//        }
//    }
}
