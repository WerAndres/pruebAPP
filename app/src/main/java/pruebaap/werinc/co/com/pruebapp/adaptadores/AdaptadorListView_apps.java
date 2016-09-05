package pruebaap.werinc.co.com.pruebapp.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pruebaap.werinc.co.com.pruebapp.POJO.ListaApps;
import pruebaap.werinc.co.com.pruebapp.R;

/**
 * Created by werinc on 3/09/16.
 */
public class AdaptadorListView_apps extends BaseAdapter {

    private Context context;
    private ArrayList<ListaApps> lista;
    public String TAG = this.getClass().getName();
    public AdaptadorListView_apps(Context context, ArrayList<ListaApps> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return this.lista.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: ENTREEEE");
        View rowView = null;
        try {
            rowView = convertView;

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.prototipo_app, parent, false);
            }

            TextView nombre = (TextView) rowView.findViewById(R.id.textView_nombre);
            TextView precio = (TextView) rowView.findViewById(R.id.textView_precio);
            TextView artista = (TextView) rowView.findViewById(R.id.textView_artista);
            TextView categoria = (TextView) rowView.findViewById(R.id.textView_categoria);
            ImageView URLImage = (ImageView) rowView.findViewById(R.id.imageView_imagen);


            nombre.setText(lista.get(position).getNombre());
            precio.setText(lista.get(position).getPrecio());
            artista.setText(lista.get(position).getArtista());
            categoria.setText(lista.get(position).getCategoria());

            if(hayInternet(context)) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                URL imageUrl = null;
                HttpURLConnection conn = null;
                imageUrl = new URL(lista.get(position).getURLImage());
                conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream());
                storeImage(imagen, lista.get(position).getNombre()+Uri.parse(lista.get(position).getURLImage()).getLastPathSegment());
                URLImage.setImageBitmap(imagen);
            }else{
                String sFichero = context.getCacheDir() + "/" + lista.get(position).getNombre()+Uri.parse(lista.get(position).getURLImage()).getLastPathSegment();
                File fichero = new File(sFichero);
                if (fichero.exists()) {
                    Log.d(TAG, "getView: El fichero " + sFichero + " existe");                    
                    Bitmap imagen = BitmapFactory.decodeFile(sFichero);                    
                    URLImage.setImageBitmap(imagen);
                }else{
                    URLImage.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_menu_gallery));
                    Log.d(TAG, "getView:  NO HAY NADA");
                }                
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return rowView;
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
            outputStream = new FileOutputStream(context.getCacheDir() + "/" + fileName);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Log.d(TAG, "storeImage: " + fileName + "   OK" + "  ");
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }



}