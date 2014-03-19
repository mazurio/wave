package uk.ac.lancaster.wave.Activity.Nested;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;
import java.util.List;

import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Data.Adapter.AppointmentAdapter;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.R;

public class Appointments extends Fragment {
    private Contact contact;

    private JobManager jobManager;
    private AuthenticatorManager authenticatorManager;
    private Repository repository;

    private ListView listViewAll;
    private List listAll;
    private AppointmentAdapter adapterAll;

    public static Fragment newInstance(Contact contact) {
        return new Appointments(contact);
    }

    public Appointments(Contact contact) {
        this.contact = contact;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.authenticatorManager = WaveApplication.getInstance().getAuthenticatorManager();
        this.repository = Repository.getInstance();

        return (ViewGroup) inflater.inflate(R.layout.contacts_activity_appointments, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.listViewAll = (ListView) view.findViewById(R.id.listViewAll);

        this.listAll = (ArrayList) this.repository.getAllAppointmentsByContact(
                authenticatorManager.getUsername(),
                contact.username
        );

        if(this.listAll != null) {
            if(!(this.listAll.isEmpty())) {
                this.adapterAll = new AppointmentAdapter(getActivity(), R.layout.profile_today_item, this.listAll);
                this.listViewAll.setAdapter(this.adapterAll);
            }
        }
    }
}
