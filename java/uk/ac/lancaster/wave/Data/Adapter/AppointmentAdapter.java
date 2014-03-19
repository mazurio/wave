package uk.ac.lancaster.wave.Data.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.devspark.robototextview.widget.RobotoTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Networking.Jobs.Appointment.CancelAppointment.CancelAppointmentJob;
import uk.ac.lancaster.wave.Networking.Jobs.Appointment.ConfirmAppointment.ConfirmAppointmentJob;
import uk.ac.lancaster.wave.R;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    private Context context;
    private int resource;
    private ArrayList<Appointment> list;

    public AppointmentAdapter(
            Context context,
            int resourceId,
            List<Appointment> list
    ) {
        super(context, resourceId, list);

        this.context = context;
        this.resource = resourceId;
        this.list = (ArrayList) list;
    }

    public void setList(ArrayList<Appointment> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Appointment getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        RobotoTextView time;

        RobotoTextView title;
        RobotoTextView description;
        RobotoTextView status;

        ImageView options;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        final Appointment appointment = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(resource, null);

            holder.time = (RobotoTextView) convertView.findViewById(R.id.time);
            holder.title = (RobotoTextView) convertView.findViewById(R.id.title);
            holder.description = (RobotoTextView) convertView.findViewById(R.id.description);
            holder.status = (RobotoTextView) convertView.findViewById(R.id.status);
            holder.options = (ImageView) convertView.findViewById(R.id.action_settings);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String auth = WaveApplication.getInstance().getAuthenticatorManager().getUsername();

        Calendar calendar = Calendar.getInstance();

        Date date = new Date();
        date.setTime(appointment.date);

        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        holder.time.setText(sdf.format(calendar.getTime()));

        holder.title.setText(appointment.title);
        holder.description.setText(appointment.description);
        holder.status.setText(appointment.status);

        if(appointment.status.matches(Appointment.CANCEL)) {
            holder.options.setVisibility(View.GONE);
        }

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.options);

                if(appointment.sender.matches(auth)) {
                    // sender can cancel
                    // if canceled then nothing can be done
                    popupMenu.getMenuInflater().inflate(R.menu.appointment_sender, popupMenu.getMenu());
                } else {
                    if(appointment.status.matches(Appointment.CONFIRM)) {
                        popupMenu.getMenuInflater().inflate(R.menu.appointment_sender, popupMenu.getMenu());
                    } else {
                        popupMenu.getMenuInflater().inflate(R.menu.appointment_receiver, popupMenu.getMenu());
                    }
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case(R.id.action_confirm): {
                                WaveApplication.getInstance().getJobManager().addJobInBackground(
                                        new ConfirmAppointmentJob(appointment)
                                );
                                break;
                            }
                            case(R.id.action_cancel): {
                                WaveApplication.getInstance().getJobManager().addJobInBackground(
                                        new CancelAppointmentJob(appointment)
                                );
                                break;
                            }
                        }

                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        return convertView;
    }
}
