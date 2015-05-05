package com.emprende;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import com.emprende.R;

public class Inicio extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);
		
		Handler HANDLER = new Handler();
	    HANDLER.postDelayed(new Runnable(){
	           public void run() {
	                finish();
	                startActivity (new Intent(getApplicationContext(), LogIn.class));
	                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	           }
	    }, 2000);
	}
}
