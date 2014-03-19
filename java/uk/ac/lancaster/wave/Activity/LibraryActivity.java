package uk.ac.lancaster.wave.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Data.Model.Book;
import uk.ac.lancaster.wave.Data.Model.Tag;
import uk.ac.lancaster.wave.R;

public class LibraryActivity extends BaseActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.book = (Book) getIntent().getSerializableExtra("book");

        this.setContentView(R.layout.library_activity);

        this.setupStatusBar();

        RobotoTextView title = (RobotoTextView) findViewById(R.id.title);
        title.setText(book.title);

        RobotoTextView author = (RobotoTextView) findViewById(R.id.author);
        author.setText(book.author);

        Calendar loan = Calendar.getInstance();
        loan.setTimeInMillis(book.timestamp);
        Date dateLoan = loan.getTime();

        RobotoTextView timestamp = (RobotoTextView) findViewById(R.id.timestamp);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEEE, d MMMM yyyy");

        timestamp.setText("Due in: " + simpleDateFormat.format(loan.getTime()));
//        timestamp.setText("" + book.timestamp);

        ImageView cover = (ImageView) findViewById(R.id.cover);

        Picasso.with(this)
                .load(book.cover)
                .noFade()
                .fit()
                .centerCrop()
                .into(cover);

    }
}