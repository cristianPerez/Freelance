package com.recursos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emprende.LogIn;
import com.emprende.R;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorVentas extends BaseAdapter {

    private final Activity actividad;
    private final JSONArray lista;
    private Typeface fuenteTitulo;
    private int [] fotos=null;
    private int positionImg;
    private JSONArray obj;
    public SharedPreferences sharedpreferences;

    public AdaptadorVentas(Activity actividad, JSONArray lista,Typeface fn) {
    	  super();
          this.actividad = actividad;
          this.lista = lista;
          this.fuenteTitulo = fn;
          this.positionImg=0;
          sharedpreferences = actividad.getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
    }
/**
 * @return: vista que contiene la lista renderizada con imagenes a 
 * los extremos de cada item.
 */
    public View getView (int position, View convertView,ViewGroup parent) {
    	  //completeFotos();
          LayoutInflater inflater = actividad.getLayoutInflater();
          View view = inflater.inflate(R.layout.item_lista_ventas, null,true);
          TextView lblTituloNoticia = (TextView)view.findViewById(R.id.lblTituloVenta);
          //TextView lblFechaNoticia = (TextView)view.findViewById(R.id.fechaVenta);
          lblTituloNoticia.setTypeface(this.fuenteTitulo);
          //JSONObject obj;
		try {
			obj = this.lista.getJSONArray(position);
			lblTituloNoticia.setText(obj.getString(5));
			String user1 = sharedpreferences.getAll().get("nameKey").toString();
			String user2 = obj.getString(1);
			if(user1.equals(user2)){
				lblTituloNoticia.setBackgroundColor(actividad.getResources().getColor(R.color.verdeAlegre));
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
