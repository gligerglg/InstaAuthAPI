package api.gliger.glg.instaauthapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import api.gliger.glg.instaoauth.InstaAuthDialog;
import api.gliger.glg.instaoauth.api.InstaAuthAPI;
import api.gliger.glg.instaoauth.api.InstagramLogInHandler;
import api.gliger.glg.instaoauth.api.InstagramLogoutHandler;
import api.gliger.glg.instaoauth.api.InstagramProfileHandler;
import api.gliger.glg.instaoauth.api.InstagramSessionManager;
import api.gliger.glg.instaoauth.model.Profile;
import api.gliger.glg.instaoauth.model.Session;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {

        InstaAuthAPI authAPI  = new InstaAuthAPI(getApplicationContext(),"","",true,null,true);

        //Login
        authAPI.logIn(new InstagramLogInHandler() {
            @Override
            public void onLogInSuccess(String token) {

            }

            @Override
            public void onLogInFailed() {

            }
        });

        //getProfile Data
        authAPI.getProfileData(new InstagramProfileHandler() {
            @Override
            public void onProfileDataReceived(Profile profile) {

            }

            @Override
            public void onErrorOccurred(String error) {

            }


        });

        //getSession
        authAPI.getSessionData();

        //logout
        authAPI.logOut();
    }

}
