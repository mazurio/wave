package uk.ac.lancaster.wave.Activity.Action;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoButton;
import com.devspark.robototextview.widget.RobotoEditText;
import com.devspark.robototextview.widget.RobotoTextView;
import com.path.android.jobqueue.JobManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.Networking.Events.SetAppointment.Done;
import uk.ac.lancaster.wave.Networking.Events.SetAppointment.InProgress;
import uk.ac.lancaster.wave.Networking.Jobs.Appointment.SetAppointmentJob;
import uk.ac.lancaster.wave.R;

public class SetAppointment extends FragmentActivity {
    private JobManager jobManager;
    private AuthenticatorManager authenticatorManager;
    private Repository repository;

    private ActionBar actionBar;

    private Calendar calendar;
    private Appointment appointment;

    private RobotoButton date;
    private RobotoButton time;

    private boolean dateChosen = false;
    private boolean timeChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.action_set_appointment);

        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.authenticatorManager = WaveApplication.getInstance().getAuthenticatorManager();
        this.repository = Repository.getInstance();

        EventBus.getDefault().register(this);

        /**
         * Setup status bar.
         */
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.branding_light));

        /**
         * Get data from bundle.
         */
        Bundle bundle = getIntent().getExtras();

        final String username = bundle.getString("username");
        String firstname = bundle.getString("firstname");
        String lastname = bundle.getString("lastname");

        String fullname = firstname + " " + lastname;

        getActionBar().setTitle("Set Appointment");
        getActionBar().setSubtitle(fullname);

        /**
         * Setup Calendar
         */
        calendar = Calendar.getInstance();

        final RobotoEditText title = (RobotoEditText) findViewById(R.id.title);
        title.setText("Appointment");

        final RobotoEditText description = (RobotoEditText) findViewById(R.id.description);
        description.setText("Description.");

        date = (RobotoButton) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        time = (RobotoButton) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        RobotoButton cancel = (RobotoButton) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RobotoButton confirm = (RobotoButton) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateChosen == false || timeChosen == false) {
                    Toast.makeText(getApplicationContext(), "Please choose Time and Date", Toast.LENGTH_LONG).show();
                } else {
                    appointment = new Appointment(
                            Calendar.getInstance().getTimeInMillis(),
                            title.getText().toString(),
                            description.getText().toString(),
                            Appointment.REQUEST,
                            authenticatorManager.getUsername(),
                            username,
                            calendar.getTimeInMillis()
                    );
                    jobManager.addJobInBackground(new SetAppointmentJob(appointment));
                }
            }
        });
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

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(InProgress event) {
        Toast.makeText(this, "Sending an appointment", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(Done event) {
        Toast.makeText(this, "Appointment send", Toast.LENGTH_LONG).show();
        /**
         * Appointment send to server, update database.
         */
        Repository.getInstance().createAppointment(appointment);

        this.finish();
    }

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);

            DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);

            DatePicker dp = d.getDatePicker();
            dp.setMinDate(c.getTimeInMillis());

            return d;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            calendar.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date.setText(sdf.format(calendar.getTime()));

            dateChosen = true;
        }
    }

    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            time.setText(sdf.format(calendar.getTime()));

            timeChosen = true;
        }
    }
}
