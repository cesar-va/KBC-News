package com.example.csar.kbc_news;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.csar.kbc_news.actividades.ActividadPrincipal;
import com.squareup.picasso.Picasso;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 * here it now takes RemoteFetchService ArrayList<ListItem> for data
 * which is a static ArrayList
 * and this example won't work if there are multiple widgets and
 * they update at same time i.e they modify RemoteFetchService ArrayList at same
 * time.
 * For that use Database or other techniquest
 */
public class ListProvider implements RemoteViewsFactory {
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private int appWidgetId;
    private int minWidth;
    private int minResizeWidth;
    private int minHeight;
    private int minResizeHeight;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(
                context).getAppWidgetInfo(appWidgetId);

        minWidth = providerInfo.minWidth;
        minResizeWidth = providerInfo.minResizeWidth;
        minHeight = providerInfo.minHeight;
        minResizeHeight = providerInfo.minResizeHeight;

        Log.i("Tamano", String.valueOf(minResizeHeight));

        populateListItem();
    }

    private void populateListItem() {
        if(RemoteFetchService.listItemList !=null )
            listItemList = (ArrayList<ListItem>) RemoteFetchService.listItemList
                    .clone();
        else
            listItemList = new ArrayList<ListItem>();

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_row);
        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.heading, listItem.heading);
        //remoteView.setTextViewText(R.id.content, listItem.content);
        Bitmap b = null;
        if(!listItem.imageUrl.equals("Vacio")){
            try {

                b = Picasso.with(context).load(listItem.imageUrl).resize(minWidth,minHeight).get();
//                Picasso.with(context).load(listItem.imageUrl)
//                        .resize(getWindowManager().getDefaultDisplay().getWidth(),
//                                (int) (84 / getWindowManager().getDefaultDisplay().getWidth())).into(im);
            } catch (IOException e) {
                e.printStackTrace();
            }
            remoteView.setImageViewBitmap(R.id.imageView, b);
            remoteView.setImageViewResource(R.id.widgetImgLauncher, R.drawable.logo);
        }

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(WidgetProvider.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(WidgetProvider.EXTRA_STRING,
                listItem.urlNoticia);
        fillInIntent.putExtras(bundle);
        remoteView.setOnClickFillInIntent(R.id.heading, fillInIntent);


        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}

