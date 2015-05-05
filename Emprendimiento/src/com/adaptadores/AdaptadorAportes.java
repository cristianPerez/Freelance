package com.adaptadores;

import org.json.JSONArray;
import org.json.JSONException;

import com.emprende.R;
import com.emprende.Respuestas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AdaptadorAportes extends BaseAdapter {

	private final Activity actividad;
	private final JSONArray lista;
	private Typeface fuente;

	public AdaptadorAportes(Activity actividad, JSONArray lista, Typeface fuente) {
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
		View view = inflater.inflate(R.layout.item_lista_aportes, null, true);

		final int seleccion = position;
		TextView lblNombre = (TextView) view.findViewById(R.id.lblNombre);
		TextView lblAsunto = (TextView) view.findViewById(R.id.lblAsunto);
		TextView lblAporte = (TextView) view.findViewById(R.id.lblAporte);
		TextView lblFecha = (TextView) view.findViewById(R.id.lblFecha);
		
		lblNombre.setTypeface(fuente, Typeface.BOLD);
		lblAsunto.setTypeface(fuente);
		lblAporte.setTypeface(fuente);
		lblFecha.setTypeface(fuente);
		
		JSONArray aporte;
		try {
			aporte = this.lista.getJSONArray(position);
			lblNombre.setText(aporte.getString(3));
			lblAsunto.setText(aporte.getString(1));
			lblAporte.setText(aporte.getString(2));
			
			//Formato fecha
			String[] fechaCompleta = aporte.getString(4).split(" ");
			String[] fecha = fechaCompleta[0].split("-");
			lblFecha.setText(Html.fromHtml(fecha[2] + "/" + fecha[1] + "/" + fecha[0] ));
			
		} catch (JSONException e1) {
		}

		return view;
	}

	public int getCount() {
		return lista.length();
	}

	public Object getItem(int arg0) {
		try {
			return lista.getJSONArray(arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

}
