package com.emprende;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.Toast;

public class Respuesta extends Activity {
	
	public SharedPreferences sharedpreferences;
	private String nombreCompleto;
	private ProgressDialog dialog;
	private WebService ws;
	private String idPreg;
	private Button btnResponder;
	private Typeface fuenteTitulo;
	private EditText txtRespuesta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.respuesta);
		
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		
		WindowManager.LayoutParams params = getWindow().getAttributes(); 
		params.width = width-80;    
		this.getWindow().setAttributes(params); 
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.txtRespuesta = (EditText) findViewById(R.id.txtRespuesta);
		this.btnResponder = (Button) findViewById(R.id.btnResponder);
		this.btnResponder.setTypeface(fuenteTitulo);
		this.txtRespuesta.setTypeface(fuenteTitulo);
		
		this.idPreg = getIntent().getStringExtra("idPreg");
		this.obtenerNombre();
		this.ws = new WebService();
		this.dialog = new ProgressDialog(Respuesta.this);
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
	
	public void responder(View v){
		
		String respuesta = txtRespuesta.getText().toString();
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		String USER_ID = sharedpreferences.getString("nameKey", null);
		if(respuesta.equals("")){
			Toast.makeText(this, "Debes proporcionar una respuesta.", Toast.LENGTH_LONG).show();
		}
		else{
			new Conexion().execute(USER_ID,respuesta);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.respuesta, menu);
		return true;
	}

	private class Conexion extends AsyncTask<String, Void, Boolean> {

		private JSONArray respuesta;
		private String respuestaUsuario;
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
			this.respuestaUsuario = arg0[1];
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_blog.php?method=ingresar_respuesta","id_pregunta,"+idPreg,"respuesta,"+this.respuestaUsuario,"usuario,"+this.USER_ID};
			respuesta = ws.conectar(parametros);
			try {
				if(respuesta.getJSONObject(0).getInt("respuesta") == 1){
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
			
			if(result){
				dialog.dismiss();
				Intent intent = new Intent();
				intent.putExtra("USUARIO", nombreCompleto);
				intent.putExtra("RESPUESTA", this.respuestaUsuario);
				setResult(RESULT_OK, intent);
				finish();
			}
			else{
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "No se ha podido realizar la respuesta", Toast.LENGTH_LONG).show();
			}
		}
	}
}
