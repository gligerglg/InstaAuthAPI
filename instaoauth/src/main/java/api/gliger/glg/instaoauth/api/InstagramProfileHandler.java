package api.gliger.glg.instaoauth.api;

import api.gliger.glg.instaoauth.model.Profile;

public interface InstagramProfileHandler {
    void onProfileDataReceived(Profile profile);
    void onErrorOccurred(String error);
}
