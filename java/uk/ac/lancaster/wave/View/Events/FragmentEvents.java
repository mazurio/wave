package uk.ac.lancaster.wave.View.Events;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import uk.ac.lancaster.wave.R;
import uk.ac.lancaster.wave.View.Events.Nested.Day;
import uk.ac.lancaster.wave.View.Events.Nested.Week;

public class FragmentEvents extends Fragment {
    private ActionBar actionBar;
    private SpinnerAdapter spinnerAdapter;

    public static Fragment newInstance() {
        return new FragmentEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Display dropdown menu using action bar.
         */
//        this.actionBar = getActivity().getActionBar();
//        this.actionBar.setDisplayShowTitleEnabled(false);
//        this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//
//        this.spinnerAdapter = ArrayAdapter.createFromResource(
//                getActivity(),
//                R.array.action_events,
//                android.R.layout.simple_spinner_dropdown_item
//        );

        this.actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                switch (itemPosition) {
                    case 0: {
                        Fragment dayFragment = new Day();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.events, dayFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    }
                    default: {
                        Fragment weekFragment = new Week();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.events, weekFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    }
                }
            }
        });

        return (ViewGroup) inflater.inflate(R.layout.events, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
