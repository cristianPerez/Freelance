package com.emprende;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.emprende.R;

public class Informacion extends Activity {

	public TextView tituloap,descUnidad,equipoEmprendedor,descJuan,descLuz,descPacho,descLore;
	private Typeface fuenteTitulo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion);
		
		//asignación de fuente al titulo de la app
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.tituloap = (TextView) findViewById(R.id.tituloapp);
		this.tituloap.setTypeface(this.fuenteTitulo);
		this.descUnidad = (TextView) findViewById(R.id.descUnidad);
		this.descUnidad.setTypeface(this.fuenteTitulo);
		this.equipoEmprendedor = (TextView) findViewById(R.id.equipoEmprendedor);
		this.equipoEmprendedor.setTypeface(this.fuenteTitulo);
		this.descJuan = (TextView) findViewById(R.id.descJuan);
		this.descJuan.setTypeface(this.fuenteTitulo);
		this.descLuz = (TextView) findViewById(R.id.descLuz);
		this.descLuz.setTypeface(this.fuenteTitulo);
		this.descPacho = (TextView) findViewById(R.id.descPacho);
		this.descPacho.setTypeface(this.fuenteTitulo);
		this.descLore = (TextView) findViewById(R.id.descLore);
		this.descLore.setTypeface(this.fuenteTitulo);
	}
	

	public void atras(View v){
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.informacion, menu);
		return true;
	}

}
