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

public class AdaptadorPreguntas extends BaseAdapter {

	private final Activity actividad;
	private final JSONArray lista;
	private Typeface fuente;

	public AdaptadorPreguntas(Activity actividad, JSONArray lista, Typeface fuente) {
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
		View view = inflater.inflate(R.layout.item_lista_preguntas, null, true);

		final int seleccion = position;
		TextView lblNombre = (TextView) view.findViewById(R.id.lblNombre);
		TextView lblTipoPregunta = (TextView) view.findViewById(R.id.lblTipoPregunta);
		TextView lblNumRespuestas = (TextView) view.findViewById(R.id.lblNumRespuestas);
		TextView lblPregunta = (TextView) view.findViewById(R.id.lblPregunta);
		TextView lblFecha = (TextView) view.findViewById(R.id.lblFecha);
		Button btnRespuestas = (Button) view.findViewById(R.id.btnRespuestas);
		
		lblNombre.setTypeface(fuente, Typeface.BOLD);
		lblTipoPregunta.setTypeface(fuente);
		lblNumRespuestas.setTypeface(fuente);
		lblPregunta.setTypeface(fuente);
		lblFecha.setTypeface(fuente);
		
		JSONArray pregunta;
		try {
			pregunta = this.lista.getJSONArray(position);
			lblNombre.setText(pregunta.getString(3));
			lblTipoPregunta.setText(pregunta.getString(4));
			lblNumRespuestas.setText(pregunta.getString(6));
			if(pregunta.getString(1).charAt(0)!='¿'){
				if(pregunta.getString(1).charAt(pregunta.getString(1).length()-1)!='?'){
					lblPregunta.setText("¿"+pregunta.getString(1)+"?");
				}
				else{
					lblPregunta.setText("¿"+pregunta.getString(1));
				}
			}
			else if(pregunta.getString(1).charAt(pregunta.getString(1).length()-1)!='?'){
					lblPregunta.setText(pregunta.getString(1)+"?");
			}
			else{
				lblPregunta.setText(pregunta.getString(1));
			}
			
			//Formato fecha
			String[] fechaCompleta = pregunta.getString(2).split(" ");
			String[] fecha = fechaCompleta[0].split("-");
			lblFecha.setText(Html.fromHtml(fecha[2] + "/" + fecha[1] + "/" + fecha[0] ));
			btnRespuestas.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(actividad.getApplicationContext(),
							Respuestas.class);
					try {
						JSONArray elemento = lista.getJSONArray(seleccion);
						intent.putExtra("id", elemento.getString(0));
					} catch (JSONException e) {}
					
					actividad.startActivity(intent);
				}
			});
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
