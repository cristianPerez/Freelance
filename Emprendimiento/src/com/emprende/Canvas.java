package com.emprende;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import com.emprende.R;

public class Canvas extends Activity {

	private String ITEM;
	static final int REQUEST_DESCRIPTION = 1;
	public SharedPreferences sharedpreferences;
	private String canvas_id;
	private Typeface fuenteTitulo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canvas);
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.configurarFuente();
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		String c_id = this.sharedpreferences.getString("idProyecto", null);
		if(c_id != null && !c_id.equals("")){
			this.canvas_id = c_id;
		}
	}

	public void item(View v) {
		this.ITEM="";
		switch (v.getId()) {
		case R.id.button_a:
			this.ITEM = "socios";
			break;
		case R.id.button_b:
			this.ITEM = "actividades";
			break;
		case R.id.button_c:
			this.ITEM = "recursos";
			break;
		case R.id.button_d:
			this.ITEM = "propuestas";
			break;
		case R.id.button_e:
			this.ITEM = "relaciones";
			break;
		case R.id.button_f:
			this.ITEM = "canales";
			break;
		case R.id.button_g:
			this.ITEM = "segmentos";
			break;
		case R.id.button_h:
			this.ITEM = "estructuras";
			break;
		case R.id.button_i:
			this.ITEM = "fuentes";
			break;

		default:
			break;
		}
		Intent intent = new Intent(this, ItemCanvas.class);
		intent.putExtra("ITEM", this.ITEM);
		intent.putExtra("CANVAS_ID", this.canvas_id);
		startActivity(intent);
	}
	
	private void configurarFuente(){
		Button a = (Button) findViewById(R.id.button_a);
		Button b = (Button) findViewById(R.id.button_b);
		Button c = (Button) findViewById(R.id.button_c);
		Button d = (Button) findViewById(R.id.button_d);
		Button e = (Button) findViewById(R.id.button_e);
		Button f = (Button) findViewById(R.id.button_f);
		Button g = (Button) findViewById(R.id.button_g);
		Button h = (Button) findViewById(R.id.button_h);
		Button i = (Button) findViewById(R.id.button_i);
		
		a.setTypeface(this.fuenteTitulo);
		b.setTypeface(this.fuenteTitulo);
		c.setTypeface(this.fuenteTitulo);
		d.setTypeface(this.fuenteTitulo);
		e.setTypeface(this.fuenteTitulo);
		f.setTypeface(this.fuenteTitulo);
		g.setTypeface(this.fuenteTitulo);
		h.setTypeface(this.fuenteTitulo);
		i.setTypeface(this.fuenteTitulo);
	}
}