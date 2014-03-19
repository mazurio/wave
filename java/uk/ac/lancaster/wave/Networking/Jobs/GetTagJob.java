package uk.ac.lancaster.wave.Networking.Jobs;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Data.Model.Tag;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.EventGetTagDone;
import uk.ac.lancaster.wave.Networking.Events.EventGetTagRunning;
import uk.ac.lancaster.wave.Networking.Rest;

public class GetTagJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;
    private String auth;
    private String tag;

    // This job requires network connectivity,
    // and should be persisted in case the application exits before job is completed.
    public GetTagJob(String auth, String tag) {
        super(new Params(Priority.LOW).requireNetwork().persist());
        this.id = jobCounter.incrementAndGet();
        this.auth = auth;
        this.tag = tag;
    }

    // Job has been saved to disk.
    // This is a good place to dispatch a UI event to indicate the job will eventually run.
    // In this example, it would be good to update the UI with the newly posted tweet.
    @Override
    public void onAdded() {
        /**
         * EventsBus: Fetching data.
         */
        EventBus.getDefault().post(new EventGetTagRunning());
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

        Tag data = null;

        try {
            Log.d("Job", this.tag);

            data = rest.getTag(this.auth, this.tag);

            Log.d("Job", data.id + "\n" + data.title + "\n" + data.description + "\n" + data.image);

            Repository.getInstance().createTag(data);

            // put into database
//            repository.createTag(data);

        } catch (Exception e) {
            Log.d("Job", e.getMessage());
            throw new Throwable();
        }

        EventBus.getDefault().post(new EventGetTagDone(data));

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