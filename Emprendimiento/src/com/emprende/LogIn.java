package com.emprende;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.emprende.R;
//import com.facebook.FacebookException;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.model.GraphUser;
//import com.facebook.widget.LoginButton;
//import com.facebook.widget.LoginButton.OnErrorListener;
import com.google.android.gcm.GCMRegistrar;
import com.recursos.ConfiguracionGlobal;
import com.recursos.User;
import com.recursos.WebService;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;

public class LogIn extends Activity {

	public TextView lblTitulo,olvido;
	public ProgressDialog pd2;
	public EditText email, pass;
	public WebService ws;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String user = "nameKey";
	public static final String password = "passwordKey";
	public static final String registerId = "idRegister";
	public static final String idproyecto = "idProyecto";
	public static final String tituloproyecto = "tituloProyecto";
	public static final String descripcionproyecto = "descripcionProyecto";
	SharedPreferences sharedpreferences;
	private Typeface fuenteTitulo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in);

		// --inicializando variables
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		pd2 = new ProgressDialog(this);
		email = (EditText) findViewById(R.id.email);
		pass = (EditText) findViewById(R.id.passs);
		olvido = (TextView) findViewById(R.id.lblOlvidoSuContrasena);
		ws = new WebService();
		this.asignarFuentes();
		olvido.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://unidademprendimiento.tk/View/restablecer_pass.php"));
				startActivity(intent);
			}
		});

		// ---FACEBOOK----
