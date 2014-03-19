package uk.ac.lancaster.wave.View.Profile;

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
import uk.ac.lancaster.wave.View.Profile.Nested.Home;
import uk.ac.lancaster.wave.View.Profile.Nested.Requested;
import uk.ac.lancaster.wave.View.Profile.Nested.Today;

public class FragmentProfile extends Fragment {
    private PagerSlidingTabStrip viewTabs;
    private ViewPager viewPager;

    /**
     * Create one instance of this fragment.
     *
     * @return
     */
    public static Fragment newInstance() {
        return new FragmentProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.profile, null);
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
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return Home.newInstance();
            } else if(position == 1) {
                return Today.newInstance();
            } else {
                return Requested.newInstance();
            }
        }

        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Home";
            } else if(position == 1) {
                return "Today";
            } else {
                return "Requested";
            }
        }
    }
}
