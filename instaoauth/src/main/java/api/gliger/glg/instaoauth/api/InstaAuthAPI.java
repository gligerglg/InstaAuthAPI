package api.gliger.glg.instaoauth.api;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import api.gliger.glg.instaoauth.InstaAuthDialog;
import api.gliger.glg.instaoauth.SharedRepository;
import api.gliger.glg.instaoauth.Utill;
import api.gliger.glg.instaoauth.model.Profile;

public class InstaAuthAPI {

    private InstaAuthDialog instaAuthDialog;
    private Context context;
    private String clientId;
    private String redirectURL;

    private boolean redirectOnSuccess = true;
    private SharedRepository sharedRepository;


    private InstaAuthAPI(Context context, String clientId, String redirectURL, boolean redirectOnSuccess) {
        this.context = context;
        this.clientId = clientId;
        this.redirectURL = redirectURL;
        this.redirectOnSuccess = redirectOnSuccess;
        this.sharedRepository = SharedRepository.getInstance(context);
    }

    public static void getDefaultSession(Context context, InstagramSessionHandler instagramSessionHandler) {
        SharedRepository sharedRepository = SharedRepository.getInstance(context);
        if (sharedRepository.isLoggedIn())
            instagramSessionHandler.onProfileDataReceived(sharedRepository.getProfile());
        else
            instagramSessionHandler.onErrorOccurred(Utill.ERROR_SESSION_EXPIRE);
    }

    public static boolean isSessionAvailable(Context context) {
        SharedRepository sharedRepository = SharedRepository.getInstance(context);
        return sharedRepository.isLoggedIn();
    }

    public static void logOut(Context context) {
        try {
            SharedRepository sharedRepository = SharedRepository.getInstance(context);
            sharedRepository.invalidate();
        } catch (Exception e) {
        }
    }

    public void logIn(InstagramSessionHandler logInHandler) {
        if (sharedRepository.isLoggedIn()) {
            new AlertDialog.Builder(context)
                    .setTitle("Logged in as " + sharedRepository.getProfile().getUsername())
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedRepository.invalidate();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        } else {
            instaAuthDialog = new InstaAuthDialog(context, logInHandler, redirectOnSuccess);
            instaAuthDialog.setCancelable(true);
            instaAuthDialog.show();
        }
    }

    public boolean isSessionAvailable() {
        return sharedRepository.isLoggedIn();
    }

    public void getProfileData(InstagramSessionHandler logInHandler) {
        try {
            Profile profile = sharedRepository.getProfile();
            if (profile.getId().isEmpty() || profile.getId().equals(Utill.INSTA_DATA_NULL_STRING))
                logIn(logInHandler);
            else {
                logInHandler.onProfileDataReceived(profile);
            }
        } catch (Exception e) {
            logInHandler.onErrorOccurred(e.getMessage());
        }
    }

    public void logOut() {
        sharedRepository.invalidate();
    }

    public static class Builder {

        private Context context;
        private String clientId;
        private String redirectURL;
        private boolean redirectOnSuccess;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            Utill.INSTA_CLIENT_ID = clientId;
            return this;
        }

        public Builder setRedirectURL(String redirectURL) {
            this.redirectURL = redirectURL;
            Utill.INSTA_REDIRECT_URI = redirectURL;
            return this;
        }

        public Builder setRedirectOnSuccess(boolean redirectOnSuccess) {
            this.redirectOnSuccess = redirectOnSuccess;
            return this;
        }

        public InstaAuthAPI build() {
            return new InstaAuthAPI(context, clientId, redirectURL, redirectOnSuccess);
        }
    }
}
