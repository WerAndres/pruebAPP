package pruebaap.werinc.co.com.pruebapp.principal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import pruebaap.werinc.co.com.pruebapp.POJO.ListaCategorias;
import pruebaap.werinc.co.com.pruebapp.R;
import pruebaap.werinc.co.com.pruebapp.adaptadores.AdaptadorListView_categorias;
import pruebaap.werinc.co.com.pruebapp.configuraciones.ComprobarInternet;
import pruebaap.werinc.co.com.pruebapp.configuraciones.Configuraciones;

public class Categorias extends AppCompatActivity {


    private String TAG = "LogspruebAAP";
    public static Categorias global;
    public RelativeLayout mensajeInternet;
    public ListView listView;
    public GridView gridView;
    public String jsonString;
    public JSONObject jsonObj_object;
    public Configuraciones conf = new Configuraciones();
    public ComprobarInternet comprobarInternet = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        global = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Categorias");
        Log.d(TAG, "-------------------------------------CLASE categorias-------------------------------------");
        mensajeInternet = (RelativeLayout) findViewById(R.id.mensajeInternet);

        if (isTabletDevice(this)) {
            Log.d(TAG, "SOY Tablet =D");

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            gridView = (GridView) findViewById(R.id.gridView);

            asyn_internet();
            boolean continuar = false;

            if(hayInternet(this)) {
                Log.d(TAG+"1", "onCreate: TENGO INTERNET");
                jsonString = traerJson();
                continuar = true;
            }else{
                Log.d(TAG+"1", "onCreate: NO TENGO INTERNET");
                String sFichero = getCacheDir() + "/" + conf.getNombreJson();
                Log.d(TAG+"1", "el fichero se llama: "+sFichero);
                File fichero = new File(sFichero);
                if (fichero.exists()) {
                    Log.d(TAG+"1", "onCreate: EXITE EL FICHERO");
                    try {
                        FileInputStream fis = new FileInputStream (new File(sFichero));
                        StringBuffer fileContent = new StringBuffer("");
                        byte[] buffer = new byte[1024];
                        int n;
                        while ((n = fis.read(buffer)) != -1){
                            fileContent.append(new String(buffer, 0, n));
                        }
                        jsonString = fileContent.toString();
                        Log.d(TAG+"1", "onCreate: ESTO ES LOQ UE HABIA EN EL ARCHVO");
                        Log.d(TAG+"1", "onCreate: "+ jsonString);
                        fis.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e("Ficheros", "Error al leer fichero desde memoria interna");
                    }
                    continuar = true;
                }else{
                    Log.d(TAG+"1", "onCreate: NO EXITE EL FICHERO");
                    jsonString = "{}";
                }
            }

            if(continuar) {
                try {
                    jsonObj_object = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayList<ListaCategorias> listaCat = listaCategorias(jsonObj_object);

                Log.d(TAG, "********************** " + listaCat.size() + " categorias*************************");
                for (int i = 0; i < listaCat.size(); i++) {
                    Log.d(TAG, "----------------");
                    Log.d(TAG, i + ". Label: " + listaCat.get(i).getLabel());
                    Log.d(TAG, i + ". Term: " + listaCat.get(i).getTerm());
                    Log.d(TAG, "----------------");
                }
                Log.d(TAG, "************************categorias***************************");

                gridView.setAdapter(new AdaptadorListView_categorias(this, listaCat));

                // Register a callback to be invoked when an item in this AdapterView
                // has been clicked
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                        try {
                            ListaCategorias item = (ListaCategorias) gridView.getAdapter().getItem(position);
                            Log.d(TAG, "onItemClick: lista label: [" + item.getLabel() + "]");

                            try {
                                Intent mainIntent = new Intent().setClass(Categorias.this, Apps.class);
                                mainIntent.putExtra("jsonString", jsonString);
                                mainIntent.putExtra("idCategoria", item.getIdCategoria());
                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeCustomAnimation(Categorias.this, R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                                ActivityCompat.startActivity(Categorias.this, mainIntent, options.toBundle());
                            } catch (Exception e) {
                                System.err.println("E: " + e);
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        } else {

            Log.d(TAG, "SOY Telefono =D");

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            listView = (ListView) findViewById(R.id.listView);

            asyn_internet();
            boolean continuar = false;

            if(hayInternet(this)) {
                Log.d(TAG+"1", "onCreate: TENGO INTERNET");
                jsonString = traerJson();
                continuar = true;
            }else{
                Log.d(TAG+"1", "onCreate: NO TENGO INTERNET");
                String sFichero = getCacheDir() + "/" + conf.getNombreJson();
                Log.d(TAG+"1", "el fichero se llama: "+sFichero);
                File fichero = new File(sFichero);
                if (fichero.exists()) {
                    Log.d(TAG+"1", "onCreate: EXITE EL FICHERO");
                    try {
                        FileInputStream fis = new FileInputStream (new File(sFichero));
                        StringBuffer fileContent = new StringBuffer("");
                        byte[] buffer = new byte[1024];
                        int n;
                        while ((n = fis.read(buffer)) != -1){
                            fileContent.append(new String(buffer, 0, n));
                        }
                        jsonString = fileContent.toString();
                        Log.d(TAG+"1", "onCreate: ESTO ES LOQ UE HABIA EN EL ARCHVO");
                        Log.d(TAG+"1", "onCreate: "+ jsonString);
                        fis.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e("Ficheros", "Error al leer fichero desde memoria interna");
                    }
                    continuar = true;
                }else{
                    Log.d(TAG+"1", "onCreate: NO EXITE EL FICHERO");
                    jsonString = "{}";
                }
            }

            if(continuar) {
                try {
                    jsonObj_object = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayList<ListaCategorias> listaCat = listaCategorias(jsonObj_object);

                Log.d(TAG, "********************** " + listaCat.size() + " categorias*************************");
                for (int i = 0; i < listaCat.size(); i++) {
                    Log.d(TAG, "----------------");
                    Log.d(TAG, i + ". Label: " + listaCat.get(i).getLabel());
                    Log.d(TAG, i + ". Term: " + listaCat.get(i).getTerm());
                    Log.d(TAG, "----------------");
                }
                Log.d(TAG, "************************categorias***************************");

                listView.setAdapter(new AdaptadorListView_categorias(this, listaCat));

                // Register a callback to be invoked when an item in this AdapterView
                // has been clicked
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                        try {
                            ListaCategorias item = (ListaCategorias) listView.getAdapter().getItem(position);
                            Log.d(TAG, "onItemClick: lista label: [" + item.getLabel() + "]");

                            try {
                                Intent mainIntent = new Intent().setClass(Categorias.this, Apps.class);
                                mainIntent.putExtra("jsonString", jsonString);
                                mainIntent.putExtra("idCategoria", item.getIdCategoria());
                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeCustomAnimation(Categorias.this, R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                                ActivityCompat.startActivity(Categorias.this, mainIntent, options.toBundle());
                            } catch (Exception e) {
                                System.err.println("E: " + e);
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        }
    }

    private boolean isTabletDevice(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            try {
                boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
                boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
                return (xlarge || large);
            } catch (Exception x) {
                return false;
            }
        }
        return false;
    }

    public void asyn_internet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        comprobarInternet = new ComprobarInternet();
                        comprobarInternet.execute("categorias");
                        //Log.d(TAG, "ejecutar: Dormir");
                        Thread.sleep(conf.getTiempoComprobarInternet());
                        //Log.d(TAG, "ejecutar: Despertar");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void OcultarMostrarMensajeInternet(boolean hayInternet){
        if(hayInternet){
            if(mensajeInternet.getVisibility() != View.INVISIBLE){
               mensajeInternet.setVisibility(View.INVISIBLE);
               animar(false);
            }
        }else{
            if(mensajeInternet.getVisibility() != View.VISIBLE) {
                mensajeInternet.setVisibility(View.VISIBLE);
                animar(true);
            }
        }
    }

    private void animar(boolean mostrar){
        Animation animation = null;
        if (mostrar)
        {
            animation= AnimationUtils.loadAnimation(this, R.anim.mostrar_menu_bajo);
            mensajeInternet.startAnimation(animation);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation=AnimationUtils.loadAnimation(this, R.anim.ocultar_menu_bajo);
            mensajeInternet.startAnimation(animation);
        }
    }

    private String traerJson() {
        String Json = "";
        try {
            URL url = new URL("https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json");
            URLConnection tc = url.openConnection();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    tc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                Json = Json + line;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "JSON: [" + Json + "]");
        Log.d(TAG+"1", "traerJson: TRAJE EL JSON ARRIBA Y TENGO QUE GUARDARLO");
        guardarCache(conf.getNombreJson(), Json);

        return Json;
    }

    public ArrayList<ListaCategorias> listaCategorias(JSONObject jsonObj_object){
        ArrayList<ListaCategorias> listaSalida = null;

        boolean ExisteCategoria = false;
        int cantCategorias = 0;
        ListaCategorias elemento;

        try {

            String StringjsonObj_object_feed = jsonObj_object.getString("feed");
            //Log.d(TAG, "StringjsonObj_object_feed: ["+StringjsonObj_object_feed+"]");

            JSONObject jsonObj_object_feed = new JSONObject(StringjsonObj_object_feed);
            String StringjsonObj_object_feed_entry = jsonObj_object_feed.getString("entry");
            //Log.d(TAG, "StringjsonObj_object_feed_entry: ["+StringjsonObj_object_feed_entry+"]");

            JSONArray jsonObj_object_feed_entry = new JSONArray(StringjsonObj_object_feed_entry);

            listaSalida = new ArrayList<>();

            for(int i = 0; i < jsonObj_object_feed_entry.length(); i++){
                String StringjsonObj_object_feed_entry_category = jsonObj_object_feed_entry.getJSONObject(i).getString("category");
                //Log.d(TAG, "StringjsonObj_object_feed_entry_category  "+i+": ["+StringjsonObj_object_feed_entry_category+"]");

                JSONObject jsonObj_object_feed_entry_category = new JSONObject(StringjsonObj_object_feed_entry_category);
                String StringjsonObj_object_feed_entry_category_attributes = jsonObj_object_feed_entry_category.getString("attributes");
                //Log.d(TAG, "StringjsonObj_object_feed_entry_category_attributes  "+i+": ["+StringjsonObj_object_feed_entry_category_attributes+"]");

                JSONObject jsonObj_object_feed_entry_category_attributes = new JSONObject(StringjsonObj_object_feed_entry_category_attributes);

                String StringjsonObj_object_feed_entry_category_attributes_label = jsonObj_object_feed_entry_category_attributes.getString("label");
                //Log.d(TAG, "StringjsonObj_object_feed_entry_category_attributes_label  "+i+": ["+StringjsonObj_object_feed_entry_category_attributes_label+"]");

                String StringjsonObj_object_feed_entry_category_attributes_term = jsonObj_object_feed_entry_category_attributes.getString("term");
                //Log.d(TAG, "StringjsonObj_object_feed_entry_category_attributes_term  "+i+": ["+StringjsonObj_object_feed_entry_category_attributes_term+"]");

                int IntjsonObj_object_feed_entry_category_attributes_imid = Integer.parseInt(jsonObj_object_feed_entry_category_attributes.getString("im:id"));
                //Log.d(TAG, "IntjsonObj_object_feed_entry_category_attributes_imid  "+i+": ["+IntjsonObj_object_feed_entry_category_attributes_imid+"]");

                ExisteCategoria = false;
                for(int j = 0; j < listaSalida.size(); j++){
                    if(listaSalida.get(j).getIdCategoria() == IntjsonObj_object_feed_entry_category_attributes_imid){
                        ExisteCategoria = true;
                        //Log.d(TAG, "Existe la idCategoria");
                    }
                }
                if(!ExisteCategoria) {
                    elemento = new ListaCategorias();
                    elemento.setLabel(StringjsonObj_object_feed_entry_category_attributes_label);
                    elemento.setTerm(StringjsonObj_object_feed_entry_category_attributes_term);
                    elemento.setIdCategoria(IntjsonObj_object_feed_entry_category_attributes_imid);
                    listaSalida.add(elemento);
                    cantCategorias = cantCategorias + 1;
                    Log.d(TAG, "cantCategorias: "+cantCategorias);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaSalida;
    }

    public void guardarCache(String nombre, String info){
        Log.d(TAG+"1", "guardarCache: LISTO A GUARDAR EL JSON");
        ///guardar info
        String filename = nombre;
        String string = info;
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(getCacheDir() + "/" + filename);
            Log.d(TAG+"1", "la carpeta se supone que se llama: "+getCacheDir());
            Log.d(TAG+"1", "el fichero se llama: "+filename);
            outputStream.write(string.getBytes());
            outputStream.close();
            Log.d(TAG+"1", "guardarCache: CREO QUE GUARDE EXITOSAMENTE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /////
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
