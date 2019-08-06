package api.gliger.glg.instaoauth.api;

import api.gliger.glg.instaoauth.model.Session;

public interface InstagramSessionManager {
    void onSessionReceived(Session session);
    void onSessionFailed();
}
