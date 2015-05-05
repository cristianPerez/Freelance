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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Aporte extends Activity {
	
	public SharedPreferences sharedpreferences;
	private String nombreCompleto;
	private ProgressDialog dialog;
	private WebService ws;
	private Typeface fuenteTitulo;
	private EditText txtTitulo;
	private EditText txtDescripcion;
	private TextView lblTiuView;
	private Button aportar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aporte);
		
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		
		WindowManager.LayoutParams params = getWindow().getAttributes(); 
		params.width = width-80;    
		this.getWindow().setAttributes(params); 
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.txtTitulo = (EditText) findViewById(R.id.txtTitulo);
		this.txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
		this.lblTiuView = (TextView) findViewById(R.id.lblTituloViewAporte);
		this.aportar = (Button) findViewById(R.id.btnAportar);
		this.txtTitulo.setTypeface(fuenteTitulo);
		this.txtDescripcion.setTypeface(fuenteTitulo);
		this.lblTiuView.setTypeface(fuenteTitulo);
		this.aportar.setTypeface(fuenteTitulo);
		this.obtenerNombre();
		this.ws = new WebService();
		this.dialog = new ProgressDialog(Aporte.this);
	}
	
	private void obtenerNombre() {
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,
				Context.MODE_PRIVATE);
//		if (sharedpreferences.getBoolean("facebookLogIn", false)) {
//			Session session = Session.getActiveSession();
//			if (session.isOpened()) {
//				Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//					// callback after Graph API response with user object
//					@Override
//					public void onCompleted(GraphUser user, Response response) {
//						if (user != null) {
//							nombreCompleto = user.getFirstName() + " "
//									+ user.getLastName();
//						}
//					}
//				}).executeAsync();
//			}
//		} else {
			this.nombreCompleto = sharedpreferences.getAll().get("nameKey")
					.toString();
//		}
	}
	
	public void aporte(View v){
		
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		String USER_ID = sharedpreferences.getString("nameKey", null);
		
		String titulo = txtTitulo.getText().toString();
		String descripcion = txtDescripcion.getText().toString();
		
		if(titulo.equals("") || descripcion.equals("")){
			Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
		}
		else{
			new Conexion().execute(USER_ID,titulo,descripcion);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aporte, menu);
		return true;
	}
	
	private class Conexion extends AsyncTask<String, Void, Boolean> {

		private JSONArray respuesta;
		private String titulo;
		private String descripcion;
		private String USER_ID;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Realizando comentario");
			dialog.setMessage("Espere un momento...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			this.USER_ID = arg0[0];
			this.titulo = arg0[1];
			this.descripcion = arg0[2];
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_blog.php?method=guardar_aporte","contenido,"+descripcion,"titulo,"+titulo,"usuario,"+USER_ID};
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
			dialog.dismiss();
			if(result){
				Intent intent = new Intent();
				intent.putExtra("USUARIO", nombreCompleto);
				intent.putExtra("TITULO", this.titulo);
				intent.putExtra("DESCRIPCION", this.descripcion);
				setResult(RESULT_OK, intent);
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "No se ha podido realizar el comentario", Toast.LENGTH_LONG).show();
			}
		}
	}

}
