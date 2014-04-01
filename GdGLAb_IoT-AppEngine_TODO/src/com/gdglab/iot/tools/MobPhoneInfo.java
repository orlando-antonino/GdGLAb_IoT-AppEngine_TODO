package com.gdglab.iot.tools;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;


public class MobPhoneInfo{

	//private final String ME = this.getClass().getName();
 
	private String andId=""; 
	private String devModel="";
	private String andVer="";
	private String appVer="";

	private Context cont;
	
	public MobPhoneInfo(Context _cont) {
		
		this.cont = _cont;
    	 //TelephonyManager tMgr =(TelephonyManager) cont.getSystemService(Context.TELEPHONY_SERVICE);

         setAndId(Secure.getString(cont.getContentResolver(), Secure.ANDROID_ID));
//         setAppVer();
//         setDevModel(android.os.Build.MODEL);
//         setAndVer(android.os.Build.VERSION.RELEASE);

	}


	public String getAndId() {
		return andId;
	}
	public void setAndId(String uuid) {
		this.andId = uuid;
	}
	public String getDevModel() {
		return devModel;
	}

	public void setDevModel(String devModel) {
		this.devModel = devModel;
	}

	public String getAndVer() {
		return andVer;
	}

	public void setAndVer(String andVer) {
		this.andVer = andVer;
	}
	public String getAppVer() {
		return appVer;
	}

	public void setAppVer() {
		String version = "";
		 try {
				version = cont.getPackageManager().getPackageInfo(cont.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		this.appVer = version;
	}
	

	
	public String toStringParamUrl(){
	
		String paramsContent="";
		
		paramsContent = "id="+getAndId()
//					+"&p4="+getAppVer()
//					+"&p5="+getAndVer()
				;
		return paramsContent;
		
		
	}
}
