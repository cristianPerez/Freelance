package com.emprende;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.emprende.R;

public class Sin_internet extends Activity implements DialogInterface {

	private Typeface fuenteTitulo;
	public TextView txt1,txt2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sin_internet);
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.txt1 = (TextView) findViewById(R.id.txtSinConexion1);
		this.txt1.setTypeface(this.fuenteTitulo);
		this.txt2 = (TextView) findViewById(R.id.txtSinConexion2);
		this.txt2.setTypeface(this.fuenteTitulo);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vendo, menu);
		return true;
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void back(View v){
		finish();
	}

}
