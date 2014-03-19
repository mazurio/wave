package uk.ac.lancaster.wave.Networking.Jobs.Announcement;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Networking.Jobs.Priority;
import uk.ac.lancaster.wave.Networking.Rest;

public class SetAnnouncementJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;

    private Announcement announcement;

    // This job requires network connectivity,
    // and should be persisted in case the application exits before job is completed.
    public SetAnnouncementJob(Announcement announcement) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.id = jobCounter.incrementAndGet();

        this.announcement = announcement;
    }

    // Job has been saved to disk.
    // This is a good place to dispatch a UI event to indicate the job will eventually run.
    // In this example, it would be good to update the UI with the newly posted tweet.
    @Override
    public void onAdded() {
        /**
         * EventsBus: Fetching data.
         */
        EventBus.getDefault().post(new AnnouncementAddedEvent());

        Log.d("SetAppointmentJob", "onAdded()");
    }

    // Job logic goes here. In this example, the network call to post to Twitter is done here.
    @Override
    public void onRun() throws Throwable {
        /**
         * Looks like other fetch jobs has been added after me.
         * No reason to keep fetching many times.
         * Cancel me, let the other one fetch data.
         */
        if(id != jobCounter.get()) {
            return;
        }

        /**
         * Handle all networking using Retrofit library here.
         */
        RestAdapter adapter = new RestAdapter.Builder().setServer(Rest.SERVER).build();
        Rest rest = adapter.create(Rest.class);

        /**
         * Fetch data.
         */
        Announcement data = null;

        try {
            data = rest.insertAnnouncement("d.mazurkiewicz", announcement.module, announcement);
        } catch (Exception e) {
            throw new Throwable();
        }

        EventBus.getDefault().post(new AnnouncementDoneEvent());

        Log.d("SetAppointmentJob", "onRun()");
    }

    // Job has exceeded retry attempts or shouldReRunOnThrowable() has returned false.
    @Override
    protected void onCancel() {
        // TODO: Show error notification
        Log.d("SetAppointmentJob", "onCancel()");
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        /**
         * Handles network exceptions.
         */
        Log.d("SetAppointmentJob", "runOnThrowable()");
        return true;
    }
}