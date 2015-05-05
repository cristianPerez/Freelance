package com.emprende;
import org.json.JSONArray;
import org.json.JSONObject;

import com.recursos.ConfiguracionGlobal;
import com.recursos.User;
import com.recursos.WebService;
import com.emprende.*;

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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Registro extends Activity implements OnItemSelectedListener {
	public Button registrar;
	public TextView nombre,email,contrasena,telefono,titulo;
	public ProgressDialog pd;
	public WebService ws;
	public String perfil;
	private Typeface fuenteTitulo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registro);
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		pd = new ProgressDialog(this);
		ws = new WebService();
		this.asignarFuentes();
		
		registrar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(hayInternet()){
				if(!nombre.getText().toString().equals("")&&!email.getText().toString().equals("")&&!contrasena.getText().toString().equals("")&&!telefono.getText().toString().equals("")){
					//ConfiguracionGlobal.getSingletonObject().setUser(new User(nombre.getText().toString(), email.getText().toString(), contrasena.getText().toString(), "Emprendedor",telefono.getText().toString(),""));	
					 if (!email.getText().toString().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+
					 "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
						Toast.makeText(getApplicationContext(),"Email invalido revisalo",Toast.LENGTH_LONG).show();
					 }
					 else{
						 if(contrasena.getText().toString().length()>7){
							 new Registrando().execute("");
						 }
						 else{
							 Toast.makeText(getApplicationContext(),"La contraseña debe tener mas de 7 caracteres",Toast.LENGTH_LONG).show();
						 }
					 }
				}
				else{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastCamposIncompletos),Toast.LENGTH_LONG).show();
				}
			}
				
				else{
					Intent intent = new Intent(getApplicationContext(),Sin_internet.class);
					startActivity(intent);
				}
				
			}
			
		});
	}
	
	public class Registrando extends AsyncTask <String,Void,Void>{
		
		int exito;
		
		 @Override
			protected Void doInBackground(String... params){
			 String[] parametros = {"url,http://unidademprendimiento.tk/Controller/fachada_usuario.php?metodo=8","nombre,"+nombre.getText().toString(),"correo,"+email.getText().toString(),"telefono,"+telefono.getText().toString(),"clave,"+contrasena.getText().toString()};
				JSONArray respuesta = ws.conectar(parametros);
				try {
					JSONObject res = respuesta.getJSONObject(0);
					exito = res.getInt("respuesta");
				}catch (Exception e){
					exito=0;
					Toast.makeText(getApplicationContext(),"Ocurrio un problema vuelve a intentarlo",Toast.LENGTH_LONG).show();
				}
				return null;
			}
		
		 @Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pd.setMessage("Registrando");
	  			pd.setTitle("Espere");
	  			pd.show();
				super.onPreExecute();
			}
			
		 
			@Override
			protected void onPostExecute(Void result){
				super.onPostExecute(result);
				if(exito==1){
					Toast.makeText(getApplicationContext(),"Registrado correctamente Ingresa",Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(getApplicationContext(),"El correo ya esta registrado",Toast.LENGTH_LONG).show();
				}
				pd.dismiss();
				finish();
			}
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
			 perfil = arg0.getItemAtPosition(arg2).toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
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
	public void asignarFuentes(){
		registrar = (Button)findViewById(R.id.btnRegistrar);
		nombre = (TextView)findViewById(R.id.etNombre);
		email = (TextView)findViewById(R.id.etCorreo);
		contrasena = (TextView)findViewById(R.id.etPassword);
		telefono = (TextView)findViewById(R.id.etTelefono);
		titulo = (TextView)findViewById(R.id.tituloapp);
		registrar.setTypeface(this.fuenteTitulo);
		nombre.setTypeface(this.fuenteTitulo);
		contrasena.setTypeface(this.fuenteTitulo);
		email.setTypeface(this.fuenteTitulo);
		telefono.setTypeface(this.fuenteTitulo);
		titulo.setTypeface(this.fuenteTitulo);
	}

}
