package uk.ac.lancaster.wave.View.Tags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;

import uk.ac.lancaster.wave.Activity.TagsActivity;
import uk.ac.lancaster.wave.Application.BaseFragment;
import uk.ac.lancaster.wave.Data.Adapter.TagAdapter;
import uk.ac.lancaster.wave.Data.Model.Tag;
//import uk.ac.lancaster.wave.Networking.Jobs.FetchTagsJob;
import uk.ac.lancaster.wave.R;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class FragmentTags extends BaseFragment implements OnRefreshListener {
    private PullToRefreshLayout mPullToRefreshLayout;
    private LinearLayout tooltip;
    private GridView gridView;

    private ArrayList<Tag> list;
    private TagAdapter adapter;

    public static Fragment newInstance() {
        return new FragmentTags();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);

        return (ViewGroup) inflater.inflate(R.layout.tags, null);
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
                .theseChildrenArePullable(R.id.gridView)
                .theseChildrenArePullable(R.id.tooltip)
                .listener(this)
                .setup(mPullToRefreshLayout);

        this.tooltip = (LinearLayout) view.findViewById(R.id.tooltip);
        this.gridView = (GridView) view.findViewById(R.id.gridView);

        this.list = (ArrayList) this.repository.getAllTags();
        if(this.list != null) {
            if(!(this.list.isEmpty())) {
                this.tooltip.setVisibility(View.GONE);

                this.adapter = new TagAdapter(getActivity(), R.layout.tags_item, this.list);
                this.gridView.setAdapter(this.adapter);
                this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), TagsActivity.class);
                        intent.putExtra("tag", list.get(position));
                        startActivity(intent);
                    }
                });
            } else {
                this.gridView.setVisibility(View.GONE);
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
        mPullToRefreshLayout.setRefreshComplete();
//        jobManager.addJobInBackground(new FetchTagsJob());
    }

//    @SuppressWarnings("UnusedDeclaration")
//    public void onEventMainThread(FetchingTags ignored) {
////        Toast.makeText(getActivity(), "Fetching tags..", Toast.LENGTH_SHORT).show();
//    }
//
//    @SuppressWarnings("UnusedDeclaration")
//    public void onEventMainThread(FetchedTags ignored) {
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
