package api.gliger.glg.instaoauth;

import api.gliger.glg.instaoauth.model.Profile;

public interface InstaAuthListener {
    void onTokenReceived(String token);
    void onProfileReceived(Profile profile);
    void onErrorOccurred(String error);
}
