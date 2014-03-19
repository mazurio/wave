package uk.ac.lancaster.wave.Activity;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.nfc.NfcAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Activity.Navigation.Drawer;
import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.EventGetContactDone;
import uk.ac.lancaster.wave.Networking.Events.EventGetContactRunning;
import uk.ac.lancaster.wave.Networking.Events.GCM.GCMAnnouncement;
import uk.ac.lancaster.wave.Networking.Events.GCM.GCMAppointment;
import uk.ac.lancaster.wave.Networking.Jobs.Appointment.CancelAppointment.CancelAppointmentDone;
import uk.ac.lancaster.wave.Networking.Jobs.Appointment.ConfirmAppointment.ConfirmAppointmentDone;
import uk.ac.lancaster.wave.Networking.Jobs.GetBookJob;
import uk.ac.lancaster.wave.Networking.Jobs.GetContactJob;
import uk.ac.lancaster.wave.Networking.Jobs.GetTagJob;
import uk.ac.lancaster.wave.R;
import uk.ac.lancaster.wave.View.Contacts.FragmentContacts;
import uk.ac.lancaster.wave.View.Events.FragmentEvents;
import uk.ac.lancaster.wave.View.Events.Nested.Day;
import uk.ac.lancaster.wave.View.Library.FragmentLibrary;
import uk.ac.lancaster.wave.View.Modules.FragmentModules;
import uk.ac.lancaster.wave.View.Profile.FragmentProfile;
import uk.ac.lancaster.wave.View.Tags.FragmentTags;

public class MainActivity extends BaseActivity implements Drawer.NavigationDrawerCallbacks {
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private AuthenticatorManager authenticatorManager;
    private NfcAdapter mNfcAdapter;
    private JobManager jobManager;
    private Repository repository;

    private Drawer drawer;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        /**
         * Database Repository and Job Manager.
         */
        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.authenticatorManager = WaveApplication.getInstance().getAuthenticatorManager();
        this.repository = Repository.getInstance();

        /**
         * Event Bus
         */
        EventBus.getDefault().register(this);

        /**
         * Check if session exists, if not then launch authenticator activity.
         */
        this.authenticatorManager.checkSession();

        /**
         * Setup NFC Reading.
         */
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

//        if (mNfcAdapter == null) {
//            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
//            return;
//        } else {
//            if(!(mNfcAdapter.isEnabled())) {
//                Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
//            } else {
//                this.handleIntent(getIntent());
//            }
//        }

        /**
         * Setup status bar.
         */
        this.setupStatusBar();

        /**
         * Setup navigation drawer.
         */
        this.drawer = (Drawer) getSupportFragmentManager().findFragmentById(R.id.drawer);
        this.drawer.setUp(R.id.drawer, (DrawerLayout) findViewById(R.id.layout));
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Check if session still exists, if not then launch authenticator activity.
         */
        this.authenticatorManager.checkSession();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
//        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
//        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
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

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    /**
     * @param activity The corresponding {@link BaseActivity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final BaseActivity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        /**
         * Notice that this is the same filter as in our manifest.
         */
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final BaseActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
     *
     * @param intent
     */
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d("NFC", "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            /**
             * In case we would still use the Tech Discovered Intent
             */
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        /**
         * Update the main content by replacing fragments.
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment defaultFragment;

        this.position = position;

        switch(position) {
            case 0: {
                defaultFragment = FragmentProfile.newInstance();
                break;
            }
            case 1: {
                defaultFragment = FragmentTags.newInstance();
                break;
            }
            case 2: {
                defaultFragment = FragmentModules.newInstance();
                break;
            }
            case 3: {
                defaultFragment = FragmentContacts.newInstance();
                break;
            }
            case 4: {
                defaultFragment = Day.newInstance();
                break;
            }
            case 5: {
                defaultFragment = FragmentLibrary.newInstance();
                break;
            }
            default: {
                defaultFragment = FragmentProfile.newInstance();
                break;
            }
        }

        /**
         * Begin transaction.
         */
        fragmentManager.beginTransaction().replace(R.id.container, defaultFragment).commit();
    }

    /**
     * Only show items in the action bar relevant to this screen
     * if the drawer is not showing. Otherwise, let the drawer
     * decide what to show in the action bar.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!(drawer.isDrawerOpen())) {
            getMenuInflater().inflate(R.menu.main, menu);

            /**
             * Return to navigation mode list (Spinner) when events fragment is in use.
             */
//            if(this.position == 4) {
//                getActionBar().setDisplayShowTitleEnabled(false);
//                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//            }
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                Intent scanIntent = new Intent(getApplicationContext(), ScanActivity.class);
                this.startActivity(scanIntent);
                break;
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                break;
//                this.overridePendingTransition(R.anim.open_next, R.anim.close_main);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e("NFC", "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(getApplicationContext(), "NFC approached: " + result, Toast.LENGTH_SHORT).show();
                handleNfcTag(result);
                Log.d("NFC", " " + result);
            }
        }
    }

    private void handleNfcTag(String result) {
        /**
         * Split tag into type (tag) and (data) content.
         */
        String parts[] = result.split(":");
        String type = parts[0];
        String content = parts[1];

        if(type.matches("tag")) {
            this.jobManager.addJobInBackground(
                    new GetTagJob(authenticatorManager.getUsername(), content)
            );
        } else if(type.matches("user")) {
            this.jobManager.addJobInBackground(
                    new GetContactJob(authenticatorManager.getUsername(), content)
            );
        } else if(type.matches("book")) {
            this.jobManager.addJobInBackground(
                    new GetBookJob(authenticatorManager.getUsername(), content)
            );
        }
    }

    /**
     * NEW Announcement FROM GCM Receiver
     * @param event
     */
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GCMAnnouncement event) {
        Log.d("MAIN", "GCM Announcement event received.");
//        repository.createAnnouncement(event.announcement);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GCMAppointment event) {
        Log.d("MAIN", "GCM appointment event received.");
//        repository.createAppointment(event.appointment);
    }
}
