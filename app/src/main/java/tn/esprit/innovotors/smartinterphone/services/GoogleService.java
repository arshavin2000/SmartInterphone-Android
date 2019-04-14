package tn.esprit.innovotors.smartinterphone.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import tn.esprit.innovotors.smartinterphone.models.User;


public class GoogleService {

    private Context context;
    private Activity activity;
    private static final String TAG_GOOGLE = "GOOGLE_LOG";

    public GoogleService(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

    }


    public void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            SigninService signinService = new SigninService(context, activity);
            signinService.signinWithFacebookorGmail(account.getGivenName() + "_" +account.getFamilyName(),"facebook",account.getEmail(),account.getGivenName() + " "+account.getFamilyName(),account.getPhotoUrl().toString());
        }
    }


    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            updateUI(account);
            // Signed in successfully, show authenticated UI.
            User user = new User();
            user.setEmail(account.getEmail());
            user.setName(account.getGivenName() + " "+account.getFamilyName() );
            user.setUsername(account.getGivenName() + "_" +account.getFamilyName());
            if (account.getPhotoUrl() != null)
                user.setUrlImage(account.getPhotoUrl().toString());


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG_GOOGLE, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


}
