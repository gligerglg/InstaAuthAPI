package api.gliger.glg.instaauthapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import api.gliger.glg.instaoauth.InstaAuthDialog;
import api.gliger.glg.instaoauth.InstaAuthListener;
import api.gliger.glg.instaoauth.model.Profile;

public class MainActivity extends AppCompatActivity implements InstaAuthListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void login(View view) {
        InstaAuthDialog authDialog = new InstaAuthDialog(MainActivity.this,this);
        authDialog.setCancelable(false);
        authDialog.show();
    }

    @Override
    public void onTokenReceived(String token) {
//        Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProfileReceived(Profile profile) {
        Toast.makeText(getApplicationContext(),profile.getId(),Toast.LENGTH_LONG).show();
        System.out.println(profile.toString());
    }

    @Override
    public void onErrorOccured(String error) {

    }
}
