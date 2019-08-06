package api.gliger.glg.instaoauth;

import android.content.Context;
import android.content.SharedPreferences;

import api.gliger.glg.instaoauth.model.Count;
import api.gliger.glg.instaoauth.model.Profile;
import api.gliger.glg.instaoauth.model.Session;

public class SharedRepository {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context mContext;
    private static SharedRepository instance;

    public static SharedRepository getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            sharedPreferences = mContext.getSharedPreferences(Utill.INSTA_SHARED_DB, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        return instance;
    }

    public void saveToken(String token) {
        editor.putString(Utill.INSTA_TOKEN, Utill.INSTA_DATA_NULL_STRING);
        editor.commit();
    }

    public String getToken() {
        return sharedPreferences.getString(Utill.INSTA_TOKEN, Utill.INSTA_DATA_NULL_STRING);
    }

    public void saveProfile(Profile profile) {
        editor.putString(Utill.INSTA_PROFILE_ID, profile.getId());
        editor.putString(Utill.INSTA_PROFILE_USERNAME, profile.getUsername());
        editor.putString(Utill.INSTA_PROFILE_PICTURE, profile.getPrfilePicture());
        editor.putString(Utill.INSTA_PROFILE_FULLNAME, profile.getFullName());
        editor.putString(Utill.INSTA_PROFILE_BIO, profile.getBio());
        editor.putString(Utill.INSTA_PROFILE_WEBSITE, profile.getWebsite());
        editor.putBoolean(Utill.INSTA_PROFILE_BUSINESS, profile.isBusinessProfile());
        editor.putInt(Utill.INSTA_PROFILE_MEDIA, profile.getCounts().getMedia());
        editor.putInt(Utill.INSTA_PROFILE_FOLLOWS, profile.getCounts().getFollows());
        editor.putInt(Utill.INSTA_PROFILE_FOLLOWEDBY, profile.getCounts().getFollowedBy());

        editor.commit();
    }

    public Profile getProfile() {
        Profile profile = new Profile();
        Count count = new Count();

        count.setMedia(sharedPreferences.getInt(Utill.INSTA_PROFILE_MEDIA, Utill.INSTA_DATA_NULL_INT));
        count.setFollows(sharedPreferences.getInt(Utill.INSTA_PROFILE_FOLLOWS, Utill.INSTA_DATA_NULL_INT));
        count.setFollowedBy(sharedPreferences.getInt(Utill.INSTA_PROFILE_FOLLOWEDBY, Utill.INSTA_DATA_NULL_INT));
        profile.setId(sharedPreferences.getString(Utill.INSTA_PROFILE_ID, Utill.INSTA_DATA_NULL_STRING));
        profile.setUsername(sharedPreferences.getString(Utill.INSTA_PROFILE_USERNAME, Utill.INSTA_PROFILE_USERNAME));
        profile.setProfilePicture(sharedPreferences.getString(Utill.INSTA_PROFILE_PICTURE, Utill.INSTA_DATA_NULL_STRING));
        profile.setFullName(sharedPreferences.getString(Utill.INSTA_PROFILE_FULLNAME, Utill.INSTA_DATA_NULL_STRING));
        profile.setBio(sharedPreferences.getString(Utill.INSTA_PROFILE_BIO, Utill.INSTA_DATA_NULL_STRING));
        profile.setWebsite(sharedPreferences.getString(Utill.INSTA_PROFILE_WEBSITE, Utill.INSTA_DATA_NULL_STRING));
        profile.setBusinessProfile(sharedPreferences.getBoolean(Utill.INSTA_PROFILE_BUSINESS, Utill.INSTA_DATA_NULL_BOOLEAN));
        profile.setCounts(count);

        return profile;
    }

    public void saveSession(Session session) {
        saveToken(session.getToken());
        saveProfile(session.getProfile());
    }

    public Session getSession() {
        Session session = new Session();
        session.setProfile(getProfile());
        session.setToken(getToken());
        return session;
    }

    public void invalidate(){
        try {
            editor.remove(Utill.INSTA_PROFILE_MEDIA);
            editor.remove(Utill.INSTA_PROFILE_FOLLOWS);
            editor.remove(Utill.INSTA_PROFILE_FOLLOWEDBY);
            editor.remove(Utill.INSTA_PROFILE_ID);
            editor.remove(Utill.INSTA_PROFILE_USERNAME);
            editor.remove(Utill.INSTA_PROFILE_PICTURE);
            editor.remove(Utill.INSTA_PROFILE_FULLNAME);
            editor.remove(Utill.INSTA_PROFILE_BIO);
            editor.remove(Utill.INSTA_PROFILE_WEBSITE);
            editor.remove(Utill.INSTA_PROFILE_BUSINESS);
        }catch (Exception e){}
    }

    public boolean isLoggedIn(){
        return !getToken().isEmpty();
    }

}
