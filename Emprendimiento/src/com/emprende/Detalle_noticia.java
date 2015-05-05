package com.emprende;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.emprende.R;

public class Detalle_noticia extends Activity {

	public String title;
	public String contenido;
	public String fecha;
	public String imagen;
	public String url;
	public String usuario_email;
	public TextView txt_v_titulo_Noticia;
	public TextView txt_v_conten_Noticia;
	public TextView txt_v_fecha_Noticia;
	public ImageView txt_v_imagen_Noticia;
	public TextView txt_v_url_Noticia;
	private Typeface fuenteTitulo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_noticia);
		Intent intent = getIntent();
		title = intent.getStringExtra("titulo");
		contenido = intent.getStringExtra("contenido");
		fecha = intent.getStringExtra("fecha");
		imagen = intent.getStringExtra("imagen");
		url = intent.getStringExtra("url");
		usuario_email = intent.getStringExtra("usuario_email");
		
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");
		this.txt_v_titulo_Noticia=(TextView) findViewById(R.id.tituNoticia);
		this.txt_v_titulo_Noticia.setTypeface(this.fuenteTitulo);
		this.txt_v_conten_Noticia=(TextView) findViewById(R.id.desNoticia);
		this.txt_v_conten_Noticia.setTypeface(this.fuenteTitulo);
		this.txt_v_fecha_Noticia=(TextView) findViewById(R.id.fecha_noticia);
		this.txt_v_fecha_Noticia.setTypeface(this.fuenteTitulo);
		this.txt_v_imagen_Noticia=(ImageView) findViewById(R.id.imgNoticia);
		this.cargando();
	}
	
	public void vermas(View v){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_noticia, menu);
		return true;
	}
	
	public void atras(View v){
		finish();
	}
	
	public void cargando(){
		try {
			txt_v_titulo_Noticia.setText(title);
			txt_v_conten_Noticia.setText(contenido);
			txt_v_fecha_Noticia.setText(fecha);
			if(imagen!=null||imagen.equals(" ")){
				String url="http://unidademprendimiento.tk/View/"+imagen;
//				String url="http://www.boomwallpaper.com/wp-content/uploads/2014/05/3d-nature-wallpaper-hd-1080p-free-download-1.jpg";
				new DownloadImageTask().execute(url);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Bitmap DownloadImage(String myURL){
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(myURL).getContent());
		}catch (IOException e1){
			Log.d("NetworkingActivity", e1.getLocalizedMessage());			
		}
		return bitmap;
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{		
		public ProgressBar progress= (ProgressBar) findViewById(R.id.progressBar1);
		
		
		@Override
  		protected void onPreExecute() {

  		}
		
		protected Bitmap doInBackground(String... urls){
			return DownloadImage(urls[0]);
		}
		
		protected void onPostExecute(Bitmap result){
			if(result!=null)
			txt_v_imagen_Noticia.setImageBitmap(result);
			this.progress.setVisibility(View.INVISIBLE);
		}
		
}

}
