package com.emprende;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.emprende.R;

public class PreguntasAportes extends TabActivity {

    private TextView tituloapp;
    private Typeface fuenteTitulo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preguntas_aportes);
		
		TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        Resources res = getResources();
        
        //Tab 1
        intent = new Intent().setClass(this, PestanaPreguntas.class);
        spec = tabHost.newTabSpec("Pestaña1").setIndicator("Preguntas", res.getDrawable(R.drawable.pestana1)).setContent(intent);
        tabHost.addTab(spec);
        //tabHost.getTabWidget().setBackgroundColor(Color.BLACK);
        
        //Tab2
        intent = new Intent().setClass(this, PestanaAportes.class);
        spec = tabHost.newTabSpec("Pestaña2").setIndicator("Aportes", res.getDrawable(R.drawable.pestana2)).setContent(intent);
        tabHost.addTab(spec);

        this.tituloapp = (TextView) findViewById(R.id.tituloapp);
        this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
        this.tituloapp.setTypeface(this.fuenteTitulo);
	}
	
	public void atras(View v){
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preguntas_aportes, menu);
		return true;
	}

}
