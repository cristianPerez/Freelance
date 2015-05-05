package com.recursos;

import com.emprende.MenuGlobal;
import com.emprende.R;
import com.emprende.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Notificacion extends Activity{

    private TextView titulo,descripcion,tituloapp;
    private Typeface fuenteTitulo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificacion);
		Intent intent = getIntent();
		Bundle b = intent.getBundleExtra("extra");


		this.titulo = (TextView) findViewById(R.id.titulo);
		this.descripcion = (TextView) findViewById(R.id.descripcion);
		this.tituloapp = (TextView) findViewById(R.id.tituloapp);

        this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
        this.tituloapp.setTypeface(this.fuenteTitulo);
        this.titulo.setTypeface(this.fuenteTitulo);
        this.descripcion.setTypeface(this.fuenteTitulo);

		titulo.setText(b.getString("titles"));
		descripcion.setText(b.getString("message"));
		
	}

    public void atras(View v){

       Intent intent = new Intent(this, MenuGlobal.class);
        startActivity(intent);

    }

}
