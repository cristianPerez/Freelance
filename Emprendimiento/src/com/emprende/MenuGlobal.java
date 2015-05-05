package com.emprende;

import org.json.JSONArray;
import org.json.JSONObject;

import com.emprende.R;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.recursos.ConfiguracionGlobal;
import com.recursos.User;
import com.recursos.WebService;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuGlobal extends Activity {

	public TextView email;
	public SharedPreferences sharedpreferences;
	private Typeface fuenteTitulo;
	private TextView titulo;
	private RotateAnimation animacion;
	static final int REQUEST_DESCRIPTION = 1;
	static final int REQUEST_DESCRIPTION2 = 2;
	public WebService ws;
	private Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES, Context.MODE_PRIVATE);
		Log.v("entro", "entro");
		String idProyecto = sharedpreferences.getString("idProyecto", null);
		if(idProyecto != null && !idProyecto.equals("") && !idProyecto.equals("null")&& !idProyecto.equals(" ")){
			setContentView(R.layout.activity_menu_global);
		}
		else{
			
			setContentView(R.layout.activity_menu_global_dos);
		}
			
		
		ws = new WebService();
		dialog = new Dialog(MenuGlobal.this);
		this.titulo = (TextView) findViewById(R.id.tituloapp);
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.titulo.setTypeface(this.fuenteTitulo);
		this.email=(TextView)findViewById(R.id.emailSession);
		this.email.setTypeface(this.fuenteTitulo);
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES, Context.MODE_PRIVATE);
		
//		if(sharedpreferences.getBoolean("facebookLogIn", false)){
//			Session session = Session.getActiveSession();
//			if(session.isOpened()){
//				Request.newMeRequest(session, new Request.GraphUserCallback(){
//
//					  // callback after Graph API response with user object
//					  @Override
//					  public void onCompleted(GraphUser user, Response response){
//					    if (user != null) {
//					    	email.setText(user.getFirstName());
//					    }
//					  }
//					}).executeAsync();
//			}	
//		}
//		else{
				email.setText(sharedpreferences.getAll().get("nameKey").toString());
//		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_global, menu);
		return true;
	}
	
	public void noticias(View v){
		Intent intent = new Intent(this, Noticias.class);
		startActivity(intent);
	}

	public void cuenta(View view) {
		Intent intent = new Intent(getApplicationContext(),Cuenta.class);
		startActivityForResult(intent, REQUEST_DESCRIPTION);
	}

	public void informacion(View v) {
		Intent intent = new Intent(getApplicationContext(), Informacion.class);
		startActivity(intent);
	}
	
	public void canvas(View v){
		if(this.hayInternet()){
			Intent intent = new Intent(this, Canvas.class);
			startActivity(intent);
		}
		else{
			Intent intent = new Intent(getApplicationContext(),Sin_internet.class);
			startActivity(intent);
		}
	}
	
	public void preguntasAportes(View v){
		if(this.hayInternet()){
		Intent intent = new Intent(this, PreguntasAportes.class);
		startActivity(intent);
		}
		else{
			Intent intent = new Intent(getApplicationContext(),Sin_internet.class);
			startActivity(intent);
		}
	}
	
	public void ventas(View v){
		Intent intent = new Intent(this, Ventas.class);
		startActivity(intent);
	}
	
	public void actualizar(View v){
		if (hayInternet()) {
			ImageView iv = (ImageView) findViewById(R.id.btnActualizar);
			animacion = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			animacion.setInterpolator(new LinearInterpolator());
			animacion.setRepeatCount(Animation.INFINITE);
			animacion.setDuration(1000);
			iv.setAnimation(animacion);
			iv.startAnimation(animacion);
			new ActualizarProyecto().execute();
		}
		
	}
	
	public void proyecto(View v){
			if(hayInternet()){
				Intent intent = new Intent(getApplicationContext(), Proyecto.class);
				startActivityForResult(intent, REQUEST_DESCRIPTION2);
			}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DESCRIPTION ) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
			if (requestCode == REQUEST_DESCRIPTION2) {		
			  if (resultCode == 10) {
				Intent intent = getIntent();
			    startActivity(intent);
			    finish();
			}
			}
	}
	
	public void facebook(View view){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/emprendimientoucaldas?fref=ts"));
		startActivity(intent);
	}
	
	public void twitter(View view){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/emprendeucaldas"));
		startActivity(intent);
	}
	
	public void gplus(View view){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/emprendimientoucaldas?fref=ts"));
		startActivity(intent);
	}
	
	public void instagram(View view){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/emprendeucaldas"));
		startActivity(intent);
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
	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
	     
		 /*if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	         return true;
	     }
		  */
		 moveTaskToBack(true);
		 return super.onKeyDown(keyCode, event);
	 }
	
	 public class ActualizarProyecto extends AsyncTask<String, Void, Void> {

			int exito;
			
			@Override
			protected void onPreExecute() {
				dialog.setTitle("Actualizando...");
				dialog.setCancelable(false);
		        dialog.show();
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(String... params) {

//				if (sharedpreferences.getBoolean("facebookLogIn", false)) {
//					String[] parametros = {
//							"url,http://citytaxiapp.com/emprendimiento/emprendimiento/Controller/fachada_usuario.php?metodo=8",
//							"correo," + sharedpreferences.getAll().get("nameKey").toString(),
//							"push," + GCMRegistrar.getRegistrationId(getApplicationContext()) 
//					};
//					JSONArray respuesta = ws.conectar(parametros);
//					try {
//						JSONObject res = respuesta.getJSONObject(0);
//						exito = res.getInt("respuesta");
//					} catch (Exception e) {
//						exito = 0;
//					}
//				} else {
					String[] parametros = {
							"url,http://unidademprendimiento.tk/Controller/fachada_usuario.php?metodo=3",
							"correo," + sharedpreferences.getString("nameKey", null),
							"contra," + sharedpreferences.getString("passwordKey", null),
							"push," + GCMRegistrar.getRegistrationId(getApplicationContext()) 
					};
					JSONArray respuesta = ws.conectar(parametros);
					try {
						JSONObject res = respuesta.getJSONObject(0);
						if(res.getString("id_proyecto")!=null && !res.getString("id_proyecto").equals("null") && !res.getString("id_proyecto").equals(" ")&&!res.getString("id_proyecto").equals("")){
							Editor editor = sharedpreferences.edit();
							editor.putString("idProyecto", res.getString("id_proyecto"));
							editor.putString("tituloProyecto", res.getString("titulo"));
							editor.putString("descripcionProyecto", res.getString("descripcion"));
							editor.commit();
							exito = 1;
//							ConfiguracionGlobal.getSingletonObject().getUser().setId_proyecto(res.getString("id_proyecto"));
						}
						else{
							exito = 0;
						}
						
					} catch (Exception e) {
						exito = 0;
					}
//				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				animacion.cancel();
				dialog.dismiss();
				if (exito == 1) {
					Intent intent = new Intent(getApplicationContext(), MenuGlobal.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "No has creado ningun proyecto", Toast.LENGTH_SHORT).show();
				}
			}
		}
}
