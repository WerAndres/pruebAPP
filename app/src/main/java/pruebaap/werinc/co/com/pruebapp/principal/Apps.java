package pruebaap.werinc.co.com.pruebapp.principal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pruebaap.werinc.co.com.pruebapp.POJO.ListaApps;
import pruebaap.werinc.co.com.pruebapp.R;
import pruebaap.werinc.co.com.pruebapp.adaptadores.AdaptadorListView_apps;
import pruebaap.werinc.co.com.pruebapp.configuraciones.ComprobarInternet;
import pruebaap.werinc.co.com.pruebapp.configuraciones.Configuraciones;

public class Apps extends AppCompatActivity {

        private String TAG = "LogspruebAAP";
        public static Apps global;
        public RelativeLayout mensajeInternet_apps;
        public ListView listView;
        public GridView gridView;
        public String jsonString;
        public int idCategoria;
        public TextView textViewTitulo;
        public JSONObject jsonObj_object;
        public Configuraciones conf = new Configuraciones();
        public ComprobarInternet comprobarInternet = null;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            try {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_apps);
                global = this;
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);


                Log.d(TAG, "-------------------------------------CLASE apps-------------------------------------");
                mensajeInternet_apps = (RelativeLayout) findViewById(R.id.mensajeInternet_apps);
                textViewTitulo = (TextView) findViewById(R.id.textView_tituloCategoria);

                if (isTabletDevice(this)) {
                    Log.d(TAG, "SOY Tablet =D");

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    gridView = (GridView) findViewById(R.id.gridView_apps);
                    jsonString = getIntent().getExtras().getString("jsonString");
                    idCategoria = getIntent().getExtras().getInt("idCategoria");
                    asyn_internet();

                    try {
                        jsonObj_object = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ArrayList<ListaApps> listaApp = listaApps(jsonObj_object, idCategoria);
                    textViewTitulo.setText(listaApp.get(0).getCategoria());
                    actionBar.setTitle("Apps - "+ listaApp.get(0).getCategoria());
                    Log.d(TAG, "********************** " + listaApp.size() + " categorias*************************");
                    for (int i = 0; i < listaApp.size(); i++) {
                        Log.d(TAG, "----------------");
                        Log.d(TAG, i + ". nombre: " + listaApp.get(i).getNombre());
                        Log.d(TAG, i + ". precio: " + listaApp.get(i).getPrecio());
                        Log.d(TAG, i + ". artista: " + listaApp.get(i).getArtista());
                        Log.d(TAG, i + ". Categoria: " + listaApp.get(i).getCategoria());
                        Log.d(TAG, i + ". URLImage: " + listaApp.get(i).getURLImage());
                        Log.d(TAG, i + ". idApp: " + listaApp.get(i).getIdApp());

                        Log.d(TAG, "----------------");
                    }
                    Log.d(TAG, "************************categorias***************************");

                    gridView.setAdapter(new AdaptadorListView_apps(this, listaApp));

                    // Register a callback to be invoked when an item in this AdapterView
                    // has been clicked
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                            try {
                                ListaApps item = (ListaApps) gridView.getAdapter().getItem(position);
                                Log.d(TAG, "onItemClick: lista label: [" + item.getNombre() + "]");

                                try {
                                    Intent mainIntent = new Intent().setClass(Apps.this, Detalles.class);
                                    mainIntent.putExtra("jsonString", jsonString);
                                    mainIntent.putExtra("idApp", item.getIdApp());
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeCustomAnimation(Apps.this, R.anim.desvanecer_in, R.anim.desvanecer_out);
                                    ActivityCompat.startActivity(Apps.this, mainIntent, options.toBundle());
                                } catch (Exception e) {
                                    System.err.println("E: " + e);
                                    e.printStackTrace();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {
                    Log.d(TAG, "SOY Telefono =D");

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    listView = (ListView) findViewById(R.id.listView_apps);
                    jsonString = getIntent().getExtras().getString("jsonString");
                    idCategoria = getIntent().getExtras().getInt("idCategoria");
                    asyn_internet();

                    try {
                        jsonObj_object = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ArrayList<ListaApps> listaApp = listaApps(jsonObj_object, idCategoria);
                    textViewTitulo.setText(listaApp.get(0).getCategoria());
                    actionBar.setTitle("Apps - "+ listaApp.get(0).getCategoria());
                    Log.d(TAG, "********************** " + listaApp.size() + " categorias*************************");
                    for (int i = 0; i < listaApp.size(); i++) {
                        Log.d(TAG, "----------------");
                        Log.d(TAG, i + ". nombre: " + listaApp.get(i).getNombre());
                        Log.d(TAG, i + ". precio: " + listaApp.get(i).getPrecio());
                        Log.d(TAG, i + ". artista: " + listaApp.get(i).getArtista());
                        Log.d(TAG, i + ". Categoria: " + listaApp.get(i).getCategoria());
                        Log.d(TAG, i + ". URLImage: " + listaApp.get(i).getURLImage());
                        Log.d(TAG, i + ". idApp: " + listaApp.get(i).getIdApp());

                        Log.d(TAG, "----------------");
                    }
                    Log.d(TAG, "************************categorias***************************");

                    listView.setAdapter(new AdaptadorListView_apps(this, listaApp));

                    // Register a callback to be invoked when an item in this AdapterView
                    // has been clicked
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                            try {
                                ListaApps item = (ListaApps) listView.getAdapter().getItem(position);
                                Log.d(TAG, "onItemClick: lista label: [" + item.getNombre() + "]");

                                try {
                                    Intent mainIntent = new Intent().setClass(Apps.this, Detalles.class);
                                    mainIntent.putExtra("jsonString", jsonString);
                                    mainIntent.putExtra("idApp", item.getIdApp());
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeCustomAnimation(Apps.this, R.anim.desvanecer_in, R.anim.desvanecer_out);
                                    ActivityCompat.startActivity(Apps.this, mainIntent, options.toBundle());
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

            }catch (Exception e){
                e.printStackTrace();
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
                            comprobarInternet.execute("apps");
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
                if(mensajeInternet_apps.getVisibility() != View.INVISIBLE){
                    mensajeInternet_apps.setVisibility(View.INVISIBLE);
                    animar(false);
                }
            }else{
                if(mensajeInternet_apps.getVisibility() != View.VISIBLE) {
                    mensajeInternet_apps.setVisibility(View.VISIBLE);
                    animar(true);
                }
            }
        }

        private void animar(boolean mostrar){
            Animation animation = null;
            if (mostrar)
            {
                animation= AnimationUtils.loadAnimation(this, R.anim.mostrar_menu_bajo);
                mensajeInternet_apps.startAnimation(animation);
            }
            else
            {    //desde la esquina superior izquierda a la esquina inferior derecha
                animation=AnimationUtils.loadAnimation(this, R.anim.ocultar_menu_bajo);
                mensajeInternet_apps.startAnimation(animation);
            }
        }


        public ArrayList<ListaApps> listaApps(JSONObject jsonObj_object, int idCategoria){
            ArrayList<ListaApps> listaSalida = null;

            try {

                String StringjsonObj_object_feed = jsonObj_object.getString("feed");
                Log.d(TAG, "StringjsonObj_object_feed: ["+StringjsonObj_object_feed+"]");

                JSONObject jsonObj_object_feed = new JSONObject(StringjsonObj_object_feed);
                String StringjsonObj_object_feed_entry = jsonObj_object_feed.getString("entry");
                Log.d(TAG, "StringjsonObj_object_feed_entry: ["+StringjsonObj_object_feed_entry+"]");

                JSONArray jsonObj_object_feed_entry = new JSONArray(StringjsonObj_object_feed_entry);

                listaSalida = new ArrayList<>();

                ListaApps item;

                for(int i = 0; i < jsonObj_object_feed_entry.length(); i++) {

                    ////////////////idCategoria
                    String StringjsonObj_object_feed_entry_category = jsonObj_object_feed_entry.getJSONObject(i).getString("category");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_category  "+i+": ["+StringjsonObj_object_feed_entry_category+"]");

                    JSONObject jsonObj_object_feed_entry_category = new JSONObject(StringjsonObj_object_feed_entry_category);
                    String StringjsonObj_object_feed_entry_category_attributes = jsonObj_object_feed_entry_category.getString("attributes");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_category_attributes  "+i+": ["+StringjsonObj_object_feed_entry_category_attributes+"]");

                    JSONObject jsonObj_object_feed_entry_category_attributes = new JSONObject(StringjsonObj_object_feed_entry_category_attributes);

                    String StringjsonObj_object_feed_entry_category_attributes_label = jsonObj_object_feed_entry_category_attributes.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_category_attributes_label  "+i+": ["+StringjsonObj_object_feed_entry_category_attributes_label+"]");

                    int IntjsonObj_object_feed_entry_category_attributes_imid = Integer.parseInt(jsonObj_object_feed_entry_category_attributes.getString("im:id"));
                    Log.d(TAG, "IntjsonObj_object_feed_entry_category_attributes_imid  "+i+": ["+IntjsonObj_object_feed_entry_category_attributes_imid+"]");
                    ////////////////

                    if(idCategoria == IntjsonObj_object_feed_entry_category_attributes_imid){
                        ////////////////nombre
                        String StringjsonObj_object_feed_entry_imname = jsonObj_object_feed_entry.getJSONObject(i).getString("im:name");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imname  " + i + ": [" + StringjsonObj_object_feed_entry_imname + "]");

                        JSONObject jsonObj_object_feed_entry_imname = new JSONObject(StringjsonObj_object_feed_entry_imname);
                        String StringjsonObj_object_feed_entry_imname_label = jsonObj_object_feed_entry_imname.getString("label");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imname_label  " + i + ": [" + StringjsonObj_object_feed_entry_imname_label + "]");
                        ////////////////


                        ////////////////precio
                        String StringjsonObj_object_feed_entry_imprice = jsonObj_object_feed_entry.getJSONObject(i).getString("im:price");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imprice  " + i + ": [" + StringjsonObj_object_feed_entry_imprice + "]");

                        JSONObject jsonObj_object_feed_entry_imprice = new JSONObject(StringjsonObj_object_feed_entry_imprice);
                        String StringjsonObj_object_feed_entry_imprice_attributes = jsonObj_object_feed_entry_imprice.getString("attributes");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imprice_attributes  " + i + ": [" + StringjsonObj_object_feed_entry_imprice_attributes + "]");

                        JSONObject jsonObj_object_feed_entry_imprice_attributes = new JSONObject(StringjsonObj_object_feed_entry_imprice_attributes);


                        String StringjsonObj_object_feed_entry_imprice_attributes_amount = jsonObj_object_feed_entry_imprice_attributes.getString("amount");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imprice_attributes_amount  " + i + ": [" + StringjsonObj_object_feed_entry_imprice_attributes_amount + "]");

                        String StringjsonObj_object_feed_entry_imprice_attributes_currency = jsonObj_object_feed_entry_imprice_attributes.getString("currency");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imprice_attributes_cuyrrency  " + i + ": [" + StringjsonObj_object_feed_entry_imprice_attributes_currency + "]");
                        ////////////////


                        ////////////////artista
                        String StringjsonObj_object_feed_entry_imartist = jsonObj_object_feed_entry.getJSONObject(i).getString("im:artist");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imartist  " + i + ": [" + StringjsonObj_object_feed_entry_imartist + "]");

                        JSONObject jsonObj_object_feed_entry_imartist = new JSONObject(StringjsonObj_object_feed_entry_imartist);
                        String StringjsonObj_object_feed_entry_imartist_label = jsonObj_object_feed_entry_imartist.getString("label");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imartist_label  " + i + ": [" + StringjsonObj_object_feed_entry_imartist_label + "]");
                        ////////////////

                        ////////////////url de la imagen
                        String StringjsonObj_object_feed_entry_imimage = jsonObj_object_feed_entry.getJSONObject(i).getString("im:image");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imimage  " + i + ": [" + StringjsonObj_object_feed_entry_imimage + "]");

                        JSONArray StringjsonObj_object_feed_entry_imimage_list = new JSONArray(StringjsonObj_object_feed_entry_imimage);
                        String StringjsonObj_object_feed_entry_imimage_list_2_label = StringjsonObj_object_feed_entry_imimage_list.getJSONObject(2).getString("label");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_imimage_list_2_label  " + i + ": [" + StringjsonObj_object_feed_entry_imimage_list_2_label + "]");
                        ////////////////

                        ////////////////id
                        String StringjsonObj_object_feed_entry_id = jsonObj_object_feed_entry.getJSONObject(i).getString("id");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_id  "+i+": ["+StringjsonObj_object_feed_entry_id+"]");

                        JSONObject jsonObj_object_feed_entry_id = new JSONObject(StringjsonObj_object_feed_entry_id);
                        String StringjsonObj_object_feed_entry_id_attributes = jsonObj_object_feed_entry_id.getString("attributes");
                        Log.d(TAG, "StringjsonObj_object_feed_entry_id_attributes  "+i+": ["+StringjsonObj_object_feed_entry_id_attributes+"]");

                        JSONObject jsonObj_object_feed_entry_id_attributes = new JSONObject(StringjsonObj_object_feed_entry_id_attributes);

                        int IntjsonObj_object_feed_entry_id_attributes_imid = Integer.parseInt(jsonObj_object_feed_entry_id_attributes.getString("im:id"));
                        Log.d(TAG, "IntjsonObj_object_feed_entry_id_attributes_imid  "+i+": ["+IntjsonObj_object_feed_entry_id_attributes_imid+"]");
                        ////////////////

                        item = new ListaApps();

                        item.setArtista(StringjsonObj_object_feed_entry_imartist_label);
                        item.setCategoria(StringjsonObj_object_feed_entry_category_attributes_label);
                        item.setPrecio(StringjsonObj_object_feed_entry_imprice_attributes_amount + " " + StringjsonObj_object_feed_entry_imprice_attributes_currency);
                        item.setNombre(StringjsonObj_object_feed_entry_imname_label);
                        item.setURLImage(StringjsonObj_object_feed_entry_imimage_list_2_label);
                        item.setIdApp(IntjsonObj_object_feed_entry_id_attributes_imid);

                        listaSalida.add(item);


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return listaSalida;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }

    }
