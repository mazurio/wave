package uk.ac.lancaster.wave.Activity.Nested;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.robototextview.widget.RobotoTextView;

import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.R;

public class Details extends Fragment {
    private Contact contact;

    public static Fragment newInstance(Contact contact) {
        return new Details(contact);
    }

    public Details(Contact contact) {
        this.contact = contact;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.contacts_activity_details, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RobotoTextView firstname = (RobotoTextView) view.findViewById(R.id.firstname);
        firstname.setText(contact.firstname);

        RobotoTextView lastname = (RobotoTextView) view.findViewById(R.id.lastname);
        lastname.setText(contact.lastname);

        RobotoTextView description = (RobotoTextView) view.findViewById(R.id.description);
        description.setText(contact.getDescription());

        RobotoTextView email = (RobotoTextView) view.findViewById(R.id.email);
        email.setText(contact.email);

        RobotoTextView phone = (RobotoTextView) view.findViewById(R.id.phone);
        phone.setText(contact.phone);
    }
}
