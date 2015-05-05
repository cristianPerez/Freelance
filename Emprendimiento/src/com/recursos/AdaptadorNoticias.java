package com.recursos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.emprende.R;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorNoticias extends BaseAdapter {

    private final Activity actividad;
    private final JSONArray lista;
    private Typeface fuenteTitulo;
    private int [] fotos=null;
    private int positionImg;

    public AdaptadorNoticias(Activity actividad, JSONArray lista,Typeface fn) {
    	  super();
          this.actividad = actividad;
          this.lista = lista;
          this.fuenteTitulo = fn;
          this.positionImg=0;
    }
/**
 * @return: vista que contiene la lista renderizada con imagenes a 
 * los extremos de cada item.
 */
    public View getView (int position, View convertView,ViewGroup parent) {
    	  completeFotos();
          LayoutInflater inflater = actividad.getLayoutInflater();
          View view = inflater.inflate(R.layout.item_lista_noticias, null,true);
          TextView lblTituloNoticia = (TextView)view.findViewById(R.id.lblTituloNoticia);
          TextView lblFechaNoticia = (TextView)view.findViewById(R.id.fechaNoti);
          ImageView hoja = (ImageView)view.findViewById(R.id.hoja);
          lblTituloNoticia.setTypeface(this.fuenteTitulo);
          JSONObject obj;
		try {
			obj = this.lista.getJSONObject(position);
			lblTituloNoticia.setText(String.valueOf(obj.getString("titulo")));
			lblFechaNoticia.setText(String.valueOf(obj.getString("fecha")));
			hoja.setBackgroundResource(fotos[positionImg]);
			if(positionImg==2){
				positionImg=0;
			}
			else{
				positionImg++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
        return view;
    }

    public int getCount() {
    		return (lista.length());
    }

    public Object getItem (int arg0) {
          try {
			return lista.getJSONObject(arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          return null;
    }

    public long getItemId(int position) {
          return position;
    }
    
public void completeFotos(){
    	this.fotos = new int [3];
    	this.fotos [0] = R.drawable.h1;
    	this.fotos [1] = R.drawable.h2;
    	this.fotos [2] = R.drawable.h3;
    }
}
