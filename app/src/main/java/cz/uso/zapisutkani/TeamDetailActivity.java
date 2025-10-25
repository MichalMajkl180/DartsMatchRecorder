package cz.uso.zapisutkani;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import cz.uso.zapisutkani.utils.AppLogger;

public class TeamDetailActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        webView = findViewById(R.id.teamWebView);
        String url = getIntent().getStringExtra("TEAM_URL");
        String teamName = getIntent().getStringExtra("TEAM_NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(teamName);
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        AppLogger.i("TeamDetailActivity", "Načítám stránku: " + url);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
