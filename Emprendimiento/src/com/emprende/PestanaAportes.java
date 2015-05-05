package com.emprende;

import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import com.adaptadores.AdaptadorAportes;
import com.adaptadores.AdaptadorPreguntas;
import com.emprende.R;
import com.recursos.WebService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PestanaAportes extends Activity {
	
	private WebService ws;
	private ProgressDialog dialog;
	private String aportes_publicos = "";
	private ListView lstAportes;
	private Typeface fuenteTitulo;
	static final int REQUEST_DESCRIPTION = 1;
    private Button btnAportar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pestana_aportes);

		this.ws = new WebService();
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.dialog = new ProgressDialog(PestanaAportes.this);
		this.lstAportes = (ListView) (findViewById(R.id.lstAportes));
        this.btnAportar = (Button) findViewById(R.id.btnAportar);
        this.btnAportar.setTypeface(fuenteTitulo);
		new Sincronizar().execute();
	}

	public void aportar(View v) {
		Intent intent = new Intent(this, Aporte.class);
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
	
	private void mostrarAportes(JSONArray aportes){
		AdaptadorAportes adaptador = new AdaptadorAportes(this, aportes, this.fuenteTitulo);
		this.lstAportes.setAdapter(adaptador);
		this.dialog.dismiss();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pestana_aportes, menu);
		return true;
	}
	
	private class Sincronizar extends AsyncTask<Void, Void, Void> {

		private JSONArray respuesta;
		
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
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_blog.php?method=mostrar_aportes"};
			respuesta = ws.conectar(parametros);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			mostrarAportes(respuesta);
		}
	}

}