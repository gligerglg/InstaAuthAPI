package api.gliger.glg.instaoauth.api;

import android.content.Context;
import android.view.View;

public class InstaAuthAPI {
    private Context context;
    private String clientId;
    private String redirectURL;

    private boolean showInstaAuthBanner;
    private View progressIndicator;
    private boolean redirectOnSuccess;

    /*public InstaAuthAPI(Context context, String clientId, String redirectURL) {
        this.context = context;
        this.clientId = clientId;
        this.redirectURL = redirectURL;
    }

    public InstaAuthAPI(Context context, String clientId, String redirectURL, boolean showInstaAuthBanner, View progressIndicator, boolean redirectOnSuccess) {
        this.context = context;
        this.clientId = clientId;
        this.redirectURL = redirectURL;
        this.showInstaAuthBanner = showInstaAuthBanner;
        this.progressIndicator = progressIndicator;
        this.redirectOnSuccess = redirectOnSuccess;
    }*/

    public void logIn(InstagramLogInHandler tokenHandler){

    }

    public void getProfileData(InstagramProfileHandler profileHandler){

    }

    public void getSessionData(InstagramSessionManager instagramSessionManager){

    }

    public void logOut(InstagramLogoutHandler instagramLogoutHandler){

    }
}
