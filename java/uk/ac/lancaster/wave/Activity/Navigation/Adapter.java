package uk.ac.lancaster.wave.Activity.Navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;

import uk.ac.lancaster.wave.R;

public class Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private int resource;

    private ArrayList<Item> data;

    public Adapter(Context context, int resource, ArrayList<Item> data) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.data = data;
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int position) {
        return this.data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * Reuse a given view, or inflate a new one from the xml.
         */
        View view;

        if (convertView == null) {
            if(position == 0) {
                view = this.inflater.inflate(R.layout.main_drawer_profile, parent, false);
            } else {
                view = this.inflater.inflate(resource, parent, false);
            }
        } else {
            view = convertView;
        }

        /**
         * Bind the data to the view object.
         */
        return this.bindData(view, position);
    }

    public View bindData(View view, int position) {
        /**
         * Make sure it's worth drawing the view.
         */
        if (this.data.get(position) == null) {
            return view;
        }

        /**
         * Pull out the object.
         */
        Item item = this.data.get(position);

        /**
         * Extract the view object.
         */
        View viewElement = view.findViewById(R.id.title);

        /**
         * Cast to the correct type.
         */
        RobotoTextView tv = (RobotoTextView) viewElement;

        /**
         * Set the value.
         */
        tv.setText(item.title);

        /**
         * Return the final view object.
         */
        return view;
    }
}