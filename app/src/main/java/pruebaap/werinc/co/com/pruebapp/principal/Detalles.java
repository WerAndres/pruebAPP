package pruebaap.werinc.co.com.pruebapp.principal;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import pruebaap.werinc.co.com.pruebapp.POJO.DetallesApp;
import pruebaap.werinc.co.com.pruebapp.R;
import pruebaap.werinc.co.com.pruebapp.configuraciones.ComprobarInternet;
import pruebaap.werinc.co.com.pruebapp.configuraciones.Configuraciones;

public class Detalles extends AppCompatActivity {

    private String TAG = "LogspruebAAP";
    public static Detalles global;
    public RelativeLayout mensajeInternet_detalles;
    public String jsonString;
    public Configuraciones conf = new Configuraciones();
    public ComprobarInternet comprobarInternet = null;
    public JSONObject jsonObj_object;
    public int idApp;

    public TextView nombre;
    public ImageView image;
    public TextView summary;
    public TextView precio;
    public TextView contentType;
    public TextView derechos;
    public TextView titulo;
    public TextView link;
    public TextView id;
    public TextView artista;
    public TextView categoria;
    public TextView fechaLanzamiento;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        global = this;
        Log.d(TAG, "-------------------------------------CLASE detalles-------------------------------------");
        mensajeInternet_detalles = (RelativeLayout) findViewById(R.id.mensajeInternet_detalles);

        nombre = (TextView) findViewById(R.id.textView_nombre_detalles);

        image = (ImageView) findViewById(R.id.imageView_imagen_detalles);

        summary = (TextView) findViewById(R.id.textView_summary_detalles);
        precio = (TextView) findViewById(R.id.textView_precio_detalles);
        contentType = (TextView) findViewById(R.id.textView_contentType_detalles);
        derechos = (TextView) findViewById(R.id.textView_derechos_detalles);
        titulo = (TextView) findViewById(R.id.textView_titulo_detalles);
        link = (TextView) findViewById(R.id.textView_link_detalles);
        id = (TextView) findViewById(R.id.textView_id_detalles);
        artista = (TextView) findViewById(R.id.textView_artista_detalles);
        categoria = (TextView) findViewById(R.id.textView_categoria_detalles);
        fechaLanzamiento = (TextView) findViewById(R.id.textView_fechaLanzamiento_detalles);


