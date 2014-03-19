package uk.ac.lancaster.wave.Data.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.lancaster.wave.Data.Model.Book;
import uk.ac.lancaster.wave.R;

public class BookAdapter extends ArrayAdapter<Book> {
    private Context context;
    private int resource;

    public BookAdapter(Context context, int resourceId, List<Book> list) {
        super(context, resourceId, list);
        this.context = context;
        this.resource = resourceId;
    }

    private class ViewHolder {
        ImageView cover;

        RobotoTextView title;
        RobotoTextView author;
        RobotoTextView timestamp;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Book book = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(this.resource, null);

            holder = new ViewHolder();
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);

            holder.title = (RobotoTextView) convertView.findViewById(R.id.title);
            holder.author = (RobotoTextView) convertView.findViewById(R.id.author);

            holder.timestamp = (RobotoTextView) convertView.findViewById(R.id.timestamp);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /**
         * Set book cover with Picasso.
         */
        Picasso.with(context)
                .load(book.cover)
                .noFade()
                .fit()
                .centerCrop()
                .into(holder.cover);

        holder.title.setText(book.title);
        holder.author.setText(book.author);

        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        // and get that as a Date
        Date today = c.getTime();

        // loan date
        Calendar loan = Calendar.getInstance();
        loan.setTimeInMillis(book.timestamp);
        Date dateLoan = loan.getTime();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEEE, d MMMM yyyy");

        holder.timestamp.setText("Due in: " + simpleDateFormat.format(loan.getTime()));

        return convertView;
    }
}
