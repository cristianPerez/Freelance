package com.emprende;

import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import com.adaptadores.AdaptadorPreguntas;
import com.emprende.R;
import com.recursos.AdaptadorNoticias;
import com.recursos.WebService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PestanaPreguntas extends Activity {
	
	private WebService ws;
	private ProgressDialog dialog;
	private ListView listaPreguntas;
	private JSONArray respuesta;
	private Typeface fuenteTitulo;
	static final int REQUEST_DESCRIPTION = 1;
    private Button hacerPregunta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pestana_preguntas);
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.ws = new WebService();
		this.dialog = new ProgressDialog(PestanaPreguntas.this);
		this.listaPreguntas = (ListView) findViewById(R.id.lstPreguntas);
        this.hacerPregunta = (Button) findViewById(R.id.hacerPregunta);
        this.hacerPregunta.setTypeface(fuenteTitulo);
		//this.listaPreguntas.setItemsCanFocus(false);
		new Sincronizar().execute();
	}
	
	private void mostrarPreguntas(JSONArray preguntas){
		AdaptadorPreguntas adaptador = new AdaptadorPreguntas(this, preguntas, this.fuenteTitulo);
		this.listaPreguntas.setAdapter(adaptador);
		this.dialog.dismiss();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pestana_preguntas, menu);
		return true;
	}
	
	public void preguntar(View v){
		Intent intent = new Intent(this, Pregunta.class);
		startActivityForResult(intent, REQUEST_DESCRIPTION);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DESCRIPTION) {
			if (resultCode == RESULT_OK) {
				new Sincronizar().execute();
			}
		}
	}

	private class Sincronizar extends AsyncTask<Void, Void, Void> {
		
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
		protected Void doInBackground(Void... arg0) {
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_blog.php?method=mostrar_preguntas"};
			respuesta = ws.conectar(parametros);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			mostrarPreguntas(respuesta);
		}
	}
}
