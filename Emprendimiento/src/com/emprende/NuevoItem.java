package com.emprende;

import org.json.JSONArray;
import org.json.JSONException;
import com.emprende.R;
import com.emprende.R.color;
import com.recursos.WebService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoItem extends Activity {
	
	private String tipo;
	private EditText txtTitulo;
	private EditText txtDescripcion;
	private Spinner spnColores;
	private WebService ws;
	private ProgressDialog dialog;
	private String actividad;
	private String canvas_id;
	private String id_item;
	private String estado_item;
	private Typeface fuenteTitulo;
	private TextView lblTituloView;
	private Button btnGuardar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nuevo_item);
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		
		this.txtTitulo = (EditText) findViewById(R.id.lblTitulo);
		this.txtDescripcion = (EditText) findViewById(R.id.lblDescripcion);
		this.spnColores = (Spinner) findViewById(R.id.spnColor);
		this.lblTituloView = (TextView) findViewById(R.id.lblTituloNuevoItem);
		this.btnGuardar = (Button) findViewById(R.id.btnGuardarItem);
		
		this.txtTitulo.setTypeface(fuenteTitulo);
		this.txtDescripcion.setTypeface(fuenteTitulo);
		this.lblTituloView.setTypeface(fuenteTitulo);
		this.btnGuardar.setTypeface(fuenteTitulo);
		
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		WindowManager.LayoutParams params = getWindow().getAttributes();  
		//params.height = height-30;  
		params.width = width-40;    
		this.getWindow().setAttributes(params); 
		
		this.llenarSpiner();
		this.ws = new WebService();
		this.dialog = new ProgressDialog(this);
		Intent intent = getIntent();
		this.tipo = intent.getStringExtra("TIPO");
		this.actividad = intent.getStringExtra("ACTIVIDAD");
		if(tipo.equals("edicion")){
			this.txtTitulo.setText(intent.getStringExtra("TITULO"));
			this.txtDescripcion.setText(intent.getStringExtra("DESCRIPCION"));
			String clase = intent.getStringExtra("CLASE");
			this.id_item = intent.getStringExtra("ID_ITEM");
			this.estado_item = intent.getStringExtra("ESTADO_ITEM");
			
			if(clase.equals("btn-danger")){
				this.spnColores.setSelection(1);
			}
			else if(clase.equals("btn-success")){
				this.spnColores.setSelection(3);
			}
			else if(clase.equals("btn-default")){
				this.spnColores.setSelection(0);
			}
			else{
				this.spnColores.setSelection(2);
			}
		}
		else{
			this.canvas_id = intent.getStringExtra("CANVAS_ID");
			this.estado_item = "question";
		}
	}
	
	private void llenarSpiner() {
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.color, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spnColores.setAdapter(adapter);
	}
	
	public void guardar(View v){
		
		String url = "";
		String clase = "";
		
		if(this.txtTitulo.getText().toString().equals("") || this.txtDescripcion.getText().toString().equals("")){
			Toast.makeText(this, "Hay campos incompletos.\nPor favor completelos.", Toast.LENGTH_SHORT).show();
		}
		else{
			String titulo = this.txtTitulo.getText().toString();
			String descripcion = this.txtDescripcion.getText().toString();
			String color = this.spnColores.getSelectedItem().toString().toLowerCase();
			
			if(color.equals("rojo")){
				clase = "btn-danger";
			}
			else if(color.equals("verde")){
				clase = "btn-success";
			}
			else if(color.equals("normal")){
				clase = "btn-default";
			}
			else{
				clase = "btn-warning";
			}
			if (this.tipo.equals("nuevo")){
				url = "http://unidademprendimiento.tk/Controller/facade_canvas.php?method=nuevo_item";
				new Sincronizar().execute(url,this.actividad,clase,descripcion,this.estado_item,this.canvas_id,titulo);
			}
			else{
				url = "http://unidademprendimiento.tk/Controller/facade_canvas.php?method=modificar_item";
				new Sincronizar().execute(url,this.actividad,clase,descripcion,this.estado_item,this.id_item,titulo);
			}	
		}
	}
	
private class Sincronizar extends AsyncTask<String, Void, Boolean> {
	
	JSONArray respuesta;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Sincronizando");
			dialog.setMessage("Espere un momento...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			if(tipo.equals("nuevo")){
				String[] parametros = 
					{
						"url,"+arg0[0],
						"actividad,"+arg0[1],
						"clase,"+arg0[2],
						"descripcion,"+arg0[3],
						"estado_item,"+arg0[4],
						"id_canvas,"+arg0[5],
						"titulo,"+arg0[6]
					};
				respuesta = ws.conectar(parametros);
			}
			else{
				String[] parametros = 
					{
						"url,"+arg0[0],
						"actividad,"+arg0[1],
						"clase,"+arg0[2],
						"descripcion,"+arg0[3],
						"estado_item,"+arg0[4],
						"id,"+arg0[5],
						"titulo,"+arg0[6]
					};
				respuesta = ws.conectar(parametros);
			}
			
			try {
				if(respuesta.getJSONObject(0).getInt("respuesta")==1){
					return true;
				}
				else{
					return false;
				}
			} catch (JSONException e) {}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(!result){
				Toast.makeText(getApplicationContext(), "No se pudo guardar la información,\n" +
						"por favor intentalo de nuevo.", Toast.LENGTH_LONG).show();
			}
			else{
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}
}
