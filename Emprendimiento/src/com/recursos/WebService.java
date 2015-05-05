package com.recursos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class WebService extends Activity {
	
	private String resultado;
	private JSONArray jArray;
	private List<BasicNameValuePair> valores;
	private InputStream is;

	public synchronized JSONArray conectar(String[] parametros) {
		this.valores = new ArrayList<BasicNameValuePair>();
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(parametros[0].split(",")[1]);
			UrlEncodedFormEntity encodeEntity = null;
			for (int i = 1; i < parametros.length; i++){
				this.valores.add(new BasicNameValuePair(parametros[i].split(",")[0],parametros[i].split(",")[1]));
			}
			encodeEntity = new UrlEncodedFormEntity(valores);
			if (encodeEntity != null){
				((HttpPost) request).setEntity(encodeEntity);
			}
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			this.is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			this.is.close();
			this.resultado = sb.toString();
			
			try {
				this.jArray = new JSONArray(resultado);
			} catch (Exception e) {
				this.resultado="["+this.resultado+"]";
				this.jArray = new JSONArray(resultado);
			}
		}catch (ClientProtocolException e) {
			try {
				this.jArray = new JSONArray("[{'respuesta':0}]");
			} catch (JSONException e1) {
			}
		}catch (IOException e) {
			e.printStackTrace();
			try {
				this.jArray = new JSONArray("[{'respuesta':0}]");
			} catch (JSONException e1) {
			}
		}catch (Exception e) {
			try {
				this.jArray = new JSONArray("[{'respuesta':0}]");
			}catch (JSONException e1) {
			}
		}
		return this.jArray;
	}
	
}