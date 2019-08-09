package api.gliger.glg.instaoauth.api;

import api.gliger.glg.instaoauth.model.Profile;

public interface InstagramSessionHandler {
    void onProfileDataReceived(Profile profile);
    void onErrorOccurred(String error);
}
