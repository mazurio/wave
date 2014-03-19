package uk.ac.lancaster.wave.Activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.picasso.Picasso;

import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Data.Model.Tag;
import uk.ac.lancaster.wave.R;

public class TagsActivity extends BaseActivity {
    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setupStatusBar();

        this.tag = (Tag) getIntent().getSerializableExtra("tag");

        this.setContentView(R.layout.tags_activity);

        RobotoTextView title = (RobotoTextView) findViewById(R.id.title);
        title.setText(tag.title);

        RobotoTextView description = (RobotoTextView) findViewById(R.id.description);
        description.setText(tag.description);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load(tag.image)
                .noFade()
                .fit()
                .centerCrop()
                .into(imageView);

//        String summary = "" +
//                "<html>" +
//                "<body>" +
//                description.getText().toString() +
//                "</body>" +
//                "</html>";
//
//        WebView webView = (WebView) findViewById(R.id.webView);
//        webView.setBackgroundColor(getResources().getColor(R.color.light));
//        webView.loadData(summary, "text/html", null);
    }
}