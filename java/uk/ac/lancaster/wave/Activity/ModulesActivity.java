package uk.ac.lancaster.wave.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Activity.Action.SetAnnouncement;
import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Data.Adapter.AnnouncementAdapter;
import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Data.Model.Module;
import uk.ac.lancaster.wave.Networking.Jobs.Announcement.AnnouncementDoneEvent;
import uk.ac.lancaster.wave.R;

public class ModulesActivity extends BaseActivity {
    private Module module;

    private LinearLayout tooltip;
    private ListView listView;

    private ArrayList<Announcement> list;
    private AnnouncementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        this.module = (Module) getIntent().getSerializableExtra("module");

        this.setContentView(R.layout.modules_activity);
        this.setupStatusBar();
        this.setupActionBar(module.id, module.title, true);
        this.setupView();
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

    private void setupView() {
        this.tooltip = (LinearLayout) this.findViewById(R.id.tooltip);
        this.listView = (ListView) this.findViewById(R.id.listView);

        this.list = this.repository.getAnnouncementsByModule(module);
        if(this.list != null) {
            if(!(this.list.isEmpty())) {
                this.tooltip.setVisibility(View.GONE);

                this.adapter = new AnnouncementAdapter(
                        this,
                        R.layout.modules_activity_item,
                        this.list
                );

                this.listView.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!authenticatorManager.isStudent()) {
            getMenuInflater().inflate(R.menu.module_activity, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_announcement:
                Intent intent = new Intent(getApplicationContext(), SetAnnouncement.class);
                intent.putExtras(getIntent().getExtras());

                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(AnnouncementDoneEvent event) {
        /**
         * TODO: This is poor design, affects large data sets and needs to be changed asap.
         */
        this.adapter = null;
        this.adapter = new AnnouncementAdapter(
                this,
                R.layout.modules_activity_item,
                this.list
        );
        this.listView.setAdapter(adapter);

//        this.adapter.notifyDataSetChanged();
    }
}
