package com.gdglab.iot;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.gdgrometestapp.clientEndpoint.ClientEndpoint;
import com.appspot.gdgrometestapp.clientEndpoint.ClientEndpoint.Operation;
import com.appspot.gdgrometestapp.clientEndpoint.ClientEndpoint.Operation.AddClient;
import com.appspot.gdgrometestapp.clientEndpoint.model.ClientBean;
import com.gdglab.iot.tools.AppConst;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;


public class GdglabActivity extends ActionBarActivity {
    TextView mSearchText;
    int mSortMode = -1;
    
    /*GCM*/
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM_GDG";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;
    
    /*GCM end*/
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        mDisplay = (TextView) findViewById(R.id.display);
        
        mDisplay.setText("");
        
        
        context = this.getApplicationContext();
        
        //Push GCM
        Intent intent = getIntent();
        this.onNewMessage(intent);
        
        /*GCM*/
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
            	//TODO - 1) Richiamare il metodo per richiedere il registrationID
            }
            else{
            	Log.i(TAG, "registration id: "+ regid);
            	mDisplay.append("registration id: "+ regid + "\n");
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
    public void onNewMessage(Intent intent){
    	if(intent.getExtras() != null){
			  Bundle xtras = intent.getExtras();
			  String newMessage = "";
			  newMessage = xtras.getString("msgText");
			  if (newMessage!=""){
				  Log.v(TAG, newMessage);
				  mDisplay.append("New message: "+newMessage+"\n\n");
			  }
	   }
    	
    }
	 @Override
	 protected void onNewIntent(Intent intent){
	   super.onNewIntent(intent);
	   if (AppConst.VERBOSE) 
		   Log.v(TAG,"New notification");
	   this.onNewMessage(intent);
	 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
       
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       
        int idIt = item.getItemId();
        switch (idIt) {
			case R.id.new_gcm_id:
				storeRegistrationId(this,"");
				//TODO - 1) Richiamare il metodo per richiedere il registrationID
				
				break;
			case R.id.about:
				Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
				break;
			case R.id.clear:
				mDisplay.setText("");
				break;
			default:
				break;
		}
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }
    /*GCM*/
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        
        /* TODO - 2) inserire all'interno delle Shared Preferences: 
         * a) PROPERTY_REG_ID
         * b) PROPERTY_APP_VERSION
         */

    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        /* TODO - 2) Recuperare dalle Shared Preferences 
         * il registrationID memorizzato precedentemente: 
         * PROPERTY_REG_ID
         */
    	SharedPreferences prefs=null;
        String registrationId = "";
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        
        Log.i(TAG, "Registration Id: "+registrationId );
        
        return registrationId;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    //TODO - 3) Inserire il codice per richiedere il registrationID al GCM server
                    regid  ="";
                    
                    msg = "Device registered, registration ID=" + regid;

                    //TODO - 3) Memorizzare localmente il registrationID

                    
                    //TODO - 3) Inviamo il registrationID al backend AppEngine
                   

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n\n");
            }
        }.execute(null, null, null);
    }

//  
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(GdglabActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */

    private void sendRegistrationIdToCloud() {
    	
		//riempi il bean con il registration id in modo opportuno dalla tua app android
		ClientBean clientBean = new ClientBean();
		//
    	SharedPreferences sprefs = getGcmPreferences(this);
    	String gcm_id_to_send =sprefs.getString(PROPERTY_REG_ID, "");
    	clientBean.setRegistrationId(gcm_id_to_send);
		
			//Costruiamo l'oggetto che chiamerà il metodo remoto
			//il primo parametro del costruttore per te da android sarà di tipo
			//com.google.api.client.extensions.android.http.AndroidHttp
			//il secondo parametro può essere del tipo che ho messo io...questo perché le API autogenerate a basso livello
			//lavorano su oggetti JSON che vengono trasformati e mappati verso bean java e viceversa i bean java vengono mappati verso JSON
			//questo perché a basso livello vengono effettuate delle chiamate REST da un web service REST
			//il terzo parametro resta null
			ClientEndpoint cep = new ClientEndpoint(AndroidHttp.newCompatibleTransport(), GsonFactory.getDefaultInstance(), null);
			
			//Ora chiamiamo l'oggeto che prepara la chiamata alle operazioni remote
			Operation cepops = cep.operation();
			
			/*TODO - 4) Riempiamo la segnatura remota per l'operazione addclient che prende un ClientBean riempito del suo registrationId
			* e lo memorizza sul datastore invocando il metodo remoto
			* lanciamo l'operazione remota di cui abbiamo preparato la segnatura alla riga sopra (viene aperta una sessione HTTP
			* e viene chiamato il tutto
			*/
			
			
			
			
			
			
		
		
    	
      
    }
    
    public void onClickClear(final View view) {
    	mDisplay.setText("");
    }

    
    
    /*GCM end*/

}
