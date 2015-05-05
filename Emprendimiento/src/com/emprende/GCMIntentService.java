package com.emprende;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.google.android.gcm.GCMBaseIntentService;
import com.recursos.ConfiguracionGlobal;
import com.recursos.Notificacion;

import com.emprende.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.RemoteViews;

public class GCMIntentService extends GCMBaseIntentService {

	public static String ide = "";
	 public GCMIntentService(){
	  super("338813978023");
	 }
	 @Override
	 protected void onError(Context context, String errorId) {
	  Log.d("GCM", "Error: " + errorId);
	 }

	 @Override
	 protected void onMessage(Context context, Intent intent) {  
	 try {
		 notificarMensaje(context, intent.getExtras().getString("message"),intent.getExtras().getString("title")); 
	} catch (Exception e) {
		// TODO: handle exception
	}
	 }

	 @Override
	 protected void onRegistered(Context context, String regId) {
	  Log.d("GCM", "onRegistered: Registrado OK.");
	  ConfiguracionGlobal.getSingletonObject().getUser().setId(regId);
	  //registrarUsuario();
	 }
	
	 private void registrarUsuario(){
		  try{
		   ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		   nameValuePairs.add(new BasicNameValuePair("tag","usersave"));
		   nameValuePairs.add(new BasicNameValuePair("username",ConfiguracionGlobal.getSingletonObject().getUser().getNombre()));
		   nameValuePairs.add(new BasicNameValuePair("gcmcode",ConfiguracionGlobal.getSingletonObject().getUser().getId()));
		   
		   HttpClient httpclient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost("http://push.zahia.com.co/index.php");
		   if(nameValuePairs != null)
		   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		   ResponseHandler<String> responseHandler = new BasicResponseHandler();
		   String res = httpclient.execute(httppost, responseHandler);
		   Log.w("GCM", "RES: " + res);
		  } catch(Exception e){
		   Log.w("GCM", "ex: " + e.getMessage().toString());
		  }
		 }

	 
	 @Override
	 protected void onUnregistered(Context context, String regId) {
	  Log.d("GCM", "onUnregistered: Desregistrado OK.");
	 }
	 
	 
	 
	 private void notificarMensaje(Context context, String message, String title)
	 {
		 
		         NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		         Intent myIntent = new Intent(this,Notificacion.class);
		         
		         Bundle b = new Bundle();
			     b.putInt("update", 1);
			     b.putString("message", message);
			     b.putString("titles", title);
			     myIntent.putExtra("extra", b);
		         Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
		         notification.flags |= Notification.FLAG_AUTO_CANCEL;

		         RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.custom_notifications);
		         contentView.setImageViewResource(R.id.logo2, R.drawable.ic_launcher);
		         contentView.setTextViewText(R.id.description, message+title);
		         notification.contentView = contentView;
		         notification.contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		         long[] vibrate = {100,100,200,300};
			     notification.vibrate = vibrate;
			     Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			     notification.sound = defaultSound;
		         
		         mManager.notify(0, notification);
		         PowerManager pm = (PowerManager) 
		         context.getSystemService(Context.POWER_SERVICE);
		         WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		         wl.acquire(15000);
		         
	 } 
	 
	}

