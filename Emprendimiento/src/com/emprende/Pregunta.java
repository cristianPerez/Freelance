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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Pregunta extends Activity {
	
	public SharedPreferences sharedpreferences;
	private String nombreCompleto;
	private Spinner spnTipo;
	private ProgressDialog dialog;
	private WebService ws;
	private Typeface fuenteTitulo;
	private EditText txtPregunta;
	private Button btnPreguntar;
    private TextView lblTituloViewPregunta;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pregunta);
		
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		
		WindowManager.LayoutParams params = getWindow().getAttributes(); 
		params.width = width-80;    
		this.getWindow().setAttributes(params); 
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.obtenerNombre();
		this.txtPregunta = (EditText) findViewById(R.id.txtPregunta);
		this.lblTituloViewPregunta = (TextView) findViewById(R.id.lblTituloViewPregunta);
		this.txtPregunta.setTypeface(fuenteTitulo);
		this.lblTituloViewPregunta.setTypeface(fuenteTitulo);
		this.btnPreguntar = (Button) findViewById(R.id.btnPreguntar);
		this.btnPreguntar.setTypeface(fuenteTitulo);
		this.spnTipo = (Spinner) findViewById(R.id.spnTipo);
		this.llenarSpiner(spnTipo);
		this.ws = new WebService();
		this.dialog = new ProgressDialog(Pregunta.this);
	}
	
	private void llenarSpiner(Spinner sp) {
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.tipo_pregunta, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
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
	
	public void preguntar(View v){
		String pregunta = txtPregunta.getText().toString();
		String tipoPregunta = this.spnTipo.getSelectedItem().toString();
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		String USER_ID = sharedpreferences.getString("nameKey", null);
		if(pregunta.equals("")){
			Toast.makeText(this, "No se ha realizado ninguna pregunta", Toast.LENGTH_LONG).show();
		}
		else{
			new Conexion().execute(USER_ID, pregunta, tipoPregunta);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pregunta, menu);
		return true;
	}
	
	private class Conexion extends AsyncTask<String, Void, Boolean> {

		private JSONArray respuesta;
		private String USER_ID;
		private String pregunta;
		private String tipoPregunta;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Realizando pregunta");
			dialog.setMessage("Espere un momento...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			this.USER_ID = arg0[0];
			this.pregunta = arg0[1];
			this.tipoPregunta = arg0[2];
			String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_blog.php?method=guardar_pregunta","pregunta,"+this.pregunta,"categoria,"+this.tipoPregunta,"usuario,"+this.USER_ID};
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
				setResult(RESULT_OK, intent);
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "No se ha podido realizar el comentario", Toast.LENGTH_LONG).show();
			}
		}
	}
}
