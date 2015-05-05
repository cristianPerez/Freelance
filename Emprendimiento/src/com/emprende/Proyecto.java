package com.emprende;

import org.json.JSONArray;
import org.json.JSONObject;

import com.emprende.R;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphUser;
import com.recursos.*;

import android.R.bool;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Proyecto extends Activity{

	public SharedPreferences sharedpreferences;
	private EditText tituloProyecto,descripcionProyecto;
	private TextView tituloapp;
	private Typeface fuenteTitulo;
//	private CheckBox publico,privado;
	private String TipoProyecto,email_publicante,idProject;
	private ProgressDialog dialog;
	private WebService ws;
	public static final String idproyecto = "idProyecto";
	private ImageButton btn_proyecto;
	private Boolean hayProyecto = false;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proyecto1);
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		this.btn_proyecto = (ImageButton)findViewById(R.id.btn_proyecto);
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.tituloProyecto = (EditText) findViewById(R.id.titulo_poyecto);
		this.descripcionProyecto = (EditText) findViewById(R.id.descripcion_proyecto);
		this.tituloapp = (TextView) findViewById(R.id.tituloapp);
//		this.tipo = (TextView) findViewById(R.id.tipo);
		this.tituloProyecto.setTypeface(fuenteTitulo);
//		this.tipo.setTypeface(fuenteTitulo);
		this.descripcionProyecto.setTypeface(fuenteTitulo);
		this.tituloapp.setTypeface(fuenteTitulo);
//		this.publico = (CheckBox)findViewById(R.id.publico);
//		this.privado = (CheckBox)findViewById(R.id.privado);
//		this.publico.setOnCheckedChangeListener(this);
//		this.privado.setOnCheckedChangeListener(this);
//		this.publico.setChecked(true);
//		this.privado.setChecked(false);
//		this.publico.setTypeface(fuenteTitulo);
//		this.privado.setTypeface(fuenteTitulo);
		this.TipoProyecto="publico";
		this.ws = new WebService();
		this.dialog = new ProgressDialog(this);
		
//		this.obtenerNombre();
		this.email_publicante = sharedpreferences.getAll().get("nameKey").toString();
		if(!sharedpreferences.getAll().get("idProyecto").toString().equals(" ") &&
				!sharedpreferences.getAll().get("idProyecto").toString().equals("") &&
				!sharedpreferences.getAll().get("idProyecto").toString().equals("null") &&
				sharedpreferences.getAll().get("idProyecto").toString()!=null){
			
			this.btn_proyecto.setBackgroundDrawable(getResources().getDrawable(R.drawable.actualizar_proyecto));
			this.hayProyecto = true;
		}
		
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		String id__proyecto=sharedpreferences.getAll().get("idProyecto").toString();
		if(id__proyecto.equals("null") || id__proyecto==null || id__proyecto.equals("") ){
			this.tituloProyecto.setHint("Ingrese el titulo del proyecto");
			this.descripcionProyecto.setHint("Ingrese la descripción de su proyecto.");
		}
		else{
			String tit = sharedpreferences.getString("tituloProyecto", "null");
			this.tituloProyecto.setText(tit);
			this.descripcionProyecto.setText(sharedpreferences.getAll().get("descripcionProyecto").toString());
		}
	}
	
	public void atras(View v){
		finish();
	}
	
//	private void obtenerNombre() {
//		
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
//			this.email_publicante = sharedpreferences.getAll().get("nameKey").toString();
//		}
//	}
	
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.noticias, menu);
		return true;
	}

//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		// TODO Auto-generated method stub
//		if(buttonView.getId()== R.id.publico){
//			if(isChecked){
//				this.privado.setChecked(false);
//				this.TipoProyecto = "publico";
//			}
//		}
//		else if(buttonView.getId()== R.id.privado){
//			if(isChecked){
//				this.publico.setChecked(false);
//				this.TipoProyecto = "privado";
//			}
//		}
//		
//	}
	
	public void publicarProyecto(View v){
		
		if(!this.tituloProyecto.getText().toString().equals("") && !this.descripcionProyecto.getText().toString().equals("")){
			if(!hayProyecto){
				new Conexion().execute(this.tituloProyecto.getText().toString(),this.descripcionProyecto.getText().toString(),"1");
			}
			else{
				new Conexion().execute(this.tituloProyecto.getText().toString(),this.descripcionProyecto.getText().toString(),"2");
			}
		}
		
		else{
			Toast.makeText(getApplicationContext(), "Campos incompletos", Toast.LENGTH_LONG).show();
		}
	}
	
	private class Conexion extends AsyncTask<String, Void, Boolean> {

		private JSONArray respuesta;
		private String tituloProyecto;
		private String descripcionProyecto;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Creando proyecto");
			dialog.setMessage("Espere un momento...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0){
			this.tituloProyecto = arg0[0];
			this.descripcionProyecto = arg0[1];
			if(arg0[2].equals("1")){
				String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_canvas.php?method=guardar_proyecto","descripcion,"+this.descripcionProyecto,"correo,"+email_publicante,"titulo,"+this.tituloProyecto,"tipo_proyecto,"+TipoProyecto};
				respuesta = ws.conectar(parametros);
			}
			else{
				String[] parametros = {"url,http://unidademprendimiento.tk/Controller/facade_canvas.php?method=actualizar_proyecto","descripcion,"+this.descripcionProyecto,"correo,"+email_publicante,"titulo,"+this.tituloProyecto,"tipo_proyecto,"+TipoProyecto};
				respuesta = ws.conectar(parametros);
			}
			
			try {
				
				JSONObject res = respuesta.getJSONObject(0);
				if(res.getInt("respuesta") ==1){
					if(!hayProyecto){
						idProject= res.getString("id_proyecto").toString();
					}
					//ConfiguracionGlobal.getSingletonObject().getUser().setId_proyecto(id);
					return true;
				}
				else if(res.getInt("respuesta") ==3 || res.getInt("respuesta") == 0 ){
					return false;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			dialog.dismiss();
			
			if(result){
				if(!hayProyecto){
					hayProyecto=true;
					Editor editor = sharedpreferences.edit();
					//String x = ConfiguracionGlobal.getSingletonObject().getUser().getId_proyecto();
					editor.putString(idproyecto, idProject);
					editor.putString("tituloProyecto", tituloProyecto);
					editor.putString("descripcionProyecto", descripcionProyecto);
					editor.commit();
					Toast.makeText(getApplicationContext(), "Proyecto almacenado", Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					setResult(10, intent);
					finish();
				}
				else{
					Editor editor = sharedpreferences.edit();
					editor.putString("tituloProyecto", tituloProyecto);
					editor.putString("descripcionProyecto", descripcionProyecto);
					editor.commit();
					Toast.makeText(getApplicationContext(), "Proyecto modificado", Toast.LENGTH_LONG).show();
				}
				
			}
			else{
				Toast.makeText(getApplicationContext(), "Fallo al almacenar el proyecto", Toast.LENGTH_LONG).show();
			}
		}
	}

}
