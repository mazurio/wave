package uk.ac.lancaster.wave.Activity.Action;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoButton;
import com.devspark.robototextview.widget.RobotoEditText;

import java.util.Calendar;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Data.Model.Module;
import uk.ac.lancaster.wave.Networking.Jobs.Announcement.AnnouncementAddedEvent;
import uk.ac.lancaster.wave.Networking.Jobs.Announcement.AnnouncementDoneEvent;
import uk.ac.lancaster.wave.Networking.Jobs.Announcement.SetAnnouncementJob;
import uk.ac.lancaster.wave.R;

public class SetAnnouncement extends BaseActivity {
    private Module module;
    private Announcement announcement;

    private RobotoEditText title;
    private RobotoEditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        this.module = (Module) getIntent().getSerializableExtra("module");

        this.setContentView(R.layout.action_set_announcement);
        this.setupStatusBar();
        this.setupActionBar("Set Announcement");
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
        title = (RobotoEditText) findViewById(R.id.title);
        title.setText(module.id + ": Title");

        description = (RobotoEditText) findViewById(R.id.description);
        description.setText("Description.");

        RobotoButton cancel = (RobotoButton) findViewById(R.id.cancel);
        RobotoButton confirm = (RobotoButton) findViewById(R.id.confirm);
    }

    public void onClickCancel(View view) {
        this.finish();
    }

    public void onClickConfirm(View view) {
        Calendar calendar = Calendar.getInstance();
        this.announcement = new Announcement(
                module.id,
                authenticatorManager.getUsername(),
                title.getText().toString(),
                description.getText().toString(),
                calendar.getTimeInMillis()
        );
        this.jobManager.addJobInBackground(new SetAnnouncementJob(announcement));
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(AnnouncementAddedEvent event) {
        Toast.makeText(this, "Sending an announcement..", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(AnnouncementDoneEvent event) {
        Toast.makeText(this, "Announcement send.", Toast.LENGTH_LONG).show();

        this.repository.createAnnouncement(announcement);
        this.finish();
    }
}
