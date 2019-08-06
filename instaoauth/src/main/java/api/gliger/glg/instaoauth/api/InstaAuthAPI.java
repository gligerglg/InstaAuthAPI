package api.gliger.glg.instaoauth.api;

import android.content.Context;
import android.view.View;

import api.gliger.glg.instaoauth.InstaAuthDialog;
import api.gliger.glg.instaoauth.SharedRepository;
import api.gliger.glg.instaoauth.model.Session;

public class InstaAuthAPI {
    private InstaAuthDialog instaAuthDialog;
    private Context context;
    private String clientId;
    private String redirectURL;

    private boolean showInstaAuthBanner;
    private View progressIndicator;
    private boolean redirectOnSuccess = true;

    private SharedRepository sharedRepository;


    public InstaAuthAPI(Context context, String clientId, String redirectURL, boolean showInstaAuthBanner, View progressIndicator, boolean redirectOnSuccess) {
        this.context = context;
        this.clientId = clientId;
        this.redirectURL = redirectURL;
        this.showInstaAuthBanner = showInstaAuthBanner;
        this.progressIndicator = progressIndicator;
        this.redirectOnSuccess = redirectOnSuccess;
        this.sharedRepository = SharedRepository.getInstance(context);
    }

    public void logIn(InstagramLogInHandler logInHandler){
        if(sharedRepository.isLoggedIn()){
            System.out.println("Already Logged in");
        }else {
            instaAuthDialog = new InstaAuthDialog(context, logInHandler, redirectOnSuccess);
            instaAuthDialog.setCancelable(true);
            instaAuthDialog.show();
        }
    }

    public void getProfileData(InstagramProfileHandler profileHandler){
        instaAuthDialog = new InstaAuthDialog(context,profileHandler,redirectOnSuccess);
        instaAuthDialog.setCancelable(false);
        instaAuthDialog.show();
    }

    public Session getSessionData(){
        return sharedRepository.getSession();

    }

    public void logOut(){

    }
}
