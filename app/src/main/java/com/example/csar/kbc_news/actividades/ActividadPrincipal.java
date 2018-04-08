package com.example.csar.kbc_news.actividades;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.csar.kbc_news.R;

public class ActividadPrincipal extends ActividadBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        loadRecentNews();

    }

    public void loadRecentNews() {
        LinearLayout allNews = (LinearLayout) findViewById(R.id.newsContainer);
        final LinearLayout[] news = new LinearLayout[50];

        for (int i = 0; i < 50; i++) {
            LinearLayout rowNews = new LinearLayout(this);
            ImageView iv = new ImageView(this);
            EditText et = new EditText(this);
            View v = new View(this, null, R.style.Divider);

            iv.setImageResource(R.mipmap.ic_launcher_round);
            rowNews.setOrientation(LinearLayout.HORIZONTAL);

            rowNews.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            rowNews.addView(iv);
            rowNews.addView(et);
            rowNews.addView(v);

            allNews.addView(rowNews);
            news[i] = rowNews;
        }
    }
}

