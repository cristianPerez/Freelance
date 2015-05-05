package com.emprende;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.emprende.R;

public class Vendo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vendo, menu);
		return true;
	}

}
