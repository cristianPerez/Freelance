package com.emprende;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.emprende.R;
import com.recursos.AdaptadorNoticias;
import com.recursos.WebService;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Noticias extends Activity implements OnItemClickListener {

	private WebService ws;
	private ListView lstNoticias;
	private ProgressDialog pd2;
	private JSONArray respuesta2;
	private Typeface fuenteTitulo;
	private TextView titulo;
	static final int REQUEST_DESCRIPTION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noticias);
		this.lstNoticias = (ListView) findViewById(R.id.listaNoticias);
		this.titulo = (TextView) findViewById(R.id.tituloapp);
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.ws = new WebService();
		pd2 = new ProgressDialog(this);
		pd2.setCancelable(false);
		
		if (hayInternet()) {
			new ObtenerNoticias().execute();
		}
		else{
			Intent intent = new Intent(getApplicationContext(),Sin_internet.class);
			startActivityForResult(intent, REQUEST_DESCRIPTION);
		}
		lstNoticias.setOnItemClickListener(this);
		this.titulo.setTypeface(this.fuenteTitulo);
		
	}
	
	private boolean hayInternet() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DESCRIPTION) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.noticias, menu);
		return true;
	}

	private void mostrarNoticias(JSONArray array) {
		AdaptadorNoticias adaptador = new AdaptadorNoticias(this, array,this.fuenteTitulo);
		this.lstNoticias.setAdapter(adaptador);
	}
	
	public void atras(View v){
		finish();
	}

	private class ObtenerNoticias extends AsyncTask<Void, Void, Boolean> {

		private JSONArray respuesta;

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd2.setMessage("Descargando noticias");
			pd2.setTitle("Espere");
			pd2.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_informacion.php?method=mostrar_anuncios","tipo_anuncio,anuncios"};
			this.respuesta = ws.conectar(parametros);
			if (respuesta.length()!=0) {
				respuesta2=respuesta;
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd2.dismiss();
			if (result) {
				mostrarNoticias(this.respuesta);
			} else {
				Toast.makeText(getApplicationContext(),"En este momento no hay noticias disponibles.",Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent( this , Detalle_noticia.class);
		try {
			JSONObject jsonObject = respuesta2.getJSONObject(arg2);
		    String title = jsonObject.getString("titulo");
		    String contenido = jsonObject.getString("contenido");
		    String fecha = jsonObject.getString("fecha");
		    String imagen = jsonObject.getString("imagen");
		    String url = jsonObject.getString("url");
		    String usuario_email = jsonObject.getString("usuario_email");
			intent.putExtra("titulo", title);
			intent.putExtra("contenido", contenido);
			intent.putExtra("fecha", fecha);
			intent.putExtra("imagen", imagen);
			intent.putExtra("url", url);
			intent.putExtra("usuario_email", usuario_email);
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Ocurrio un error al descargar las noticias intentalo de nuevo",
					Toast.LENGTH_LONG).show();
		}
	}

}
