package com.example.csar.kbc_news;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csar.kbc_news.actividades.ActividadBase;
import com.example.csar.kbc_news.actividades.ActividadPrincipal;
import com.example.csar.kbc_news.actividades.ActividadWeb;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class WidgetProvider extends AppWidgetProvider{
    public static final String DATA_FETCHED = "com.example.csar.kbc_news.DATA_FETCHED";

    public static final String ACTION_TOAST = "com.example.csar.kbc_news.ACTION_TOAST";
    public static final String EXTRA_STRING = "com.example.csar.kbc_news.EXTRA_STRING";
    /*
     * this method is called every 30 mins as specified on widgetinfo.xml this
     * method is also called on every phone reboot from this method nothing is
     * updated right now but instead RetmoteFetchService class is called this
     * service will fetch data,and send broadcast to WidgetProvider this
     * broadcast will be received by WidgetProvider onReceive which in turn
     * updates the widget
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.d("Widget", "Hello WidgetProvider onUpdate");


        for (int i = 0; i < N; i++) {

            RemoteViews mView = initViews(context, appWidgetManager, appWidgetIds[i]);

            Intent serviceIntent = new Intent(context, RemoteFetchService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetIds[i]);
//
//            // Adding collection list item handler
//            final Intent onItemClick = new Intent(context, WidgetProvider.class);
//            onItemClick.setAction(ACTION_TOAST);
//            onItemClick.setData(Uri.parse(onItemClick
//                    .toUri(Intent.URI_INTENT_SCHEME)));
//            finalonClickPending PendingIntent Intent = PendingIntent
//                    .getBroadcast(context, 0, onItemClick,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//            mView.setPendingIntentTemplate(R.id.listViewWidget,
//                    onClickPendingIntent);

            mView.setImageViewResource(R.id.widgetImgLauncher, R.drawable.logo);

            appWidgetManager.updateAppWidget(appWidgetIds[i], mView);

            context.startService(serviceIntent);

            //Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
            //mView.setImageViewBitmap(R.id.widgetImgLauncher,bitmap);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        // which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_provider);

        // RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        // passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // setting a unique Uri to the intent
        // don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                svcIntent);
        // setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);

        SimpleDateFormat formato =
                new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es_ES"));
        String fecha = formato.format(new Date());

        remoteViews.setTextViewText(R.id.txvWidgetTitle, fecha);
        return remoteViews;
    }

    /*
     * It receives the broadcast as per the action set on intent filters on
     * Manifest.xml once data is fetched from RemotePostService,it sends
     * broadcast and WidgetProvider notifies to change the data the data change
     * right now happens on ListProvider as it takes RemoteFetchService
     * listItemList as data
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Widget", "Hello WidgetProvider onReceive");

        if (intent.getAction().equals(DATA_FETCHED)) {
            Log.d("Widget", "Data fetched in Widget Provider");
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
//if item on list was clicked
        if (intent.getAction().equals(ACTION_TOAST)) {
            String item = intent.getExtras().getString(EXTRA_STRING);
            Toast.makeText(context, item, Toast.LENGTH_LONG).show();
        }

        super.onReceive(context, intent);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.widget_provider);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.listViewWidget, intent);

        return mView;
    }

}