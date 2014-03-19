package uk.ac.lancaster.wave.Database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.path.android.jobqueue.JobManager;

import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Data.Model.Book;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.Data.Model.Module;
import uk.ac.lancaster.wave.Data.Model.Tag;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 20;

    private Context context;
    private JobManager jobManager;

    private Dao<Tag, Integer> tag = null;
    private Dao<Module, Integer> module = null;
    private Dao<Contact, Integer> contact = null;
    private Dao<Book, Integer> book = null;
    private Dao<Appointment, Integer> appointment = null;
    private Dao<Announcement, Integer> announcement = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "onCreate");

        try {
            TableUtils.createTable(connectionSource, Tag.class);
            TableUtils.createTable(connectionSource, Module.class);
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, Book.class);
            TableUtils.createTable(connectionSource, Appointment.class);
            TableUtils.createTable(connectionSource, Announcement.class);
        } catch (SQLException e) {
            Log.e("JOBS", "Cannot create database.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, Tag.class, true);
            TableUtils.dropTable(connectionSource, Module.class, true);
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, Appointment.class, true);
            TableUtils.dropTable(connectionSource, Announcement.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Cannot drop database.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, Tag.class, true);
            TableUtils.dropTable(connectionSource, Module.class, true);
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, Appointment.class, true);
            TableUtils.dropTable(connectionSource, Announcement.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Cannot drop database.", e);
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        try {
            Log.i(DatabaseHelper.class.getName(), "delete()");

            TableUtils.clearTable(connectionSource, Tag.class);
            TableUtils.clearTable(connectionSource, Module.class);
            TableUtils.clearTable(connectionSource, Contact.class);
            TableUtils.clearTable(connectionSource, Book.class);
            TableUtils.clearTable(connectionSource, Appointment.class);
            TableUtils.clearTable(connectionSource, Announcement.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Cannot clear database.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our Tag class.
     * It will create it or just give the cached value.
     */
    public Dao<Tag, Integer> getTagDao() throws SQLException {
        if(tag == null) {
            tag = getDao(Tag.class);
        }
        return tag;
    }

    /**
     * Returns the Database Access Object (DAO) for our Module class.
     * It will create it or just give the cached value.
     */
    public Dao<Module, Integer> getModuleDao() throws SQLException {
        if(module == null) {
            module = getDao(Module.class);
        }
        return module;
    }

    /**
     * Returns the Database Access Object (DAO) for our Contact class.
     * It will create it or just give the cached value.
     */
    public Dao<Contact, Integer> getContactDao() throws SQLException {
        if(contact == null) {
            contact = getDao(Contact.class);
        }
        return contact;
    }

    /**
     * Returns the Database Access Object (DAO) for our Book class.
     * It will create it or just give the cached value.
     */
    public Dao<Book, Integer> getBookDao() throws SQLException {
        if(book == null) {
            book = getDao(Book.class);
        }
        return book;
    }

    /**
     * Returns the Database Access Object (DAO) for our Appointment class.
     * It will create it or just give the cached value.
     */
    public Dao<Appointment, Integer> getAppointmentDao() throws SQLException {
        if(appointment == null) {
            appointment = getDao(Appointment.class);
        }
        return appointment;
    }

    /**
     * Returns the Database Access Object (DAO) for our Announcement class.
     * It will create it or just give the cached value.
     */
    public Dao<Announcement, Integer> getAnnouncementDao() throws SQLException {
        if(announcement == null) {
            announcement = getDao(Announcement.class);
        }
        return announcement;
    }

    @Override
    public void close() {
        super.close();
        tag = null;
        module = null;
        contact = null;
        book = null;
        appointment = null;
        announcement = null;
    }
}
