package api.gliger.glg.instaoauth.api;

public interface InstagramLogInHandler {
    void onLogInSuccess(String token);
    void onLogInFailed();
}
