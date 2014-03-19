package uk.ac.lancaster.wave.View.Contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import uk.ac.lancaster.wave.R;
import uk.ac.lancaster.wave.View.Contacts.Nested.Contacts;
import uk.ac.lancaster.wave.View.Contacts.Nested.Messages;

public class FragmentContacts extends Fragment {
    private PagerSlidingTabStrip viewTabs;
    private ViewPager viewPager;

    public static Fragment newInstance() {
        return new FragmentContacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.contacts, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.viewTabs = (PagerSlidingTabStrip) view.findViewById(R.id.viewTabs);
        this.viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        this.viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        this.viewTabs.setViewPager(this.viewPager);

        this.viewTabs.setIndicatorColor(getResources().getColor(R.color.branding_light));
    }

    /**
     * Adapter for nested fragments.
     */
    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return Contacts.newInstance();
            } else {
                return Messages.newInstance();
            }
        }

        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Contacts";
            } else {
                return "Messages";
            }
        }
    }
}
