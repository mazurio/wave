package uk.ac.lancaster.wave.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.astuetz.PagerSlidingTabStrip;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import uk.ac.lancaster.wave.Activity.Nested.Appointments;
import uk.ac.lancaster.wave.Activity.Nested.Details;
import uk.ac.lancaster.wave.Activity.Nested.Messages;
import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.R;

public class ContactsActivity extends BaseActivity {
    private Contact contact;

    private ShareActionProvider shareActionProvider;

    private PagerSlidingTabStrip viewTabs;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.contacts_activity);

        this.setupStatusBar();
//        this.setupActionBar(contact.firstname + " " + contact.lastname, true);

        /**
         * Get bundle sent to this activity.
         */
        this.contact = (Contact) getIntent().getSerializableExtra("contact");

        /**
         * Setup fragments.
         */
        this.viewTabs = (PagerSlidingTabStrip) this.findViewById(R.id.viewTabs);
        this.viewPager = (ViewPager) this.findViewById(R.id.viewPager);

        this.viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), contact));
        this.viewTabs.setViewPager(this.viewPager);

        this.viewTabs.setIndicatorColor(getResources().getColor(R.color.branding_dark));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_activity, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        shareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Contact"
        );
        shareIntent.setType("text/plain");

        setShareIntent(shareIntent);

        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if(shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    /**
     * Adapter for nested fragments.
     */
    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private Contact contact;

        public ViewPagerAdapter(FragmentManager fragmentManager, Contact contact) {
            super(fragmentManager);

            this.contact = contact;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return Details.newInstance(contact);
            } else if(position == 1) {
                return Appointments.newInstance(contact);
            } else {
                return Messages.newInstance();
            }
        }

        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Details";
            } else if(position == 1) {
                return "Appointments";
            } else {
                return "Messages";
            }
        }
    }
}
