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

import static api.gliger.glg.instaoauth.Utill.INSTA_BASE_URL;
import static api.gliger.glg.instaoauth.Utill.INSTA_CLIENT_ID;
import static api.gliger.glg.instaoauth.Utill.INSTA_REDIRECT_URI;

public class InstaAuthDialog extends Dialog {
    private InstaAuthListener authListener;
    private Context context;
    private WebView webView;
    private ProgressBar progressBar;
    private RelativeLayout frameLayout, errorLayout;
    private TextView errorText;
    private Button btnClose;

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

        initComponents();
        initializeWebView();
    }

    private void initComponents() {
        frameLayout = findViewById(R.id.frame);
        errorText = findViewById(R.id.error_message);
        errorLayout = findViewById(R.id.error_layout);
        btnClose = findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
                    setError("Oops!\nSomething went wrong");
                } else if(url.equals("https://www.instagram.com/")){
                    stopProgress();
                    initializeWebView();
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
