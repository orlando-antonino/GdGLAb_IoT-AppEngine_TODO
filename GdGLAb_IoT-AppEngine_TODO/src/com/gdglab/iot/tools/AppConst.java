package com.gdglab.iot.tools;

public final class AppConst {

	
    public static final String BEND_URL 			= "http://10.42.43.102/tps/";
    public static final String ACTN_STORE 			= "store.php?";
    public static final String PRM_GCM_ID 			= "gcmid=";
    
   /*
    * LogLevel
    */
    public static final int LOGLEVEL 				= 5; //0 assert >, 1 err >, 2 warn >, 3 info > 4 debug >, 5 verbose
    public static final boolean ASSERT 				= LOGLEVEL > -1;
    public static final boolean ERROR 				= LOGLEVEL > 0;
    public static final boolean WARN 				= LOGLEVEL > 1;
    public static final boolean INFO 				= LOGLEVEL > 2;
    public static final boolean DEBUG 				= LOGLEVEL > 3;
    public static final boolean VERBOSE 			= LOGLEVEL > 4;
    													
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    public static final boolean BEND_APP_ENGINE		= false;
    

    public static final String SENDER_ID 			= "";


    
}