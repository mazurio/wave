package uk.ac.lancaster.wave.Networking.GCM;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.GCM.GCMAnnouncement;
import uk.ac.lancaster.wave.Networking.Events.GCM.GCMAppointment;
import uk.ac.lancaster.wave.R;

public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.d("GCM", "error" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d("GCM", "deleted" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d("GCM", "Received: " + extras.toString());
                handleBundle(extras);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMReceiver.completeWakefulIntent(intent);
    }

    private void handleBundle(Bundle bundle)
    {
        String type = bundle.getString("type");

        if(type.matches("announcement"))
        {
            handleAnnouncement(bundle);
        } else if(type.matches("appointment")) {
            handleAppointment(bundle);
        }
    }

    private void handleAnnouncement(Bundle bundle)
    {
        String module = bundle.getString("module");
        String user = bundle.getString("user");
        String title = bundle.getString("title");
        String description = bundle.getString("description");
        String timestamp = bundle.getString("timestamp");

        /**
         * Check if we have send the announcement.
         */
        if(!(user.matches(WaveApplication.getInstance().getAuthenticatorManager().getUsername()))) {
            final Announcement announcement = new Announcement(
                    module,
                    user,
                    title,
                    description,
                    Long.parseLong(timestamp)
            );

            Repository.getInstance().createAnnouncement(announcement);

            EventBus.getDefault().post(new GCMAnnouncement(announcement));

            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(module)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                            .setContentText(description);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void handleAppointment(Bundle bundle)
    {
        String timestamp = bundle.getString("timestamp");
        String title = bundle.getString("title");
        String description = bundle.getString("description");
        String status = bundle.getString("status");
        String sender = bundle.getString("sender");
        String receiver = bundle.getString("receiver");
        String date = bundle.getString("date");

        final Appointment appointment = new Appointment(
                Long.parseLong(timestamp),
                title,
                description,
                status,
                sender,
                receiver,
                Long.parseLong(date)
        );

        Repository.getInstance().createAppointment(appointment);

        EventBus.getDefault().post(new GCMAppointment(appointment));

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                        .setContentText(description);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}