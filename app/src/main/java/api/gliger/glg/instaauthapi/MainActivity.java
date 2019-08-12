package api.gliger.glg.instaauthapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import api.gliger.glg.instaoauth.api.InstaAuthAPI;
import api.gliger.glg.instaoauth.api.InstagramSessionHandler;
import api.gliger.glg.instaoauth.model.Profile;

public class MainActivity extends AppCompatActivity {

    private InstaAuthAPI authAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authAPI = new InstaAuthAPI.Builder(this)
                .setClientId("ENTER_YOUR_CLIENT_ID_HERE")
                .setRedirectURL("ENTER_YOUR_REDIRECT_URL_HERE")
                .setRedirectOnSuccess(true)
                .build();

        if(authAPI.isSessionAvailable()) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            finish();
        }
    }

    public void login(View view){
        authAPI.logIn(new InstagramSessionHandler() {
            @Override
            public void onProfileDataReceived(Profile profile) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                finish();
            }

            @Override
            public void onErrorOccurred(String error) {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
            }
        });
    }

}
