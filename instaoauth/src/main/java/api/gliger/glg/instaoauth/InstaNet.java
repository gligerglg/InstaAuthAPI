package api.gliger.glg.instaoauth;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import api.gliger.glg.instaoauth.api.InstagramSessionHandler;
import api.gliger.glg.instaoauth.model.Count;
import api.gliger.glg.instaoauth.model.Profile;

public class InstaNet extends AsyncTask<Void, String, Profile> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private final String userInfo = "https://api.instagram.com/v1/users/self/?access_token=";
    private InstagramSessionHandler instagramSessionHandler;
    private SharedRepository sharedRepository;
    private InstaNetHandler instaNetHandler;

    private String token;

    public InstaNet(String token, InstagramSessionHandler profileHandler, SharedRepository sharedRepository, InstaNetHandler instaNetHandler) {
        this.token = token;
        this.instagramSessionHandler = profileHandler;
        this.sharedRepository = sharedRepository;
        this.instaNetHandler = instaNetHandler;
    }


    @Override
    protected Profile doInBackground(Void... voids) {
        String inputLine, jsonString;
        Profile result = new Profile();

        try {
            URL url = new URL(userInfo + token);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();
            jsonString = stringBuilder.toString();
            JSONObject object = new JSONObject(jsonString);

            Profile profile = new Profile();
            Count counts = new Count();
            profile.setId(object.getJSONObject("data").getString("id"));
            profile.setUsername(object.getJSONObject("data").getString("username"));
            profile.setProfilePicture(object.getJSONObject("data").getString("profile_picture"));
            profile.setFullName(object.getJSONObject("data").getString("full_name"));
            profile.setBio(object.getJSONObject("data").getString("bio"));
            profile.setWebsite(object.getJSONObject("data").getString("website"));
            profile.setBusinessProfile((object.getJSONObject("data").getBoolean("is_business")));
            counts.setMedia(object.getJSONObject("data").getJSONObject("counts").getInt("media"));
            counts.setFollows(object.getJSONObject("data").getJSONObject("counts").getInt("follows"));
            counts.setFollowedBy(object.getJSONObject("data").getJSONObject("counts").getInt("followed_by"));
            profile.setCounts(counts);

            result = profile;


        } catch (Exception w) {
            instagramSessionHandler.onErrorOccurred(w.getMessage());
            instaNetHandler.onErrorOccurred();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Profile response) {
        //Profile response
        instagramSessionHandler.onProfileDataReceived(response);
        instaNetHandler.onRequestSuccess();
        sharedRepository.saveProfile(response);
        super.onPostExecute(response);
    }
}
