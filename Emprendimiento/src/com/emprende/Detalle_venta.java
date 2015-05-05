package com.emprende;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.emprende.R;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphUser;
import com.recursos.WebService;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Detalle_venta extends Activity {

	public String id_publicacion,email_publicante,resumen_publicacion,imagen_publicacion,nombre_publicante,publicacion,email_interesado;
	public TextView email_vendedor,resumen_venta,publicacion_venta,titulo;
	//public ImageView txt_v_imagen_Noticia;
	private Typeface fuenteTitulo;
	private ProgressDialog dialog;
	private WebService ws;
	public SharedPreferences sharedpreferences;
	private ImageButton sendEmail;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detalle_venta);
		Intent intent = getIntent();
		this.obtenerNombre();
		id_publicacion = intent.getStringExtra("id");
		email_publicante = intent.getStringExtra("email_publicante");
		resumen_publicacion = intent.getStringExtra("resumen_publicacion");
		imagen_publicacion = intent.getStringExtra("imagen_publicacion");
		nombre_publicante = intent.getStringExtra("nombre_publicante");
		publicacion = intent.getStringExtra("publicacion");
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.email_vendedor=(TextView) findViewById(R.id.email_vendedor);
		this.email_vendedor.setTypeface(fuenteTitulo);
		this.resumen_venta=(TextView) findViewById(R.id.tituVenta);
		this.resumen_venta.setTypeface(fuenteTitulo);
		this.publicacion_venta=(TextView) findViewById(R.id.descVenta);
		this.titulo=(TextView) findViewById(R.id.tituloapp);
		this.titulo.setTypeface(fuenteTitulo);
		this.publicacion_venta.setTypeface(fuenteTitulo);
		this.email_vendedor.setText(email_publicante);
		this.resumen_venta.setText(resumen_publicacion);
		this.publicacion_venta.setText(publicacion);
		this.ws = new WebService();
		this.dialog = new ProgressDialog(Detalle_venta.this);
		this.sendEmail = (ImageButton)findViewById(R.id.sendEmail);
		Drawable Eliminar = this.getResources().getDrawable(R.drawable.eliminar_venta);
				
		if(email_interesado.equals(email_publicante)){
			titulo.setText("Eliminar venta");
			sendEmail.setImageDrawable(Eliminar);
		}
		
	}
	
	public void enviarEmail(View v){
	if(hayInternet()){
		new EnviarEmail().execute(email_publicante,email_interesado);
	} else {
		Intent intent = new Intent(getApplicationContext(),
				Sin_internet.class);
		startActivity(intent);
	}
	}
	
	public void atras(View v){
		finish();
	}
	
	private void obtenerNombre() {
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,
				Context.MODE_PRIVATE);
//		if (sharedpreferences.getBoolean("facebookLogIn", false)) {
//			Session session = Session.getActiveSession();
//			if (session.isOpened()) {
//				Request.newMeRequest(session, new Request.GraphUserCallback() {
//					@Override
//					public void onCompleted(GraphUser user, Response response) {
//						if (user != null) {
//							email_interesado = user.getProperty("email").toString();
//						}
//					}
//				}).executeAsync();
//			}
//		} else {
			this.email_interesado = sharedpreferences.getAll().get("nameKey").toString();
//		}
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
	
	private class EnviarEmail extends AsyncTask<String, Void, Boolean> {

		private JSONArray respuesta;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		    
			dialog.setTitle("Actualizando datos");
			dialog.setMessage("Espere un momento...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0){
			
			String publicante=arg0[0];
			String interesado=arg0[1];
			
			if(!publicante.equals(interesado)){
				String[]	parametros = {"url,http://unidademprendimiento.tk/Controller/facade_colega.php?method=contactar_recurso","correo,"+email_publicante,"correo_usuario,"+email_interesado,"imagen,images/perfi.jpg","nombre,"+email_interesado};
				respuesta = ws.conectar(parametros);
			}
			else{
				String[]	parametros2 = {"url,http://unidademprendimiento.tk/Controller/facade_colega.php?method=eliminar_venta","id_venta,"+id_publicacion};
				respuesta = ws.conectar(parametros2);
			}
			
			JSONObject res;
			Boolean oto=false;
			try {
				res = respuesta.getJSONObject(0);
				if(res.getInt("respuesta")==1){
					oto=true;
				}
				else{
					oto=false;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return oto;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			
			if(result){
				
				
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "No se ha podido contactar al proveedor", Toast.LENGTH_LONG).show();
			}
			
			dialog.dismiss();
		}
	}

}
