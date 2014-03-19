package uk.ac.lancaster.wave.Data.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.lancaster.wave.Data.Model.Tag;
import uk.ac.lancaster.wave.R;

public class TagAdapter extends ArrayAdapter<Tag> {
    private Context context;
    private int resource;

    public TagAdapter(Context context, int resourceId, List<Tag> tagItems) {
        super(context, resourceId, tagItems);
        this.context = context;
        this.resource = resourceId;
    }

    private class ViewHolder {
        ImageView imageView;

        RobotoTextView title;
        RobotoTextView description;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Tag tag = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(this.resource, null);

            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            holder.title = (RobotoTextView) convertView.findViewById(R.id.title);
            holder.description = (RobotoTextView) convertView.findViewById(R.id.description);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context)
                .load(tag.image)
                .fit()
                .centerCrop()
                .noFade()
                .into(holder.imageView);

        if(holder.title != null) {
            holder.title.setText(tag.title);
        }

        if(holder.description != null) {
            holder.description.setText(tag.description);
        }

        return convertView;
    }
}
