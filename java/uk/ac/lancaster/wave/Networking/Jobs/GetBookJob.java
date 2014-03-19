package uk.ac.lancaster.wave.Networking.Jobs;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import uk.ac.lancaster.wave.Data.Model.Book;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.EventGetBookDone;
import uk.ac.lancaster.wave.Networking.Events.EventGetBookRunning;
import uk.ac.lancaster.wave.Networking.Rest;

public class GetBookJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;
    private String book;
    private String auth;

    // This job requires network connectivity,
    // and should be persisted in case the application exits before job is completed.
    public GetBookJob(String auth, String book) {
        super(new Params(Priority.LOW).requireNetwork().persist());
        this.id = jobCounter.incrementAndGet();
        this.book = book;
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
        EventBus.getDefault().post(new EventGetBookRunning());
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

        Book data = null;

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 2); // adds one hour

        try {

            data = rest.getBook(auth, this.book, cal.getTimeInMillis());

            Repository.getInstance().createBook(data);
        } catch (Exception e) {
            throw new Throwable();
        }

        EventBus.getDefault().post(new EventGetBookDone(data));

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