package uk.ac.lancaster.wave.Database;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Data.Model.Book;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.Data.Model.Module;
import uk.ac.lancaster.wave.Data.Model.Tag;

public class Repository {
    private static Repository instance;

    public synchronized static Repository getInstance() {
        if(instance == null) {
            instance = new Repository();
        }

        return instance;
    }

    public Repository() {

    }

    public void delete() {
        DatabaseManager databaseManager = new DatabaseManager();
        DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

        databaseHelper.delete();

        databaseManager.releaseHelper(databaseHelper);
    }

    public int createTag(Tag tag) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            List<Tag> existing = databaseHelper.getTagDao().queryForEq("id", tag.id);
            if (existing.size() > 0) {
                Log.d("DAO", "Updating the existing tag.");

                databaseHelper.getTagDao().update(tag);
            } else {
                Log.d("DAO", "Creating new tag.");

                databaseHelper.getTagDao().create(tag);
            }

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int createContact(Contact contact) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            List<Contact> existing = databaseHelper.getContactDao().queryForEq("username", contact.username);
            if (existing.size() > 0) {
                Log.d("DAO", "Updating the existing contact.");

                databaseHelper.getContactDao().update(contact);
            } else {
                Log.d("DAO", "Creating new contact.");

                databaseHelper.getContactDao().create(contact);
            }

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int createBook(Book book) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            List<Book> existing = databaseHelper.getBookDao().queryForEq("id", book.id);
            if (existing.size() > 0) {
                Log.d("DAO", "Updating the existing book.");

                databaseHelper.getBookDao().update(book);
            } else {
                Log.d("DAO", "Creating new book.");

                databaseHelper.getBookDao().create(book);
            }

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int createModule(Module module) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());


            Module existing = databaseHelper.getModuleDao().queryForSameId(module);
            if(existing != null && existing.id.matches(module.id)) {
                Log.d("DAO", "Updating the existing module.");

                databaseHelper.getModuleDao().update(module);
            } else {
                Log.d("DAO", "Creating new module.");

                databaseHelper.getModuleDao().create(module);
            }

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int createAnnouncement(Announcement announcement) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            Announcement existing = databaseHelper.getAnnouncementDao().queryForSameId(announcement);
            if(existing != null && existing.timestamp == announcement.timestamp) {
                Log.d("DAO", "Updating the existing announcement.");

                databaseHelper.getAnnouncementDao().update(announcement);
            } else {
                Log.d("DAO", "Creating new announcement.");

                databaseHelper.getAnnouncementDao().create(announcement);
            }

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int createAppointment(Appointment appointment) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            Appointment existing = databaseHelper.getAppointmentDao().queryForSameId(appointment);
            if(existing != null && existing.timestamp == appointment.timestamp) {
                Log.d("DAO", "Updating the existing appointment.");

                databaseHelper.getAppointmentDao().update(appointment);
            } else {
                Log.d("DAO", "Creating new appointment.");

                databaseHelper.getAppointmentDao().create(appointment);
            }

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List getAllTags() {
        List list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            list = databaseHelper.getTagDao().queryForAll();

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List getAllModules() {
        List list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            list = databaseHelper.getModuleDao().queryForAll();

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public long getCountOfAnnouncements(Module module) {
        long count = 0;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            count = databaseHelper.getAnnouncementDao().countOf(
                    databaseHelper.getAnnouncementDao()
                            .queryBuilder()
                            .setCountOf(true)
                            .where()
                            .eq("module", module.id)
                            .prepare()
            );

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public List getAllContacts() {
        List list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            list = databaseHelper.getContactDao().queryForAll();

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List getAllBooks() {
        List list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            list = databaseHelper.getBookDao().queryForAll();

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Query announcements only for particular module.
     */
    public ArrayList getAnnouncementsByModule(Module module) {
        ArrayList list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            QueryBuilder<Announcement, Integer> queryBuilder = databaseHelper.getAnnouncementDao().queryBuilder();

            Where where = queryBuilder.where();
            where.eq("module", module.id);

            list = (ArrayList) databaseHelper.getAnnouncementDao().query(queryBuilder.prepare());

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List getAppointmentsRequests() {
        List list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            QueryBuilder<Appointment, Integer> queryBuilder = databaseHelper.getAppointmentDao().queryBuilder();

            queryBuilder.where().eq("status", Appointment.REQUEST);
            queryBuilder.orderBy("timestamp", true);

            list = databaseHelper.getAppointmentDao().query(queryBuilder.prepare());

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List getAppointmentsToday() {
        List list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            QueryBuilder<Appointment, Integer> queryBuilder = databaseHelper.getAppointmentDao().queryBuilder();

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            tomorrow.set(Calendar.HOUR_OF_DAY, 0);
            tomorrow.set(Calendar.MINUTE, 0);
            tomorrow.set(Calendar.SECOND, 0);
            tomorrow.set(Calendar.MILLISECOND, 0);

//            queryBuilder.where()
//                    .eq("status", Appointment.CONFIRM).or()
//                    .eq("status", Appointment.REQUEST)
//                    .and()
//                    .between("date", today.getTimeInMillis(), tomorrow.getTimeInMillis());

            queryBuilder.where()
                    .eq("status", Appointment.CONFIRM)
                    .and()
                    .between("date", today.getTimeInMillis(), tomorrow.getTimeInMillis());

            queryBuilder.orderBy("timestamp", true);

            list = databaseHelper.getAppointmentDao().query(queryBuilder.prepare());

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List getAllAppointmentsByContact(String sender, String receiver) {
        List list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = databaseManager.getHelper(WaveApplication.getInstance().getApplicationContext());

            QueryBuilder<Appointment, Integer> queryBuilder = databaseHelper.getAppointmentDao().queryBuilder();

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            tomorrow.set(Calendar.HOUR_OF_DAY, 0);
            tomorrow.set(Calendar.MINUTE, 0);
            tomorrow.set(Calendar.SECOND, 0);
            tomorrow.set(Calendar.MILLISECOND, 0);

            Where where = queryBuilder.where();
            where.or(where.and(where.eq("sender", sender), where.eq("receiver", receiver)),
                     where.and(where.eq("receiver", receiver), where.eq("sender", sender)));

            queryBuilder.orderBy("timestamp", true);

//            queryBuilder.where().between("date", today.getTimeInMillis(), tomorrow.getTimeInMillis());

            queryBuilder.orderBy("timestamp", true);

            list = databaseHelper.getAppointmentDao().query(queryBuilder.prepare());

            databaseManager.releaseHelper(databaseHelper);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}