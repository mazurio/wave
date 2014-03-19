package uk.ac.lancaster.wave.Data.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import uk.ac.lancaster.wave.Activity.Action.SetAppointment;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.R;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private List<Contact> list;

    public ContactAdapter(Context context, int resourceId, List<Contact> list) {
        super(context, resourceId, list);

        this.context = context;
        this.resource = resourceId;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    private class ViewHolder {
        RobotoTextView username;

        RobotoTextView firstname;
        RobotoTextView lastname;

        RobotoTextView description;

        RobotoTextView email;
        RobotoTextView phone;

        ImageView settings;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Contact contact = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new ViewHolder();
//            holder.username = (RobotoTextView) convertView.findViewById(R.id.username);

            holder.firstname = (RobotoTextView) convertView.findViewById(R.id.firstname);
            holder.lastname = (RobotoTextView) convertView.findViewById(R.id.lastname);

            holder.description = (RobotoTextView) convertView.findViewById(R.id.description);

//            holder.email = (RobotoTextView) convertView.findViewById(R.id.email);
//            holder.phone = (RobotoTextView) convertView.findViewById(R.id.phone);

            holder.settings = (ImageView) convertView.findViewById(R.id.action_settings);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(holder.username != null) {
            holder.username.setText(contact.username);
        }

        if(holder.firstname != null) {
            holder.firstname.setText(contact.firstname);
        }

        if(holder.lastname != null) {
            holder.lastname.setText(contact.lastname);
        }

        /**
         * Change 'student' to 'Student' etc.
         */
        if(holder.description != null) {
            holder.description.setText(contact.getDescription());
        }

        if(holder.email != null) {
            holder.email.setText(contact.email);
        }

        if(holder.phone != null) {
            holder.phone.setText(contact.phone);
        }

        /**
         * TODO: Add R.string parameters for titles.
         */
        if(holder.settings != null) {
            holder.settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(context, holder.settings);
                    popupMenu.getMenuInflater().inflate(R.menu.contact, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()) {
                                /**
                                 * Send bundle to Set Appointment class
                                 * with the username of this contact.
                                 */
                                case(R.id.action_set_appointment): {
                                    Intent intent = new Intent(getContext(), SetAppointment.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putString("username", contact.username);
                                    bundle.putString("firstname", contact.firstname);
                                    bundle.putString("lastname", contact.lastname);

                                    intent.putExtras(bundle);

                                    getContext().startActivity(intent);

                                    return true;
                                }
                                case(R.id.action_email): {
                                    Intent emailIntent = new Intent(
                                            Intent.ACTION_SENDTO,
                                            Uri.fromParts("mailto", contact.email, null));

                                    getContext().startActivity(Intent.createChooser(emailIntent, "Email"));

                                    return true;
                                }
                                case(R.id.action_call): {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);

                                    callIntent.setData(Uri.parse("tel:" + contact.phone));

                                    getContext().startActivity(Intent.createChooser(callIntent, "Call"));

                                    return true;
                                }
                                case(R.id.action_share): {
                                    Intent shareIntent = new Intent();

                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(
                                            Intent.EXTRA_TEXT,
                                            "" + contact.firstname + " " + contact.lastname + "\n" +
                                                    "Description: " + contact.description + "\n" +
                                                    "Email: " + contact.email + "\n" +
                                                    "Phone: " + contact.phone
                                    );
                                    shareIntent.setType("text/plain");

                                    getContext().startActivity(shareIntent);

                                    return true;
                                }
                            }

                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });
        }

        return convertView;
    }
}
