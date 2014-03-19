package uk.ac.lancaster.wave.Authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import uk.ac.lancaster.wave.Activity.AuthenticatorActivity;

import static android.accounts.AccountManager.ERROR_CODE_CANCELED;
import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

public class AccountAuthenticator extends AbstractAccountAuthenticator {
    private Context context;
    private AuthenticatorManager authenticatorManager;

    public AccountAuthenticator(Context context) {
        super(context);
        this.context = context;
        this.authenticatorManager = new AuthenticatorManager(this.context);
    }

    /**
     * TODO:
     *  - Fix removing Lancaster University account from preferences.
     */
    @Override
    public Bundle getAccountRemovalAllowed(
            AccountAuthenticatorResponse response,
            Account account) throws NetworkErrorException {
//        Log.d("AUTH", "TRYING TO REMOVE AN ACCOUNT");

        this.authenticatorManager.logoutUserWithoutActivity();
//
//        Bundle result = super.getAccountRemovalAllowed(response, account);
//        session.logoutUserWithoutActivity();
//        return result;

        Bundle result = super.getAccountRemovalAllowed(response, account);

        if (result != null && result.containsKey(AccountManager.KEY_BOOLEAN_RESULT)
                && !result.containsKey(AccountManager.KEY_INTENT)) {
            final boolean removalAllowed = result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT);

            if (removalAllowed) {
                // Do my removal stuff here
//                session.logoutUserWithoutActivity();
            }
        }

        return result;
    }

    /**
     * Add account using Android Settings.
     * After intent is done, it should return to settings instead starting new activity.
     *
     * TODO: Return to settings instead of starting new activity (Main).
     *
     * @param response
     * @param accountType
     * @param authTokenType
     * @param requiredFeatures
     * @param options
     * @return
     * @throws NetworkErrorException
     */
    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse response,
            String accountType,
            String authTokenType,
            String[] requiredFeatures,
            Bundle options) throws NetworkErrorException {

        if(accountExists(this.context)) {
            final Bundle result = new Bundle();

            result.putInt(AccountManager.KEY_ERROR_CODE, ERROR_CODE_CANCELED);
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "Only one account allowed.");

            return result;
        } else {
            final Intent intent = new Intent(context, AuthenticatorActivity.class);

            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);

            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);

            return bundle;
        }
    }

    @Override
    public Bundle getAuthToken(
            AccountAuthenticatorResponse response,
            Account account,
            String authTokenType,
            Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse response,
            Account account,
            String[] features) throws NetworkErrorException {

        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle editProperties(
            AccountAuthenticatorResponse response,
            String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse response,
            Account account,
            Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(
            AccountAuthenticatorResponse response,
            Account account,
            String authTokenType,
            Bundle options) throws NetworkErrorException {
        return null;
    }

    public static boolean accountExists(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        if(accounts.length > 0) {
            return true;
        }

        return false;
    }
}
