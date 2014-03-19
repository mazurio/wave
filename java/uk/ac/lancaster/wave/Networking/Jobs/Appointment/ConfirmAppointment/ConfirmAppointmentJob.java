package uk.ac.lancaster.wave.Networking.Jobs.Appointment.ConfirmAppointment;


import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.SetAppointment.Done;
import uk.ac.lancaster.wave.Networking.Events.SetAppointment.InProgress;
import uk.ac.lancaster.wave.Networking.Jobs.Priority;
import uk.ac.lancaster.wave.Networking.Rest;

public class ConfirmAppointmentJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;
    private Appointment appointment;

    // This job requires network connectivity,
    // and should be persisted in case the application exits before job is completed.
    public ConfirmAppointmentJob(Appointment appointment) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.id = jobCounter.incrementAndGet();
        this.appointment = appointment;
    }

    // Job has been saved to disk.
    // This is a good place to dispatch a UI event to indicate the job will eventually run.
    // In this example, it would be good to update the UI with the newly posted tweet.
    @Override
    public void onAdded() {
        /**
         * EventsBus: Fetching data.
         */
//        EventBus.getDefault().post(new InProgress());

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

        Appointment data = null;

        try {
            data = rest.confirmAppointment("d.mazurkiewicz", appointment);
        } catch (Exception e) {
            throw new Throwable();
        }

        Repository.getInstance().createAppointment(data);

        EventBus.getDefault().post(new ConfirmAppointmentDone(data));

        Log.d("Confirm Appointment DONE", "onRun()");
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