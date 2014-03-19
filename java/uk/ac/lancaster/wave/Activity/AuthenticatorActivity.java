package uk.ac.lancaster.wave.Activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoButton;
import com.devspark.robototextview.widget.RobotoEditText;
import com.path.android.jobqueue.JobManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import de.greenrobot.event.EventBus;
import uk.ac.lancaster.wave.Application.WaveApplication;
import uk.ac.lancaster.wave.Authentication.AccountAuthenticator;
import uk.ac.lancaster.wave.Authentication.AccountGeneral;
import uk.ac.lancaster.wave.Authentication.AuthenticatorManager;
import uk.ac.lancaster.wave.Data.Model.User;
import uk.ac.lancaster.wave.Networking.Events.Auth.Authenticated;
import uk.ac.lancaster.wave.Networking.Events.Auth.Authenticating;
import uk.ac.lancaster.wave.Networking.Events.EventUnauthorized;
import uk.ac.lancaster.wave.Networking.Jobs.Auth.AuthenticationJob;
import uk.ac.lancaster.wave.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    private AuthenticatorManager authenticatorManager;
    private JobManager jobManager;

    private RobotoEditText usernameEditText;
    private RobotoEditText passwordEditText;

    private RobotoButton loginButton;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.jobManager = WaveApplication.getInstance().getJobManager();
        this.authenticatorManager = WaveApplication.getInstance().getAuthenticatorManager();

        EventBus.getDefault().register(this);

        setupStatusBar();

        this.setContentView(R.layout.authenticator_activity);

        this.usernameEditText = (RobotoEditText) this.findViewById(R.id.username);
        this.passwordEditText = (RobotoEditText) this.findViewById(R.id.password);

        this.usernameEditText.setText("d.mazurkiewicz");
        this.passwordEditText.setText("password");

        this.loginButton = (RobotoButton) this.findViewById(R.id.login);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", usernameEditText.getText().toString() + " " + passwordEditText.getText().toString());

                jobManager.addJobInBackground(new AuthenticationJob(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        getApplicationContext()
                ));
            }
        });
    }

    private void setupStatusBar() {
        if(Build.VERSION.SDK_INT >= 19) {
            Log.d("BaseActivity", "API Level 19: KitKat, setting up status bar.");

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.branding_light));
        }
    }

    private void onLogin(User user) {
        if(AccountAuthenticator.accountExists(this)) {
            Toast.makeText(getBaseContext(), "Account already exists. Remove it from settings.", Toast.LENGTH_LONG).show();
        } else {
            authenticatorManager.createSession(
                    user.username,
                    user.photo,
                    user.firstname,
                    user.lastname,
                    user.description,
                    user.email,
                    user.phone
            );

            Account account = new Account(user.username, AccountGeneral.ACCOUNT_TYPE);
            AccountManager accountManager = AccountManager.get(this);

            Bundle accountBundle = new Bundle();
            accountBundle.putString(AccountManager.KEY_ACCOUNT_NAME, AccountGeneral.ACCOUNT_USERNAME);
            accountBundle.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);
            accountBundle.putString(AccountManager.KEY_AUTHTOKEN, AccountGeneral.ACCOUNT_USERNAME);

            if(accountManager.addAccountExplicitly(account, "password", accountBundle)) {
                Bundle result = new Bundle();

                result.putString(AccountManager.KEY_ACCOUNT_NAME, user.username);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);

                setAccountAuthenticatorResult(result);
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            finish();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(Authenticating ignored) {
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(Authenticated object) {
        this.onLogin(object.user);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(EventUnauthorized ignored) {
        Toast.makeText(this, "Unable to authorize.", Toast.LENGTH_SHORT).show();
    }
}
