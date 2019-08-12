package api.gliger.glg.instaauthapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import api.gliger.glg.instaoauth.api.InstaAuthAPI;
import api.gliger.glg.instaoauth.api.InstagramSessionHandler;
import api.gliger.glg.instaoauth.model.Profile;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView txtUsername, txtFullname, txtBio, txtWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initComponents();
        InstaAuthAPI.getDefaultSession(this,new InstagramSessionHandler() {
            @Override
            public void onProfileDataReceived(Profile profile) {
                Picasso.get().load(profile.getPrfilePicture()).into(profileImage);
                txtBio.setText(profile.getBio());
                txtFullname.setText(profile.getFullName());
                txtUsername.setText(profile.getUsername());
                txtWeb.setText(profile.getWebsite());
            }

            @Override
            public void onErrorOccurred(String error) {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initComponents() {
        profileImage = findViewById(R.id.profile_image);
        txtBio = findViewById(R.id.txt_bio);
        txtFullname = findViewById(R.id.txt_fullname);
        txtUsername = findViewById(R.id.txt_username);
        txtWeb = findViewById(R.id.txt_web);
    }

    public void logout(View view) {
        InstaAuthAPI.logOut(this);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
