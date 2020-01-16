package com.e.rpi_controller.ui.nas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.rpi_controller.R;

public class NasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NestedScrollView.OnScrollChangeListener {

    private WebView webView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nas, container, false);

        webView = view.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://djd-nas.fr1.quickconnect.to/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        swipeRefreshLayout = view.findViewById(R.id.nas_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        nestedScrollView = view.findViewById(R.id.nas_nested_scroll);
        nestedScrollView.setOnScrollChangeListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // TODO: Trovare alternativa
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        scrollY = v.getScrollY();

        if (scrollY == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onRefresh() {
        webView.reload();

        swipeRefreshLayout.setRefreshing(false);
    }
}