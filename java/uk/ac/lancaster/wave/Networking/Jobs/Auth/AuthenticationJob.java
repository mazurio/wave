package uk.ac.lancaster.wave.Networking.Jobs.Auth;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Data.Model.Book;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.Data.Model.Module;
import uk.ac.lancaster.wave.Data.Model.Tag;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.Auth.Authenticated;
import uk.ac.lancaster.wave.Networking.Events.Auth.Authenticating;
import uk.ac.lancaster.wave.Networking.Events.EventUnauthorized;
import uk.ac.lancaster.wave.Networking.GCM.GCMConfig;
import uk.ac.lancaster.wave.Networking.Jobs.Priority;
import uk.ac.lancaster.wave.Networking.Rest;

public class AuthenticationJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;

    private String username;
    private String password;

    private Context context;

    // This job requires network connectivity,
    // and should be persisted in case the application exits before job is completed.
    public AuthenticationJob(String username, String password, Context context) {
        super(new Params(Priority.HIGH).requireNetwork());

        this.id = jobCounter.incrementAndGet();

        this.username = username;
        this.password = password;
        this.context = context;
    }

    // Job has been saved to disk.
    // This is a good place to dispatch a UI event to indicate the job will eventually run.
    // In this example, it would be good to update the UI with the newly posted tweet.
    @Override
    public void onAdded() {
        /**
         * EventsBus: Fetching data.
         */
        EventBus.getDefault().post(new Authenticating());
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

        Rest.APIAuthList data = null;

        String registrationId = null;
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this.context);
            registrationId = gcm.register(GCMConfig.SENDER_ID);

            Log.d("AUTH", registrationId);
        } catch(IOException e) {
            Log.d("Exception", e.getMessage());
            e.printStackTrace();
        }


        try {
            data = rest.auth(this.username, this.password, registrationId);
            Log.d("AUTH", this.username + " " + this.password + "\n" + registrationId);
        } catch(RetrofitError e) {
            Response response = e.getResponse();

            if(response != null && response.getStatus() == 401) {
                Log.d("AUTH", "401: Unauthorized.");
            }

            EventBus.getDefault().post(new EventUnauthorized());
            throw new Throwable("401");
        }

        /**
         * Tags:
         */
        if(data.tags != null) {
            if(!(data.tags.isEmpty())) {
                for(Tag tag : data.tags) {
                    Log.d("JOBS", tag.id + " - " + tag.title);
                    Repository.getInstance().createTag(tag);
                }
            }
        }

        /**
         * Contacts:
         */
        if(data.contacts != null) {
            if(!(data.contacts.isEmpty())) {
                for(Contact contact : data.contacts) {
                    Log.d("JOBS", contact.email + " - " + contact.username);
                    Repository.getInstance().createContact(contact);
                }
            }
        }

        /**
         * Books:
         */
        if(data.books != null) {
            if(!(data.books.isEmpty())) {
                for(Book book : data.books) {
                    Log.d("JOBS", book.id + " - " + book.title);
                    Repository.getInstance().createBook(book);
                }
            }
        }

        /**
         * Modules:
         */
        if(data.modules != null) {
            if(!(data.modules.isEmpty())) {
                for(Module module : data.modules) {
                    Log.d("JOBS", module.id + " - " + module.title);
                    Repository.getInstance().createModule(module);
                }
            }
        }

        /**
         * Announcements:
         */
        if(data.announcements != null) {
            if(!(data.announcements.isEmpty())) {
                for(Announcement announcement : data.announcements) {
                    Log.d("JOBS", announcement.timestamp + " - " + announcement.title);
                    Repository.getInstance().createAnnouncement(announcement);
                }
            }
        }

        /**
         * Appointments:
         */
        if(data.appointments != null) {
            if(!(data.appointments.isEmpty())) {
                for(Appointment appointment : data.appointments) {
                    Log.d("JOBS", appointment.timestamp + " - " + appointment.title);
                    Repository.getInstance().createAppointment(appointment);
                }
            }
        }

        /**
         * EventBus: Fetched all data.
         */
        EventBus.getDefault().post(new Authenticated(data.auth));



        Log.d("Job", "Do all this onRun() stuff using Retrofit.");
    }

    // Job has exceeded retry attempts or shouldReRunOnThrowable() has returned false.
    @Override
    protected void onCancel() {
        // TODO: Show error notification
        Log.d("JOB", "onCancel()");
        Log.d("AUTH", this.username + " " + this.password);
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        /**
         * Handles network exceptions.
         */
        Log.d("JOB", "shouldBeRunOnThrowable (Exception)");
        Log.d("AUTH", this.username + " " + this.password);

        if(throwable.getMessage().matches("401")) {
            return false;
        }

        return true;
    }
}
