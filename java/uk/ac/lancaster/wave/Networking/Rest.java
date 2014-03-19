package uk.ac.lancaster.wave.Networking;

import java.util.Collection;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import uk.ac.lancaster.wave.Data.Model.Announcement;
import uk.ac.lancaster.wave.Data.Model.Appointment;
import uk.ac.lancaster.wave.Data.Model.Book;
import uk.ac.lancaster.wave.Data.Model.Contact;
import uk.ac.lancaster.wave.Data.Model.Module;
import uk.ac.lancaster.wave.Data.Model.Tag;
import uk.ac.lancaster.wave.Data.Model.User;

public interface Rest {
    public static final String SERVER = "https://mazurwebapp.appspot.com/_ah/api/backend/v1";
//    public static final String API_URL = "http://10.0.3.2:10080/_ah/api/backend/v1";

    public static class APIAuthList {
        public User auth;

        public List<Tag> tags;
        public List<Contact> contacts;
        public List<Book> books;
        public List<Module> modules;
        public List<Announcement> announcements;
        public List<Appointment> appointments;
    }

    public static class APISyncList {
        public List<Tag> tags;
        public List<Contact> contacts;
        public List<Book> books;
        public List<Module> modules;
        public List<Announcement> announcements;
        public List<Appointment> appointments;
    }

    /**
     * New backend starts here:
     */
    @POST("/auth")
    APIAuthList auth(
            @Query("username") String username,
            @Query("password") String password,
            @Query("device") String device
    );

    @POST("/sync")
    APISyncList sync(
            @Query("auth") String auth
    );

    @GET("/get.tag")
    Tag getTag(
            @Query("auth") String authToken,
            @Query("id") String id
    );

    @GET("/get.contact")
    Contact getContact(
            @Query("auth") String authToken,
            @Query("id") String id
    );

    @GET("/get.book")
    Book getBook(
            @Query("auth") String authToken,
            @Query("id") String id,
            @Query("timestamp") long timestamp
    );

    @POST("/insert.tag")
    Tag insertTag(
            @Query("auth") String auth,
            @Query("id") String id
    );

    @POST("/insert.contact")
    Contact insertContact(
            @Query("auth") String auth,
            @Query("id") String id
    );

    @POST("/insert.book")
    Book insertBook(
            @Query("auth") String auth,
            @Query("id") String id
    );

    @POST("/insert.appointment")
    Appointment insertAppointment(
            @Query("auth") String auth,
            @Body Appointment appointment
    );

    @POST("/confirm.appointment")
    Appointment confirmAppointment(
            @Query("auth") String auth,
            @Body Appointment appointment
    );

    @POST("/cancel.appointment")
    Appointment cancelAppointment(
            @Query("auth") String auth,
            @Body Appointment appointment
    );

    @POST("/insert.announcement")
    Announcement insertAnnouncement(
            @Query("auth") String auth,
            @Query("module") String module,
            @Body Announcement announcement
    );

//    @POST("/delete.announcement")
//    Announcement deleteAnnouncement(
//            @Query("auth") String auth,
//            @Query("module") String module,
//            @Body Announcement announcement
//    );

//    @GET("/list.tags")
//    APITagList listTags(
//            @Query("auth") String auth
//    );
//
//    @GET("/list.contacts")
//    APIContactList listContacts(
//            @Query("auth") String auth
//    );
//
//    @GET("/list.books")
//    APIBookList listBooks(
//            @Query("auth") String auth
//    );
//
//    @GET("/list.modules")
//    APIModuleList listModules(
//            @Query("auth") String auth
//    );
//
//    @GET("/list.appointments")
//    APIAppointmentList listAppointments(
//            @Query("auth") String auth
//    );
//
//    @GET("/list.announcements")
//    APIAnnouncementList listAnnouncements(
//            @Query("auth") String auth,
//            @Query("module") String module
//    );
}
