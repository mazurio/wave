package uk.ac.lancaster.wave.Networking.Jobs;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.EventGetContactDone;
import uk.ac.lancaster.wave.Networking.Events.EventGetContactRunning;
import uk.ac.lancaster.wave.Networking.Rest;

public class GetContactJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;
    private String contact;
    private String auth;

    // This job requires network connectivity,
    // and should be persisted in case the application exits before job is completed.
    public GetContactJob(String auth, String contact) {
        super(new Params(Priority.LOW).requireNetwork().persist());
        this.id = jobCounter.incrementAndGet();
        this.contact = contact;
        this.auth = auth;
    }

    // Job has been saved to disk.
    // This is a good place to dispatch a UI event to indicate the job will eventually run.
    // In this example, it would be good to update the UI with the newly posted tweet.
    @Override
    public void onAdded() {
        /**
         * EventsBus: Fetching data.
         */
        EventBus.getDefault().post(new EventGetContactRunning());
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

        Contact data = null;

        try {
            data = rest.getContact("d.mazurkiewicz", this.contact);

            Repository.getInstance().createContact(data);
        } catch (Exception e) {
            throw new Throwable();
        }

        EventBus.getDefault().post(new EventGetContactDone(data));

        Log.d("Job", "Do all this onRun() stuff using Retrofit.");
    }

    // Job has exceeded retry attempts or shouldReRunOnThrowable() has returned false.
    @Override
    protected void onCancel() {
        // TODO: Show error notification
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        /**
         * Handles network exceptions.
         */
        return true;
    }
}