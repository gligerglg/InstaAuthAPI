package api.gliger.glg.instaoauth;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import api.gliger.glg.instaoauth.api.InstagramSessionHandler;

import static api.gliger.glg.instaoauth.Utill.INSTA_BASE_URL;
import static api.gliger.glg.instaoauth.Utill.INSTA_CLIENT_ID;
import static api.gliger.glg.instaoauth.Utill.INSTA_REDIRECT_URI;

public class InstaAuthDialog extends Dialog {
    private final String url = INSTA_BASE_URL
            + "oauth/authorize/?client_id="
            + INSTA_CLIENT_ID
            + "&redirect_uri="
            + INSTA_REDIRECT_URI
            + "&response_type=token";
    private Context context;
    private WebView webView;
    private ProgressBar progressBar;
    private RelativeLayout frameLayout, errorLayout;
    private TextView errorText;
    private Button btnClose;
    private SharedRepository sharedRepository;
    private boolean redirectOnSuccess;
    private InstagramSessionHandler instagramSessionHandler;

    public InstaAuthDialog(Context context, InstagramSessionHandler logInHandler, boolean redirectOnSuccess) {
        super(context);
        this.context = context;
        this.instagramSessionHandler = logInHandler;
        this.redirectOnSuccess = redirectOnSuccess;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.insta_auth_dialog);

        initComponents();
        initializeWebView();
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

    private void removeSessionCookies() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }


    private boolean initializeWebView() {
        startProgress();
        webView = findViewById(R.id.insta_webView);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);

        /*if (!sharedRepository.isLoggedIn())
            removeSessionCookies();*/

        webView.setWebViewClient(new WebViewClient() {

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
                    accessToken = accessToken.substring(accessToken.lastIndexOf("=") + 1);
                    authComplete = true;

                    if (instagramSessionHandler != null && accessToken != null) {

                        new InstaNet(accessToken, instagramSessionHandler, sharedRepository, new InstaNetHandler() {
                            @Override
                            public void onRequestSuccess() {
                                if (!redirectOnSuccess)
                                    dismiss();
                            }

                            @Override
                            public void onErrorOccurred() {
                                setError(Utill.ERROR_OOPS);
                            }
                        }).execute();
                    }

                } else if (url.contains("?error")) {
                    setError(Utill.ERROR_OOPS);
                } else if (url.equals("https://www.instagram.com/")) {
                    stopProgress();
                    initializeWebView();
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                setError(Utill.ERROR_CONNECTION);
                if (instagramSessionHandler != null)
                    instagramSessionHandler.onErrorOccurred(Utill.ERROR_CONNECTION);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                setError(Utill.ERROR_OOPS);
                if (instagramSessionHandler != null)
                    instagramSessionHandler.onErrorOccurred(Utill.ERROR_CREDENTIAL);
            }
        });

        return true;

    }

    private void startProgress() {
        progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(0xc7c7c7c7, android.graphics.PorterDuff.Mode.MULTIPLY);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        frameLayout.addView(progressBar, params);
    }

    private void stopProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            frameLayout.removeView(progressBar);
        }
    }

    private void setError(String error) {
        errorLayout.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        errorText.setText(error);
    }

}
