package uk.ac.lancaster.wave.Authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import uk.ac.lancaster.wave.Activity.AuthenticatorActivity;
import uk.ac.lancaster.wave.Database.Repository;

public class AuthenticatorManager {
    private Repository repository;

    private Context context;
    private SharedPreferences sharedPreferences;
    private Editor editor;

    public AuthenticatorManager(Context context) {
        this.repository = Repository.getInstance();

        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(AccountGeneral.PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void createSession(
            String username,
            String photo,
            String firstname,
            String lastname,
            String description,
            String email,
            String phone) {
        editor.putBoolean(AccountGeneral.IS_LOGIN, true);

        editor.putString(AccountGeneral.ACCOUNT_USERNAME, username);

        editor.putString(AccountGeneral.ACCOUNT_PHOTO, photo);

        editor.putString(AccountGeneral.ACCOUNT_FIRSTNAME, firstname);
        editor.putString(AccountGeneral.ACCOUNT_LASTNAME, lastname);

        editor.putString(AccountGeneral.ACCOUNT_DESCRIPTION, description);

        editor.putString(AccountGeneral.ACCOUNT_EMAIL, email);
        editor.putString(AccountGeneral.ACCOUNT_PHONE, phone);

        editor.commit();
    }

    public String getUsername() {
        return this.sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, null);
    }

    public String getFirstname() {
        return this.sharedPreferences.getString(AccountGeneral.ACCOUNT_FIRSTNAME, null);
    }

    public String getLastname() {
        return this.sharedPreferences.getString(AccountGeneral.ACCOUNT_LASTNAME, null);
    }

    public String getFullname() {
        return this.getFirstname() + " " + this.getLastname();
    }

    public String getDescription() {
        return this.sharedPreferences.getString(AccountGeneral.ACCOUNT_DESCRIPTION, null);
    }

    /**
     * Returns Student instead of student.
     *
     * @return String of description. First letter is uppercase.
     */
    public String getReadableDescription() {
        String description = this.sharedPreferences.getString(AccountGeneral.ACCOUNT_DESCRIPTION, "");

        if(description.isEmpty()) {
            return description;
        } else if(description.length() <= 1) {
            return description;
        } else {
            return Character.toUpperCase(description.charAt(0)) + description.substring(1);
        }
    }

    public String getEmail() {
        return this.sharedPreferences.getString(AccountGeneral.ACCOUNT_EMAIL, "");
    }

    public String getPhone() {
        return this.sharedPreferences.getString(AccountGeneral.ACCOUNT_PHONE, "");
    }

    public boolean isStudent() {
        if(sharedPreferences.getString(AccountGeneral.ACCOUNT_DESCRIPTION, "student").matches("student")) {
            return true;
        } else {
            return false;
        }
    }

    public void checkSession() {
        if(!this.isLoggedIn()) {
            logoutUser();
        }
    }

    public void logoutUser() {
        /**
         * Get user account name.
         */
        String accoutName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "ACCOUNT_USERNAME");

        AccountManager accountManager = AccountManager.get(context);
        Account account = new Account(accoutName, AccountGeneral.ACCOUNT_TYPE);

        this.editor.clear();
        this.editor.commit();
        this.repository.delete();

        try {
            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    // This is the line that actually starts the call to remove the account.
                    try {
                        boolean wasAccountDeleted = future.getResult();
                    } catch (Exception e) {
                        Log.d("AUTH", "Logging out exception" + e);
                    }

                }
            }, null);
            Log.d("AUTH", "account removed");
        } catch(Exception e) {
            Log.d("AUTH", "Logging out exception" + e);
        }

        Intent intent = new Intent(context, AuthenticatorActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        context.startActivity(intent);
    }

    public void logoutUserWithoutActivity() {
        this.editor.clear();
        this.editor.commit();
        this.repository.delete();
    }

    /**
     * Is user logged in?
     *
     * @return
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(AccountGeneral.IS_LOGIN, false);
    }
}
