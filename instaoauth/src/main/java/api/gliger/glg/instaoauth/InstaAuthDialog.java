package api.gliger.glg.instaoauth;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import api.gliger.glg.instaoauth.api.InstagramLogInHandler;
import api.gliger.glg.instaoauth.api.InstagramLogoutHandler;
import api.gliger.glg.instaoauth.api.InstagramProfileHandler;
import api.gliger.glg.instaoauth.api.InstagramSessionManager;

import static api.gliger.glg.instaoauth.Utill.INSTA_BASE_URL;
import static api.gliger.glg.instaoauth.Utill.INSTA_CLIENT_ID;
import static api.gliger.glg.instaoauth.Utill.INSTA_REDIRECT_URI;

public class InstaAuthDialog extends Dialog{
    private Context context;
    private WebView webView;
    private ProgressBar progressBar;
    private RelativeLayout frameLayout, errorLayout;
    private TextView errorText;
    private Button btnClose;
    private SharedRepository sharedRepository;
    private boolean redirectOnSuccess;

    private InstagramLogInHandler instagramLogInHandler;
    private InstagramProfileHandler instagramProfileHandler;

    private final String url = INSTA_BASE_URL
            + "oauth/authorize/?client_id="
            + INSTA_CLIENT_ID
            + "&redirect_uri="
            + INSTA_REDIRECT_URI
            + "&response_type=token";

    public static final int MODE_PROFILE=0;
    public static final int MODE_LOGIN=1;
    private int SELECTED_MODE=-1;

    public InstaAuthDialog(Context context, InstagramLogInHandler logInHandler,boolean redirectOnSuccess) {
        super(context);
        this.context = context;
        this.instagramLogInHandler = logInHandler;
        this.SELECTED_MODE = MODE_LOGIN;
        this.redirectOnSuccess = redirectOnSuccess;
    }


    public InstaAuthDialog(Context context, InstagramProfileHandler profileHandler,boolean redirectOnSuccess) {
        super(context);
        this.context = context;
        this.instagramProfileHandler = profileHandler;
        this.SELECTED_MODE = MODE_PROFILE;
        this.redirectOnSuccess = redirectOnSuccess;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.insta_auth_dialog);

        initComponents();
        route(SELECTED_MODE);
    }

    private void route(int selected_mode) {
        switch (selected_mode){
            case MODE_LOGIN:
                initializeWebView(MODE_LOGIN);

                break;

            case MODE_PROFILE:
                initializeWebView(MODE_PROFILE);
                break;
        }
    }

    private void initComponents() {
        frameLayout = findViewById(R.id.frame);
        errorText = findViewById(R.id.error_message);
        errorLayout = findViewById(R.id.error_layout);
        btnClose = findViewById(R.id.btn_close);
        sharedRepository = SharedRepository.getInstance(context);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private boolean initializeWebView(final int selected_mode) {
        startProgress();
        webView = findViewById(R.id.insta_webView);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);

        webView.setWebViewClient(new WebViewClient(){

            boolean authComplete = false;
            String accessToken;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                stopProgress();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("#access_token=") && !authComplete) {
                    stopProgress();
                    Uri uri = Uri.parse(url);
                    accessToken = uri.getEncodedFragment();
                    accessToken = accessToken.substring(accessToken.lastIndexOf("=")+1);
                    authComplete = true;

                    if(selected_mode==MODE_LOGIN && instagramLogInHandler!=null && accessToken!=null){
                        instagramLogInHandler.onLogInSuccess(accessToken);
                        sharedRepository.saveToken(accessToken);

                        if(!redirectOnSuccess)
                            dismiss();
                        return true;
                    }else if(selected_mode==MODE_PROFILE && instagramProfileHandler!=null && accessToken!=null) {

                        new InstaNet(accessToken, instagramProfileHandler, sharedRepository, new InstaNetHandler() {
                            @Override
                            public void onRequestSuccess() {
                                if (!redirectOnSuccess)
                                    dismiss();
                            }

                            @Override
                            public void onErrorOccurred() {
                                setError("Oops!\nSomething went wrong");
                            }
                        }).execute();
                    }

                } else if (url.contains("?error")) {
                    setError("Oops!\nSomething went wrong");
                } else if(url.equals("https://www.instagram.com/")){
                    stopProgress();
                    initializeWebView(selected_mode);
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                setError("No data network detected.\nPlease turn on internet services.");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                setError("Oops!\nSomething went wrong");
            }
        });

        return true;

    }

    private void startProgress(){
        progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        frameLayout.addView(progressBar,params);
    }

    private void stopProgress(){
        if(progressBar!=null){
            progressBar.setVisibility(View.GONE);
            frameLayout.removeView(progressBar);
        }
    }

    private void setError(String error){
        errorLayout.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        errorText.setText(error);
        setCancelable(false);
    }

    private void dismissError(){
        errorLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

}
