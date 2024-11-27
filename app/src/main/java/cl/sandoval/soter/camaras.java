package cl.sandoval.soter;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class camaras extends AppCompatActivity {

    private WebView webView1;
    private WebView webView2;
    private WebView webView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camaras);

        // Configuración del primer WebView
        webView1 = findViewById(R.id.webView1);
        webView1.setWebViewClient(new WebViewClient()); // Para abrir en la misma WebView
        WebSettings webSettings1 = webView1.getSettings();
        webSettings1.setJavaScriptEnabled(true); // Habilitar JavaScript si es necesario
        webSettings1.setLoadWithOverviewMode(true);  // Ajustar la vista al tamaño disponible
        webSettings1.setUseWideViewPort(true); // Permite el ajuste del zoom
        webView1.loadUrl("http://50.199.215.185/mjpg/video.mjpg?camera=1&timestamp=1732687887771");

        // Configuración del segundo WebView
        webView2 = findViewById(R.id.webView2);
        webView2.setWebViewClient(new WebViewClient());
        WebSettings webSettings2 = webView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);
        webSettings2.setLoadWithOverviewMode(true);
        webSettings2.setUseWideViewPort(true);
        webView2.loadUrl("http://50.248.1.46:8000/mjpg/video.mjpg?timestamp=1732688643478");

        // Configuración del tercer WebView
        webView3 = findViewById(R.id.webView3);
        webView3.setWebViewClient(new WebViewClient());
        WebSettings webSettings3 = webView3.getSettings();
        webSettings3.setJavaScriptEnabled(true);
        webSettings3.setLoadWithOverviewMode(true);
        webSettings3.setUseWideViewPort(true);
        webView3.loadUrl("http://200.54.42.126:8090/mjpg/video.mjpg?camera=1&timestamp=1732688714056");
    }
}