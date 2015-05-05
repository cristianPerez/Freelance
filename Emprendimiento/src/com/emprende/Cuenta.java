package com.emprende;

import com.emprende.R;
//import com.facebook.Session;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Cuenta extends Activity {

	public SharedPreferences sharedpreferences;
	private Typeface fuenteTitulo;
	private TextView titulo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cuenta);
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES, Context.MODE_PRIVATE);
		this.titulo = (TextView)findViewById(R.id.tituloapp);
		this.titulo.setTypeface(this.fuenteTitulo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cuenta, menu);
		return true;
	}
	
	public void logout(View view) {
//		if (sharedpreferences.getBoolean("facebookLogIn", false)) {
//			Session.getActiveSession().closeAndClearTokenInformation();
//		}
		Editor editor = sharedpreferences.edit();
		editor.clear();
		editor.commit();

		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}


}
