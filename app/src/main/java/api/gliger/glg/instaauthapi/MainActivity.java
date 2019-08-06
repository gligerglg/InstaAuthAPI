package api.gliger.glg.instaauthapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import api.gliger.glg.instaoauth.InstaAuthDialog;
import api.gliger.glg.instaoauth.InstaAuthListener;
import api.gliger.glg.instaoauth.api.InstaAuthAPI;
import api.gliger.glg.instaoauth.api.InstagramLogInHandler;
import api.gliger.glg.instaoauth.model.Profile;

public class MainActivity extends AppCompatActivity implements InstaAuthListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        InstaAuthDialog authDialog = new InstaAuthDialog(MainActivity.this, this);
        authDialog.setCancelable(true);
        authDialog.show();

        InstaAuthAPI authAPI  = new InstaAuthAPI();
        authAPI.logIn(new InstagramLogInHandler() {
            @Override
            public void onLogInSuccess(String token) {

            }

            @Override
            public void onLogInFailed() {

            }
        });
    }

    @Override
    public void onTokenReceived(String token) {
//        Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProfileReceived(Profile profile) {

    }

    @Override
    public void onErrorOccurred(String error) {

    }

}
