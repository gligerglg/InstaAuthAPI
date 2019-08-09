package api.gliger.glg.instaauthapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import api.gliger.glg.instaoauth.api.InstaAuthAPI;
import api.gliger.glg.instaoauth.api.InstagramSessionHandler;
import api.gliger.glg.instaoauth.model.Profile;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initComponents();
        InstaAuthAPI.getDefaultSession(this,new InstagramSessionHandler() {
            @Override
            public void onProfileDataReceived(Profile profile) {
                Picasso.get().load(profile.getPrfilePicture()).into(profileImage);
            }

            @Override
            public void onErrorOccurred(String error) {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initComponents() {
        profileImage = findViewById(R.id.profile_image);

    }

    public void logout(View view) {
        InstaAuthAPI.logOut(this);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
