package com.adaptadores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emprende.R;
import com.emprende.Respuestas;
import com.emprende.R.color;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AdaptadorComponenteICanvas extends BaseAdapter {

	private final Activity actividad;
	private final JSONArray lista;
	private Typeface fuente;

	public AdaptadorComponenteICanvas(Activity actividad, JSONArray lista, Typeface fuente) {
		super();
		this.actividad = actividad;
		this.lista = lista;
		this.fuente = fuente;
	}

	/**
	 * @return: vista que contiene la lista renderizada
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.componente_item_canvas, null, true);
		
		TextView lblTitulo =  (TextView) view.findViewById(R.id.lblTituloComponente);
		lblTitulo.setTypeface(fuente);

		try {
			JSONObject elemento = this.lista.getJSONObject(position);
			lblTitulo.setText(elemento.getString("titulo"));
			String clase = elemento.getString("clase");
			
			if(clase.equals("btn-danger")){
				lblTitulo.setTextColor(Color.WHITE);
				lblTitulo.setBackgroundColor(this.actividad.getResources().getColor(R.color.red));
			}
			else if(clase.equals("btn-success")){
				lblTitulo.setTextColor(Color.WHITE);
				lblTitulo.setBackgroundColor(this.actividad.getResources().getColor(R.color.green3));
			}
			else if(clase.equals("btn-default")){
				lblTitulo.setBackgroundColor(Color.WHITE);
			}
			else{
				lblTitulo.setTextColor(Color.WHITE);
				lblTitulo.setBackgroundColor(this.actividad.getResources().getColor(R.color.orange));
			}
		} catch (JSONException e) {} 

		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.lista.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		try {
			return this.lista.getJSONObject(arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
