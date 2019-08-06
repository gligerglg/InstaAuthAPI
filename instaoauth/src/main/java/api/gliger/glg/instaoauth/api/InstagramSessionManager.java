package api.gliger.glg.instaoauth.api;

import api.gliger.glg.instaoauth.model.Profile;

public interface InstagramSessionManager {
    void onSessionReceived(Profile profile);
    void onSessionFailed();
}
