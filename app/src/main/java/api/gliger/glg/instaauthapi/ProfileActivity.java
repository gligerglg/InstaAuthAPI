package api.gliger.glg.instaauthapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import api.gliger.glg.instaoauth.api.InstaAuthAPI;
import api.gliger.glg.instaoauth.api.InstagramSessionHandler;
import api.gliger.glg.instaoauth.model.Profile;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InstaAuthAPI.getDefaultClient(this,new InstagramSessionHandler() {
            @Override
            public void onProfileDataReceived(Profile profile) {
                Toast.makeText(getApplicationContext(),profile.getFullName(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorOccurred(String error) {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
            }
        });
    }
}
