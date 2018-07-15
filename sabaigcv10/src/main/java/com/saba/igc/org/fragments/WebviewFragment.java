package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.saba.igc.org.R;

/**
 * Created by snaqvi on 7/13/18.
 */

public class WebviewFragment extends Fragment {

    //private FragmentActivity myContext;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout. fragment_saba_website, container, false);
        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.loadUrl("http://saba-igc.org");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Enable Zoom
        webSettings.setBuiltInZoomControls(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return view;
    }
}