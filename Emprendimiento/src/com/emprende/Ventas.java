package com.emprende;

import org.json.JSONArray;
import com.emprende.R;
import com.recursos.*;
import android.app.*;
import android.content.*;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Ventas extends Activity implements OnItemClickListener {

	private WebService ws;
	private ListView lstVentas;
	private ProgressDialog pd2;
	private JSONArray respuesta2;
	private Typeface fuenteTitulo;
	private TextView titulo;
	static final int REQUEST_DESCRIPTION = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ventas);
		this.lstVentas = (ListView) findViewById(R.id.lista_ventas);
		this.titulo = (TextView) findViewById(R.id.tituloapp);
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.ws = new WebService();
		pd2 = new ProgressDialog(this);
		pd2.setCancelable(false);
		
		if (hayInternet()) {
			new ObtenerVentas().execute();
		}
		else{
			Intent intent = new Intent(getApplicationContext(),Sin_internet.class);
			startActivity(intent);
		}
		lstVentas.setOnItemClickListener(this);
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
	
	private void mostrarVentas(JSONArray array) {
		AdaptadorVentas adaptador = new AdaptadorVentas(this, array,this.fuenteTitulo);
		this.lstVentas.setAdapter(adaptador);
	}
	
	public void Vender(View v){
		
		if (hayInternet()) {
			Intent intent = new Intent(this, Venta.class);
			startActivityForResult(intent, REQUEST_DESCRIPTION);
		}
		else{
			Intent intent = new Intent(getApplicationContext(),Sin_internet.class);
			startActivity(intent);
		}
		
		
	}
	
	
	public void atras(View v){
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DESCRIPTION) {
			if (resultCode == RESULT_OK) {
				new ObtenerVentas().execute();
			}
		}
	}
	
	
	private class ObtenerVentas extends AsyncTask<Void, Void, Boolean> {

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
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_colega.php?method=mostrar_recursos"};
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
				mostrarVentas(this.respuesta);
			} else {
				Toast.makeText(getApplicationContext(),"En este momento no hay noticias disponibles.",Toast.LENGTH_LONG).show();
			}
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent( this , Detalle_venta.class);
		try {
			JSONArray jsonObject = respuesta2.getJSONArray(arg2);
			String id_publicacion = jsonObject.getString(0);
			String email_publicante = jsonObject.getString(1);
			String resumen_publicacion = jsonObject.getString(2);
			String imagen_publicacion = jsonObject.getString(3);
			String nombre_publicante = jsonObject.getString(4);
			String publicacion = jsonObject.getString(5);
			
			intent.putExtra("id",id_publicacion);
			intent.putExtra("email_publicante", email_publicante);
			intent.putExtra("resumen_publicacion", resumen_publicacion);
			intent.putExtra("imagen_publicacion", imagen_publicacion);
			intent.putExtra("nombre_publicante", nombre_publicante);
			intent.putExtra("publicacion", publicacion);
			startActivityForResult(intent, REQUEST_DESCRIPTION);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Ocurrio un error al descargar las ventas intentalo de nuevo",
					Toast.LENGTH_LONG).show();
		}
	}


	
}
