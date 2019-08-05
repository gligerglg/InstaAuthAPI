package api.gliger.glg.instaoauth;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import static api.gliger.glg.instaoauth.Utill.INSTA_BASE_URL;
import static api.gliger.glg.instaoauth.Utill.INSTA_CLIENT_ID;
import static api.gliger.glg.instaoauth.Utill.INSTA_REDIRECT_URI;

public class InstaAuthDialog extends Dialog {
    private InstaAuthListener authListener;
    private Context context;
    private WebView webView;
    private ProgressBar progressBar;
    private RelativeLayout frameLayout;

    private final String url = INSTA_BASE_URL
            + "oauth/authorize/?client_id="
            + INSTA_CLIENT_ID
            + "&redirect_uri="
            + INSTA_REDIRECT_URI
            + "&response_type=token";

    public InstaAuthDialog(Context context, InstaAuthListener authListener) {
        super(context);
        this.authListener = authListener;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.insta_auth_dialog);

        frameLayout = findViewById(R.id.frame);
        initializeWebView();
    }


    private void initializeWebView() {
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
                    authListener.onTokenReceived(accessToken);
                    authComplete = true;

                    new InstaNet(accessToken,authListener).execute();

                } else if (url.contains("?error")) {
                    dismiss();
                } else if(url.equals("https://www.instagram.com/")){
                    stopProgress();
                    initializeWebView();
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

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
}
