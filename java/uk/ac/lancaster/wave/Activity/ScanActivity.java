package uk.ac.lancaster.wave.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Application.BaseActivity;
import uk.ac.lancaster.wave.Networking.Events.EventGetBookDone;
import uk.ac.lancaster.wave.Networking.Events.EventGetContactDone;
import uk.ac.lancaster.wave.Networking.Events.EventGetTagDone;
import uk.ac.lancaster.wave.Networking.Jobs.GetBookJob;
import uk.ac.lancaster.wave.Networking.Jobs.GetContactJob;
import uk.ac.lancaster.wave.Networking.Jobs.GetTagJob;
import uk.ac.lancaster.wave.R;

public class ScanActivity extends BaseActivity implements ScanditSDKListener {
    private ScanditSDKAutoAdjustingBarcodePicker mPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        this.setupStatusBar();

        if (ScanditSDKBarcodePicker.canRunPortraitPicker()) {
            mPicker = new ScanditSDKAutoAdjustingBarcodePicker(
                    this,
                    "dKfjYj5SEeObMFBn/PescLlfdVywDMLAi2kUrorKhPw",
                    ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK
            );

        }

        mPicker.getOverlayView().addListener(this);
        mPicker.getOverlayView().setTorchEnabled(false);
        mPicker.getOverlayView().setBeepEnabled(true);
        mPicker.getOverlayView().setVibrateEnabled(true);

        this.setContentView(mPicker);
    }

    @Override
    protected void onResume() {
        mPicker.startScanning();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPicker.stopScanning();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            //this may crash if registration did not go through. just be safe
        }
    }

    @Override
    public void didScanBarcode(String barcode, String symbology) {
        String cleanedBarcode = "";
        for (int i = 0 ; i < barcode.length(); i++) {
            if (barcode.charAt(i) > 30) {
                cleanedBarcode += barcode.charAt(i);
            }
        }

        /**
         * Check if this barcode is valid wave smart tag.
         */
        if(cleanedBarcode.contains(":")) {
            String parts[] = cleanedBarcode.split(":");
            String barcodeType = parts[0];
            String barcodeContent = parts[1];

            Toast.makeText(this, barcodeType + ":" + barcodeContent, Toast.LENGTH_LONG).show();

            /**
             * if barcode scanned is not currently logged in user.
             */
            if(!(barcodeType.matches("user") && barcodeContent.matches(authenticatorManager.getUsername()))) {

                if(barcodeType.matches("tag")) {
                    this.jobManager.addJobInBackground(
                            new GetTagJob(authenticatorManager.getUsername(), barcodeContent)
                    );
                }

                if(barcodeType.matches("contact") || barcodeType.matches("user")) {
                    this.jobManager.addJobInBackground(
                            new GetContactJob(authenticatorManager.getUsername(), barcodeContent)
                    );
                }

                if(barcodeType.matches("book")) {
                    this.jobManager.addJobInBackground(
                            new GetBookJob(authenticatorManager.getUsername(), barcodeContent)
                    );
                }
            }
        }
    }

    @Override
    public void didManualSearch(String entry) {

    }

    @Override
    public void didCancel() {

    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(EventGetTagDone done) {
        Intent intent = new Intent(this, TagsActivity.class);
        intent.putExtra("tag", done.tag);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(EventGetContactDone done) {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra("contact", done.contact);
        startActivity(intent);
    }

    // Listening to: the cranberries
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(EventGetBookDone done) {
        Intent intent = new Intent(this, LibraryActivity.class);
        intent.putExtra("book", done.book);
        startActivity(intent);
    }
}
