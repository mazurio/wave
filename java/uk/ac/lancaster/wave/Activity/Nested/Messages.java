package uk.ac.lancaster.wave.Activity.Nested;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.lancaster.wave.R;

public class Messages extends Fragment {
    public static Fragment newInstance() {
        return new Messages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.contacts_activity_messages, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
