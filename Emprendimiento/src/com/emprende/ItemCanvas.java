package com.emprende;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.adaptadores.AdaptadorComponenteICanvas;
import com.adaptadores.AdaptadorPreguntas;
import com.emprende.R;
import com.recursos.WebService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemCanvas extends Activity implements OnItemClickListener, OnItemLongClickListener{

	private WebService ws;
	private EditText txtDescripcion;
	public SharedPreferences sharedpreferences;
	private ProgressDialog dialog;
	private ListView listaComponentes;
	private JSONArray respuesta;
	private String item;
	private String canvas_id;
	private Typeface fuenteTitulo;
	static final int REQUEST_DESCRIPTION = 1;
	private TextView lblTitulo;
	private Button btnNuevo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_canvas);

		Intent intent = getIntent();
		sharedpreferences = getSharedPreferences(LogIn.MyPREFERENCES,Context.MODE_PRIVATE);
		String USER_ID = sharedpreferences.getAll().get("nameKey").toString();
		this.item = intent.getStringExtra("ITEM");
		this.canvas_id = intent.getStringExtra("CANVAS_ID");
		this.fuenteTitulo = Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf");

		this.ws = new WebService();
		this.lblTitulo = (TextView) findViewById(R.id.lblItem);
		this.btnNuevo = (Button) findViewById(R.id.bntNuevo);
		this.lblTitulo.setTypeface(fuenteTitulo);
		this.btnNuevo.setTypeface(fuenteTitulo);
		this.dialog = new ProgressDialog(this);
		this.listaComponentes = (ListView) findViewById(R.id.lstComponentes);
		this.listaComponentes.setOnItemClickListener(this);
		this.listaComponentes.setOnItemLongClickListener(this);
		TextView lblTituloItem = (TextView) findViewById(R.id.lblItem);
		lblTituloItem.setTypeface(fuenteTitulo);
		lblTituloItem.setText(this.item);
		new Sincronizar().execute("descargar",this.item,this.canvas_id);
	}
	
	private void mostrarItems(){
		AdaptadorComponenteICanvas adaptador = new AdaptadorComponenteICanvas(this, this.respuesta, this.fuenteTitulo);
		this.listaComponentes.setAdapter(adaptador);
	}
	
	public void nuevo(View v){
		Intent intent = new Intent(this, NuevoItem.class);
		intent.putExtra("TIPO", "nuevo");
		intent.putExtra("ACTIVIDAD", this.item);
		intent.putExtra("CANVAS_ID", this.canvas_id);
		startActivityForResult(intent, REQUEST_DESCRIPTION);
	}
	
	public void atras(View v){
		this.finish();
	}

	private class Sincronizar extends AsyncTask<String, Void, Void> {
		
		String tipoConsulta;
		JSONArray respElim;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setTitle("Sincronizando");
			dialog.setMessage("Espere un momento...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			this.tipoConsulta = arg0[0];
			if(this.tipoConsulta.equals("descargar")){
			String[] parametros = 
				{
					"url,"+"http://unidademprendimiento.tk/Controller/facade_canvas.php?method=item",
					"actividad,"+arg0[1],
					"id_canvas,"+arg0[2]
				};
			respuesta = ws.conectar(parametros);
			}
			else{
				String[] parametros = 
					{
						"url,"+"http://unidademprendimiento.tk/Controller/facade_canvas.php?method=eliminar_actividad_canvas_movil",
						"id_item,"+arg0[1],
						"id_canvas,"+arg0[2]
					};
				respElim = ws.conectar(parametros);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(this.tipoConsulta.equals("descargar")){
				mostrarItems();
			}
			else{
				try {
					if(this.respElim.getJSONObject(0).getInt("respuesta")==1){
						new Sincronizar().execute("descargar",item,canvas_id);
					}
					else{
						Toast.makeText(getApplicationContext(), "No se pudo realizar la operación", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = this.respuesta.getJSONObject(arg2);
			String titulo = obj.getString("titulo");
			String descripcion = obj.getString("descripcion");
			String clase = obj.getString("clase");
			String id_item = obj.getString("id");
			Intent intent = new Intent(this, NuevoItem.class);
			intent.putExtra("TIPO", "edicion");
			intent.putExtra("TITULO", titulo);
			intent.putExtra("DESCRIPCION", descripcion);
			intent.putExtra("CLASE", clase);
			intent.putExtra("ACTIVIDAD", this.item);
			intent.putExtra("ID_ITEM", id_item);
			intent.putExtra("ESTADO_ITEM", id_item);
			startActivityForResult(intent, REQUEST_DESCRIPTION);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DESCRIPTION) {
			if (resultCode == RESULT_OK) {
				new Sincronizar().execute("descargar",this.item,this.canvas_id);
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vb.vibrate(50);
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		JSONObject item = null;
		try {
			item = respuesta.getJSONObject(arg2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final JSONObject obj = item;
        adb.setTitle("¿Deseas borrar este item?");
        adb.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        // TODO Auto-generated method stub
                    	String id = null;
						try {
							id = obj.getString("id");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	new Sincronizar().execute("eliminar",id,canvas_id);

                    }
                });
        adb.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        // TODO Auto-generated method stub
                         dialog.dismiss();

                    }
                });
        adb.show();
		
		return false;
	}

}
