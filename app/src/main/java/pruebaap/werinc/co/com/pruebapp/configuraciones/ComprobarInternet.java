package pruebaap.werinc.co.com.pruebapp.configuraciones;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import pruebaap.werinc.co.com.pruebapp.principal.Apps;
import pruebaap.werinc.co.com.pruebapp.principal.Categorias;
import pruebaap.werinc.co.com.pruebapp.principal.Detalles;

/**
 * Created by werinc on 31/08/16.
 */

public class ComprobarInternet extends AsyncTask<String, Void, Boolean> {
    String TAG = "COMPROBAR_INTERNET";
    Categorias globalCategorias;
    Apps globalApps;
    Detalles globalDetalles;
    String claseLLamada;

    @Override
    protected Boolean doInBackground(String... params) {
        claseLLamada = params[0];
        return ejecutar();

    }

    private Boolean ejecutar(){
        boolean hay=false;
        if(claseLLamada.equals("categorias")){
            globalCategorias = Categorias.global;
            hay = hayInternet(globalCategorias.getBaseContext());
        }
        if(claseLLamada.equals("apps")){
            globalApps = Apps.global;
            hay = hayInternet(globalApps.getBaseContext());
        }
        if(claseLLamada.equals("detalles")){
            globalDetalles = Detalles.global;
            hay = hayInternet(globalDetalles.getBaseContext());
        }
        return hay;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //Log.d(TAG, "onPostExecute: "+result);
        if(claseLLamada.equals("categorias")){
            globalCategorias.OcultarMostrarMensajeInternet(result);
        }
        if(claseLLamada.equals("apps")){
            globalApps.OcultarMostrarMensajeInternet(result);
        }
        if(claseLLamada.equals("detalles")){
            globalDetalles.OcultarMostrarMensajeInternet(result);
        }
    }

   @Override
    protected void onCancelled() {
    }

    private boolean hayInternet(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < redes.length; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
                //Log.d(TAG, "Hay internet");
            }
        }
        if(!connected){
            //Log.d(TAG, "NO Hay internet");
        }
        return connected;
    }
}