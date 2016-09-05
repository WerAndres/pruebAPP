package pruebaap.werinc.co.com.pruebapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pruebaap.werinc.co.com.pruebapp.POJO.ListaCategorias;
import pruebaap.werinc.co.com.pruebapp.R;

/**
 * Created by werinc on 3/09/16.
 */
public class AdaptadorListView_categorias extends BaseAdapter {

    private Context context;
    private ArrayList<ListaCategorias> lista;

    public AdaptadorListView_categorias(Context context, ArrayList<ListaCategorias> lista) {
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

        View rowView = convertView;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.prototipo_categorias, parent, false);
        }


        TextView texto_arriba = (TextView) rowView.findViewById(R.id.textView_superior);
        TextView texto_abajo = (TextView) rowView.findViewById(R.id.textView_inferior);

        texto_arriba.setText(lista.get(position).getLabel());
        texto_abajo.setText(lista.get(position).getTerm());

        return rowView;
    }

}