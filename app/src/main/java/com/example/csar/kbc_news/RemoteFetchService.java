package com.example.csar.kbc_news;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.csar.kbc_news.actividades.ActividadPrincipal;
import com.example.csar.kbc_news.modelos.noticias.Noticia;
import com.example.csar.kbc_news.modelos.noticias.RespuestaNoticias;
import com.example.csar.kbc_news.utils.HttpUtils;
import com.example.csar.kbc_news.utils.VariablesGlobales;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//
//        import com.androidquery.AQuery;
//        import com.androidquery.callback.AjaxCallback;
//        import com.androidquery.callback.AjaxStatus;

public class RemoteFetchService extends Service {
    private HttpUtils httpUtils = VariablesGlobales.getInstance().getHttpUtils();
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    List<Noticia> lNoticias;

    //    private AQuery aquery;
    //  private String remoteJsonUrl = "http://laaptu.files.wordpress.com/2013/07/widgetlist.key";
    public static ArrayList<ListItem> listItemList;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /*
     * Retrieve appwidget id from intent it is needed to update widget later
     * initialize our AQuery class
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("D","Dentro del Start Comand prro");
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        //aquery = new AQuery(getBaseContext());
        fetchDataFromWeb();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * method which fetches data(json) from web aquery takes params
     * remoteJsonUrl = from where data to be fetched String.class = return
     * format of data once fetched i.e. in which format the fetched data be
     * returned AjaxCallback = class to notify with data once it is fetched
     */
    private void fetchDataFromWeb() {
//        aquery.ajax(remoteJsonUrl, String.class, new AjaxCallback<String>() {
//            @Override
//            public void callback(String url, String result, AjaxStatus status) {
//                processResult(result);
//                super.callback(url, result, status);
//            }
//        });
        this.httpUtils.confiarTodosCertificados();
        Call<RespuestaNoticias> call = this.httpUtils.callNoticiasPorPais("us");
        call.enqueue(new Callback<RespuestaNoticias>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaNoticias> call, @NonNull Response<RespuestaNoticias> response) {
                // Aqui se pone la acción que se ejcutaría una vez que el servidor retorna los datos
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    lNoticias = response.body().getArticles();
                    processResult();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaNoticias> call, @NonNull Throwable t) {
                //mensaje("Ocurrió un problema al cargar las noticias, inténtelo de nuevo");
            }
        });
    }

    /**
     * Json parsing of result and populating ArrayList<ListItem> as per json
     * data retrieved from the string
     */
    private void processResult() {
        listItemList = new ArrayList<ListItem>();
        for (int i = 0; i < lNoticias.size(); i++) {
            ListItem listItem = new ListItem();
            listItem.heading = lNoticias.get(i).getTitle();
            listItem.content = lNoticias.get(i).getDescription();
            listItem.urlNoticia = lNoticias.get(i).getUrl();
            if (lNoticias.get(i).getUrlToImage() != null){
                listItem.imageUrl = lNoticias.get(i).getUrlToImage();
            }
            else{
                listItem.imageUrl = "Vacio";
            }

            listItemList.add(listItem);
        }

        populateWidget();
    }

    /**
     * Method which sends broadcast to WidgetProvider
     * so that widget is notified to do necessary action
     * and here action == WidgetProvider.DATA_FETCHED
     */
    private void populateWidget() {

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(WidgetProvider.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        sendBroadcast(widgetUpdateIntent);

        this.stopSelf();
    }
}
