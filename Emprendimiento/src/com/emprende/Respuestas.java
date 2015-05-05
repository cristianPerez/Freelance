package com.emprende;

import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.TextView;

public class Respuestas extends Activity {
	
	private TextView lstRespuestas;
	private WebService ws;
	private ProgressDialog dialog;
	private String respuestas_publicas="";
	static final int REQUEST_DESCRIPTION = 1;
	private String id;
	private Typeface fuenteTitulo;
	private Button btnAportarRespuesta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.respuestas);
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.id = getIntent().getExtras().getString("id");
		this.ws = new WebService();
		this.dialog = new ProgressDialog(Respuestas.this);
		this.lstRespuestas = (TextView) findViewById(R.id.lblRespuestas);
		this.btnAportarRespuesta = (Button) findViewById(R.id.btnResponder);
		this.btnAportarRespuesta.setTypeface(fuenteTitulo);
		this.lstRespuestas.setTypeface(fuenteTitulo);
		this.lstRespuestas.setMovementMethod(new ScrollingMovementMethod());
		new Sincronizar().execute(id);
	}
	
	private void mostrarRespuestas(JSONArray respuestas){
		this.respuestas_publicas = "";
		for(int i=0; i<respuestas.length(); i++){
			try {
				JSONArray respuesta = respuestas.getJSONArray(i);
				this.respuestas_publicas = this.respuestas_publicas + "<strong>" + respuesta.getString(0) + "</strong>"
						+ "<br>" + respuesta.getString(1) + "<br><br>";
				
			} catch (JSONException e) {
				this.respuestas_publicas = "No hay respuestas para esta pregunta";
			}
		}
		
		this.lstRespuestas.setText(Html.fromHtml(this.respuestas_publicas));
		this.dialog.dismiss();
	}
	
	public void responder(View v){
		Intent intent = new Intent(this, Respuesta.class);
		intent.putExtra("idPreg", this.id);
		startActivityForResult(intent, REQUEST_DESCRIPTION);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DESCRIPTION) {
			if (resultCode == RESULT_OK) {
				new Sincronizar().execute(this.id);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.respuestas, menu);
		return true;
	}

	private class Sincronizar extends AsyncTask<String, Void, Void> {

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
		protected Void doInBackground(String... arg0) {
			String id = arg0[0];
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_blog.php?method=mostrar_respuestas","id,"+id};
			respuesta = ws.conectar(parametros);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			mostrarRespuestas(respuesta);
		}
	}
}