//		LoginButton authButton = (LoginButton) findViewById(R.id.login_button);
//		authButton.setOnErrorListener(new OnErrorListener() {
//			@Override
//			public void onError(FacebookException error) {
//			}
//		});
//		authButton.setReadPermissions(Arrays.asList("basic_info", "email"));
//		authButton.setSessionStatusCallback(new Session.StatusCallback() {
//			@Override
//			public void call(Session session, SessionState state,
//					Exception exception) {
//
//				if (session.isOpened()) {
//					Request.newMeRequest(session,
//							new Request.GraphUserCallback() {
//								// callback after Graph API response with user
//								// object
//								@Override
//								public void onCompleted(GraphUser user,
//										Response response) {
//									if (user != null) {
//										Editor editor = sharedpreferences.edit();
//										editor.putBoolean("facebookLogIn", true);
//										editor.putString("nameKey", user
//												.getProperty("email")
//												.toString());
//										editor.commit();
//										new Login().execute("");
//									}
//								}
//							}).executeAsync();
//				} else if (session.isClosed()) {
//				}
//			}
//		});
		// ---FIN FACEBOOK---
	}
	
	
	public void registro(View v) {
		Intent intent = new Intent(getApplicationContext(),
				Registro.class);
		startActivity(intent);
	}
	

	public void log(View v) {

		if (hayInternet()) {
			if (!email.getText().toString().equals("")
					&& !pass.getText().toString().equals("")) {

				if (!email
						.getText()
						.toString()
						.matches(
								"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
										+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
					Toast.makeText(getApplicationContext(),
							"Email invalido revisalo", Toast.LENGTH_LONG)
							.show();
				} else {
					if (pass.getText().toString().length() > 7) {
						new Login().execute("");
					} else {
						Toast.makeText(getApplicationContext(),
								"La contraseña debe tener mas de 7 caracteres",
								Toast.LENGTH_LONG).show();
					}
				}
			} else {
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.toastCamposIncompletos),
						Toast.LENGTH_LONG).show();
			}

		} else {
			Intent intent = new Intent(getApplicationContext(),Sin_internet.class);
			startActivity(intent);
		}

	}

	@Override
	protected void onResume() {
		sharedpreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
		
//		if ((sharedpreferences.contains(user) && sharedpreferences.contains(password))
//				|| (sharedpreferences.getBoolean("facebookLogIn", false))) {
//			Intent i = new Intent(this, com.emprende.MenuGlobal.class);
//			startActivity(i);
//		}
//		
		if ((sharedpreferences.contains(user) && sharedpreferences.contains(password))) {
			Intent i = new Intent(this, com.emprende.MenuGlobal.class);
			startActivity(i);
		}
		super.onResume();
	}

	// ---FACEBOOK---
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Session.getActiveSession().onActivityResult(this, requestCode,resultCode, data);
	}

	// ---FIN FACEBOOK---

	public class Login extends AsyncTask<String, Void, Void> {

		int exito;

		@Override
		protected Void doInBackground(String... params) {

			final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
			if (regId.equals("")) {

				if (ConfiguracionGlobal.getSingletonObject().getUser() != null) {
					ConfiguracionGlobal.getSingletonObject().getUser()
							.setCorreo(email.getText().toString());
					ConfiguracionGlobal.getSingletonObject().getUser()
							.setPass(pass.getText().toString());
				} else {
					ConfiguracionGlobal.getSingletonObject().setUser(
							new User("", email.getText().toString(), pass
									.getText().toString(), "", "", "", "","",""));
				}
				registrarId();
			} else {
				ConfiguracionGlobal.getSingletonObject().setUser(
						new User("", email.getText().toString(), pass.getText()
								.toString(), "", "", regId, "","",""));
				Log.v("GCM", "Ya registrado");
			}

//			if (sharedpreferences.getBoolean("facebookLogIn", false)) {
//				String[] parametros = {
//						"url,http://citytaxiapp.com/emprendimiento/emprendimiento/Controller/fachada_usuario.php?metodo=8",
//						"correo," + sharedpreferences.getAll().get("nameKey").toString(),
//						"push," + GCMRegistrar.getRegistrationId(getApplicationContext()) 
//				};
//				JSONArray respuesta = ws.conectar(parametros);
//				try {
//					JSONObject res = respuesta.getJSONObject(0);
//					exito = res.getInt("respuesta");
//				} catch (Exception e) {
//					exito = 0;
//				}
//			} else {
			String push_id=" ";
			if(!GCMRegistrar.getRegistrationId(getApplicationContext()).toString().equals("") && !GCMRegistrar.getRegistrationId(getApplicationContext()).toString().equals("null")&& !GCMRegistrar.getRegistrationId(getApplicationContext()).toString().equals(" ")){
				push_id=GCMRegistrar.getRegistrationId(getApplicationContext()).toString();
			}
			
				String[] parametros = {
						"url,http://unidademprendimiento.tk/Controller/fachada_usuario.php?metodo=3",
						"correo," + email.getText().toString(),
						"contra," + pass.getText().toString(),
						"push," + push_id 
				};
				JSONArray respuesta = ws.conectar(parametros);
				try {
					JSONObject res = respuesta.getJSONObject(0);
					//exito = 1;
					exito = res.getInt("respuesta");
					ConfiguracionGlobal.getSingletonObject().getUser().setId_proyecto(res.getString("id_proyecto"));
					ConfiguracionGlobal.getSingletonObject().getUser().setTitulo_proyecto(res.getString("titulo"));
					ConfiguracionGlobal.getSingletonObject().getUser().setDescripcion_proyecto(res.getString("descripcion"));
					
				} catch (Exception e) {
					exito = 0;
				}
//			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			pd2.setMessage("La configuración inicial tardara algunos segundos");
			pd2.setTitle("Iniciando sesión");
			pd2.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (exito == 1) {
//				if (!sharedpreferences.getBoolean("facebookLogIn", false)) {
					
					Editor editor = sharedpreferences.edit();
					String u = ConfiguracionGlobal.getSingletonObject().getUser().getCorreo().toString();
					String p = ConfiguracionGlobal.getSingletonObject().getUser().getPass().toString();
					String k = ConfiguracionGlobal.getSingletonObject().getUser().getId().toString();
					String x = ConfiguracionGlobal.getSingletonObject().getUser().getId_proyecto().toString();
					String tituloProyecto = ConfiguracionGlobal.getSingletonObject().getUser().getTitulo_proyecto().toString();
					String descripcionProyecto = ConfiguracionGlobal.getSingletonObject().getUser().getDescripcion_proyecto().toString();

					editor.putString(user, u);
					editor.putString(password, p);
					editor.putString(registerId, k);
					editor.putString(idproyecto, x);
					editor.putString(tituloproyecto, tituloProyecto);
					editor.putString(descripcionproyecto, descripcionProyecto);
					editor.commit();

					email.setText("");
					pass.setText("");
//				}

				Intent intent = new Intent(getApplicationContext(),
						MenuGlobal.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "malos datos",Toast.LENGTH_LONG).show();
			}
			pd2.dismiss();
		}
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

	public void asignarFuentes() {
		lblTitulo = (TextView) findViewById(R.id.txtSaludo);
		lblTitulo.setTypeface(this.fuenteTitulo);
		email.setTypeface(this.fuenteTitulo);
		pass.setTypeface(this.fuenteTitulo);
		olvido.setTypeface(this.fuenteTitulo);
	}

	public void registrarId() {
		GCMRegistrar.register(getApplicationContext(), "338813978023");
		Log.v("GCM", "Registrado");
		ConfiguracionGlobal.getSingletonObject().getUser()
				.setId(GCMRegistrar.getRegistrationId(getApplicationContext()));
		SystemClock.sleep(4000);
		ConfiguracionGlobal.getSingletonObject().getUser()
				.setId(GCMRegistrar.getRegistrationId(getApplicationContext()));

	}

}