package com.emprende;

import org.json.JSONArray;
import com.emprende.R;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphUser;
import com.recursos.WebService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Venta extends Activity {
	
	public SharedPreferences sharedpreferences;
	private String email_publicante;
	private ProgressDialog dialog;
	private WebService ws;
	private TextView venta;
	private Typeface fuenteTitulo;
	private EditText txtVenta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.venta);
		this.obtenerNombre();
		this.ws = new WebService();
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.dialog = new ProgressDialog(Venta.this);
		this.venta = (TextView) findViewById(R.id.tituloapp);
		this.txtVenta = (EditText) findViewById(R.id.txtVenta);
		this.venta.setTypeface(fuenteTitulo);
		this.txtVenta.setTypeface(fuenteTitulo);
	}

	
	private void obtenerNombre() {
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
//		if (sharedpreferences.getBoolean("facebookLogIn", false)) {
//			Session session = Session.getActiveSession();
//			if (session.isOpened()) {
//				Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//					// callback after Graph API response with user object
//					@Override
//					public void onCompleted(GraphUser user, Response response) {
//						if (user != null) {
//							email_publicante = user.getProperty("email").toString();
//						}
//					}
//				}).executeAsync();
//			}
//		} else {
			this.email_publicante = sharedpreferences.getAll().get("nameKey").toString();
//		}
	}
	
	public void publicar_venta(View v){
		
		String venta = txtVenta.getText().toString();
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		String USER_ID = this.email_publicante;
		if(venta.equals("")){
			Toast.makeText(this, "Complete el campo", Toast.LENGTH_LONG).show();
		}
		else{
			new Conexion().execute(USER_ID, venta);
		}
	}
	
	private class Conexion extends AsyncTask<String, Void, Boolean> {

		private JSONArray respuesta;
		private String USER_ID;
		private String venta;
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Realizando Venta");
			dialog.setMessage("Espere un momento...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			this.USER_ID = arg0[0];
			this.venta = arg0[1];
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_colega.php?method=ingresar_venta","descripcion,"+this.venta,"usuario,"+this.USER_ID};
			respuesta = ws.conectar(parametros);
			if(respuesta.length() > 0){
				return true;
			}
			else{
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if(result){
				
				dialog.dismiss();
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "No se ha podido realizar la venta", Toast.LENGTH_LONG).show();
			}
		}
	}
}