        if (isTabletDevice(this)) {
            Log.d(TAG, "SOY Tablet =D");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            Log.d(TAG, "SOY Telefono =D");

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
        jsonString = getIntent().getExtras().getString("jsonString");
        idApp = getIntent().getExtras().getInt("idApp");
        asyn_internet();

        try {
            jsonObj_object = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DetallesApp detalles_App = listaDetalles(jsonObj_object, idApp);

        Log.d(TAG, "********************** Detalles App *************************");
        Log.d(TAG, "----------------");
        Log.d(TAG, "nombre: " + detalles_App.getNombre());
        Log.d(TAG, "URLImage: " + detalles_App.getURLImage());
        Log.d(TAG, "summary: " + detalles_App.getSummary());
        Log.d(TAG, "precio: " + detalles_App.getPrecio());
        Log.d(TAG, "contentTypr: " + detalles_App.getContentType());
        Log.d(TAG, "derechos: " + detalles_App.getDerechos());
        Log.d(TAG, "titulo: " + detalles_App.getTitulo());
        Log.d(TAG, "link: " + detalles_App.getLink());
        Log.d(TAG, "id: " + detalles_App.getId());
        Log.d(TAG, "artista: " + detalles_App.getArtista());
        Log.d(TAG, "idCategoria: " + detalles_App.getCategoria());
        Log.d(TAG, "fechaLanzamiento: " + detalles_App.getFechaLanzamiento());
        Log.d(TAG, "----------------");
        Log.d(TAG, "************************ Detalles App ***************************");

        if(detalles_App.getNombre().length()>20){
            nombre.setTextSize(15);
        }

        nombre.setText(detalles_App.getNombre());

        if(hayInternet(this)) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageUrl = null;
            HttpURLConnection conn = null;
            try {
                imageUrl = new URL(detalles_App.getURLImage());
                conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream());
                storeImage(imagen, detalles_App.getNombre()+ Uri.parse(detalles_App.getURLImage()).getLastPathSegment());

                image.setImageBitmap(imagen);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            String sFichero = getCacheDir() + "/" + detalles_App.getNombre()+Uri.parse(detalles_App.getURLImage()).getLastPathSegment();
            File fichero = new File(sFichero);
            if (fichero.exists()) {
                Log.d(TAG, "getView: El fichero " + sFichero + " existe");
                File file = getCacheDir();
                Bitmap imagen = BitmapFactory.decodeFile(sFichero);
                storeImage(imagen, detalles_App.getNombre()+Uri.parse(detalles_App.getURLImage()).getLastPathSegment());

                image.setImageBitmap(imagen);

            }else{
                image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_gallery));
                Log.d(TAG, "getView: ISSSSSSSSSSSHHHHHHHHHHHHHHHHHHHH NO HAY NADA");
            }
        }

        summary.setText(detalles_App.getSummary());
        precio.setText(detalles_App.getPrecio());
        contentType.setText(detalles_App.getContentType());
        derechos.setText(detalles_App.getDerechos());
        titulo.setText(detalles_App.getTitulo());
        link.setText(detalles_App.getLink());
        id.setText(detalles_App.getId());
        artista.setText(detalles_App.getArtista());
        categoria.setText(detalles_App.getCategoria());
        fechaLanzamiento.setText(detalles_App.getFechaLanzamiento());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animar_imagen_detalle();
            }
        });


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
                        comprobarInternet.execute("detalles");
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


    public DetallesApp listaDetalles(JSONObject jsonObj_object, int idApp){
        DetallesApp Salida = new DetallesApp();

        try {

            String StringjsonObj_object_feed = jsonObj_object.getString("feed");
            Log.d(TAG, "StringjsonObj_object_feed: ["+StringjsonObj_object_feed+"]");

            JSONObject jsonObj_object_feed = new JSONObject(StringjsonObj_object_feed);
            String StringjsonObj_object_feed_entry = jsonObj_object_feed.getString("entry");
            Log.d(TAG, "StringjsonObj_object_feed_entry: ["+StringjsonObj_object_feed_entry+"]");

            JSONArray jsonObj_object_feed_entry = new JSONArray(StringjsonObj_object_feed_entry);

            for(int i = 0; i < jsonObj_object_feed_entry.length(); i++) {

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
                Log.d(TAG, "idApp: " + idApp);
                if(idApp == IntjsonObj_object_feed_entry_id_attributes_imid){

                    ////////////////nombre
                    String StringjsonObj_object_feed_entry_imname = jsonObj_object_feed_entry.getJSONObject(i).getString("im:name");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imname  " + i + ": [" + StringjsonObj_object_feed_entry_imname + "]");

                    JSONObject jsonObj_object_feed_entry_imname = new JSONObject(StringjsonObj_object_feed_entry_imname);
                    String StringjsonObj_object_feed_entry_imname_label = jsonObj_object_feed_entry_imname.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imname_label  " + i + ": [" + StringjsonObj_object_feed_entry_imname_label + "]");
                    ////////////////

                    ////////////////url de la imagen
                    String StringjsonObj_object_feed_entry_imimage = jsonObj_object_feed_entry.getJSONObject(i).getString("im:image");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imimage  " + i + ": [" + StringjsonObj_object_feed_entry_imimage + "]");

                    JSONArray StringjsonObj_object_feed_entry_imimage_list = new JSONArray(StringjsonObj_object_feed_entry_imimage);
                    String StringjsonObj_object_feed_entry_imimage_list_2_label = StringjsonObj_object_feed_entry_imimage_list.getJSONObject(2).getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imimage_list_2_label  " + i + ": [" + StringjsonObj_object_feed_entry_imimage_list_2_label + "]");
                    ////////////////


                    ////////////////summary
                    String StringjsonObj_object_feed_entry_summary = jsonObj_object_feed_entry.getJSONObject(i).getString("summary");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_summary  " + i + ": [" + StringjsonObj_object_feed_entry_summary + "]");

                    JSONObject jsonObj_object_feed_entry_summary = new JSONObject(StringjsonObj_object_feed_entry_summary);
                    String StringjsonObj_object_feed_entry_summary_label = jsonObj_object_feed_entry_summary.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_summary_label  " + i + ": [" + StringjsonObj_object_feed_entry_summary_label + "]");
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


                    ////////////////contentType
                    String StringjsonObj_object_feed_entry_imcontentType = jsonObj_object_feed_entry.getJSONObject(i).getString("im:contentType");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imcontentType  "+i+": ["+StringjsonObj_object_feed_entry_imcontentType+"]");

                    JSONObject jsonObj_object_feed_entry_imcontentType = new JSONObject(StringjsonObj_object_feed_entry_imcontentType);
                    String StringjsonObj_object_feed_entry_imcontentType_attributes = jsonObj_object_feed_entry_imcontentType.getString("attributes");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imcontentType_attributes  "+i+": ["+StringjsonObj_object_feed_entry_imcontentType_attributes+"]");

                    JSONObject jsonObj_object_feed_entry_imcontentType_attributes = new JSONObject(StringjsonObj_object_feed_entry_imcontentType_attributes);

                    String StringjsonObj_object_feed_entry_imcontentType_attributes_label = jsonObj_object_feed_entry_imcontentType_attributes.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imcontentType_attributes_label  "+i+": ["+StringjsonObj_object_feed_entry_imcontentType_attributes_label+"]");
                    ////////////////

                    ////////////////derechos
                    String StringjsonObj_object_feed_entry_rights = jsonObj_object_feed_entry.getJSONObject(i).getString("rights");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_rights " + i + ": [" + StringjsonObj_object_feed_entry_rights + "]");

                    JSONObject jsonObj_object_feed_entry_rights = new JSONObject(StringjsonObj_object_feed_entry_rights);
                    String StringjsonObj_object_feed_entry_rights_label = jsonObj_object_feed_entry_rights.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_rights_label  " + i + ": [" + StringjsonObj_object_feed_entry_rights_label + "]");
                    ////////////////


                    ////////////////titulo
                    String StringjsonObj_object_feed_entry_title = jsonObj_object_feed_entry.getJSONObject(i).getString("title");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_title  " + i + ": [" + StringjsonObj_object_feed_entry_title + "]");

                    JSONObject jsonObj_object_feed_entry_title = new JSONObject(StringjsonObj_object_feed_entry_title);
                    String StringjsonObj_object_feed_entry_title_label = jsonObj_object_feed_entry_title.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_title_label  " + i + ": [" + StringjsonObj_object_feed_entry_title_label + "]");
                    ////////////////


                    ////////////////link
                    String StringjsonObj_object_feed_entry_link = jsonObj_object_feed_entry.getJSONObject(i).getString("link");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_link  "+i+": ["+StringjsonObj_object_feed_entry_link+"]");

                    JSONObject jsonObj_object_feed_entry_link = new JSONObject(StringjsonObj_object_feed_entry_link);
                    String StringjsonObj_object_feed_entry_link_attributes = jsonObj_object_feed_entry_link.getString("attributes");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_link_attributes  "+i+": ["+StringjsonObj_object_feed_entry_link_attributes+"]");

                    JSONObject jsonObj_object_feed_entry_link_attributes = new JSONObject(StringjsonObj_object_feed_entry_link_attributes);

                    String StringjsonObj_object_feed_entry_link_attributes_href = jsonObj_object_feed_entry_link_attributes.getString("href");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_link_attributes_href  "+i+": ["+StringjsonObj_object_feed_entry_link_attributes_href+"]");
                    ////////////////


                    ////////////////artista
                    String StringjsonObj_object_feed_entry_imartist = jsonObj_object_feed_entry.getJSONObject(i).getString("im:artist");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imartist  " + i + ": [" + StringjsonObj_object_feed_entry_imartist + "]");

                    JSONObject jsonObj_object_feed_entry_imartist = new JSONObject(StringjsonObj_object_feed_entry_imartist);
                    String StringjsonObj_object_feed_entry_imartist_label = jsonObj_object_feed_entry_imartist.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imartist_label  " + i + ": [" + StringjsonObj_object_feed_entry_imartist_label + "]");
                    ////////////////

                    ////////////////categoria
                    String StringjsonObj_object_feed_entry_category = jsonObj_object_feed_entry.getJSONObject(i).getString("category");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_category  "+i+": ["+StringjsonObj_object_feed_entry_category+"]");

                    JSONObject jsonObj_object_feed_entry_category = new JSONObject(StringjsonObj_object_feed_entry_category);
                    String StringjsonObj_object_feed_entry_category_attributes = jsonObj_object_feed_entry_category.getString("attributes");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_category_attributes  "+i+": ["+StringjsonObj_object_feed_entry_category_attributes+"]");

                    JSONObject jsonObj_object_feed_entry_category_attributes = new JSONObject(StringjsonObj_object_feed_entry_category_attributes);

                    String StringjsonObj_object_feed_entry_category_attributes_label = jsonObj_object_feed_entry_category_attributes.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_category_attributes_label  "+i+": ["+StringjsonObj_object_feed_entry_category_attributes_label+"]");
                    ////////////////

                    ////////////////fechaLanzamiento
                    String StringjsonObj_object_feed_entry_imreleaseDate = jsonObj_object_feed_entry.getJSONObject(i).getString("im:releaseDate");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imreleaseDate  " + i + ": [" + StringjsonObj_object_feed_entry_imreleaseDate + "]");

                    JSONObject jsonObj_object_feed_entry_imreleaseDate = new JSONObject(StringjsonObj_object_feed_entry_imreleaseDate);
                    String StringjsonObj_object_feed_entry_imreleaseDate_label = jsonObj_object_feed_entry_imreleaseDate.getString("label");
                    Log.d(TAG, "StringjsonObj_object_feed_entry_imreleaseDate_label  " + i + ": [" + StringjsonObj_object_feed_entry_imreleaseDate_label + "]");
                    ////////////////

                    Salida.setNombre(StringjsonObj_object_feed_entry_imname_label);
                    Salida.setURLImage(StringjsonObj_object_feed_entry_imimage_list_2_label);
                    Salida.setSummary(StringjsonObj_object_feed_entry_summary_label);
                    Salida.setPrecio(StringjsonObj_object_feed_entry_imprice_attributes_amount + " " + StringjsonObj_object_feed_entry_imprice_attributes_currency);
                    Salida.setContentType(StringjsonObj_object_feed_entry_imcontentType_attributes_label);
                    Salida.setDerechos(StringjsonObj_object_feed_entry_rights_label);
                    Salida.setTitulo(StringjsonObj_object_feed_entry_title_label);
                    Salida.setLink(StringjsonObj_object_feed_entry_link_attributes_href);
                    Salida.setId(IntjsonObj_object_feed_entry_id_attributes_imid + "");
                    Salida.setArtista(StringjsonObj_object_feed_entry_imartist_label);
                    Salida.setCategoria(StringjsonObj_object_feed_entry_category_attributes_label);
                    Salida.setFechaLanzamiento(StringjsonObj_object_feed_entry_imreleaseDate_label);


                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Salida;
    }

    private boolean hayInternet(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < redes.length; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
                Log.d(TAG, "Hay internet");
            }
        }
        if(!connected){
            Log.d(TAG, "NO Hay internet");
        }
        return connected;
    }

    private void storeImage(Bitmap image, String fileName) {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(getCacheDir() + "/" + fileName);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Log.d(TAG, "storeImage: " + fileName + "   OK" + "  ");
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public void OcultarMostrarMensajeInternet(boolean hayInternet){
        if(hayInternet){
            if(mensajeInternet_detalles.getVisibility() != View.INVISIBLE){
                mensajeInternet_detalles.setVisibility(View.INVISIBLE);
                animar(false);
            }
        }else{
            if(mensajeInternet_detalles.getVisibility() != View.VISIBLE) {
                mensajeInternet_detalles.setVisibility(View.VISIBLE);
                animar(true);
            }
        }
    }

    private void animar(boolean mostrar){
        Animation animation = null;
        if (mostrar)
        {
            animation= AnimationUtils.loadAnimation(this, R.anim.mostrar_menu_bajo);
            mensajeInternet_detalles.startAnimation(animation);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation=AnimationUtils.loadAnimation(this, R.anim.ocultar_menu_bajo);
            mensajeInternet_detalles.startAnimation(animation);
        }
    }

    private void animar_imagen_detalle(){
        Animation animation = null;
        animation= AnimationUtils.loadAnimation(this, R.anim.zumbido);
        image.startAnimation(animation);

    }

}
