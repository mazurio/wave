package uk.ac.lancaster.wave.Data.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;

import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.R;

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {
    private Context context;
    private int resource;
    private ArrayList<Announcement> list;

    public AnnouncementAdapter(Context context, int resourceId, ArrayList<Announcement> list) {
        super(context, resourceId, list);

        this.context = context;
        this.resource = resourceId;
        this.list = list;
    }

    private class ViewHolder {
        RobotoTextView title;
        RobotoTextView description;
        RobotoTextView user;

        ImageView options;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Announcement getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        final Announcement announcement = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(this.resource, null);

            holder.title = (RobotoTextView) convertView.findViewById(R.id.title);
            holder.description = (RobotoTextView) convertView.findViewById(R.id.description);
            holder.user = (RobotoTextView) convertView.findViewById(R.id.user);
//            holder.options = (ImageView) convertView.findViewById(R.id.action_settings);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(unquote(announcement.title));
        holder.description.setText(unquote(announcement.description));
        holder.user.setText(announcement.user);

        return convertView;
    }

    public static String unquote(String string) {
        return string = string.replace("\\n", "\n");
    }
}