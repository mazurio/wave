package uk.ac.lancaster.wave.Data.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import uk.ac.lancaster.wave.Data.Model.Module;
import uk.ac.lancaster.wave.Database.Repository;
import uk.ac.lancaster.wave.R;

public class ModuleAdapter extends ArrayAdapter<Module> {
    private Context context;
    private int resource;

    public ModuleAdapter(Context context, int resourceId, List<Module> list) {
        super(context, resourceId, list);
        this.context = context;
        this.resource = resourceId;
    }

    private class ViewHolder {
        RobotoTextView module;
        RobotoTextView title;
        RobotoTextView announcements;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Module module = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new ViewHolder();
            holder.module = (RobotoTextView) convertView.findViewById(R.id.module);
            holder.title = (RobotoTextView) convertView.findViewById(R.id.title);
            holder.announcements = (RobotoTextView) convertView.findViewById(R.id.announcements);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(holder.module != null) {
            holder.module.setText(module.id);
        }

        if(holder.title != null) {
            holder.title.setText(module.title);
        }

        long count = Repository.getInstance().getCountOfAnnouncements(module);
        if(count == 1) {
            holder.announcements.setText("1 announcement");
        } else if(count > 0) {
            holder.announcements.setText(count + " announcements");
        } else {
            holder.announcements.setText("No announcements");
        }

        return convertView;
    }
}