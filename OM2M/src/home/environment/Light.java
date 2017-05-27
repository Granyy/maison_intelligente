package org.eclipse.om2m.home.environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

//import org.eclipse.om2m.home.rfid.User;

public class Light {
	
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "STATE";
	
	
/* Variable de preferences d'utilisateurs */
	public static int lumiTh = Integer.MAX_VALUE ;
	public static boolean led1Pref = false ;
	public static boolean led2Pref = false ;
	
	//User defaultUser = new User("default", 0, Integer.MAX_VALUE,);				// User par defaut créé au cas où si besoin
	
	public static Map<String,LED> lights;
	public static LED Led1 ;
	public static LED Led2 ; 
	
	public static void createLightsMap () {
		Led1 = new LED(3,"LED1");
		Led2 = new LED(4,"LED2");
		lights = new HashMap<>();
		lights.put("LED1", Led1); 
		lights.put("LED2", Led2); 
	}

	public static void createLightsResources() {
		String content, ledId;
		LED Led ;
		
		createLightsMap();
		
		
		Set<Entry<String, LED>> setHm = lights.entrySet();
	    Iterator<Entry<String, LED>> it = setHm.iterator();
	    while (it.hasNext()) {
	       Entry<String, LED> nextTag = it.next();
	       Led = nextTag.getValue();
	       ledId = nextTag.getValue().getLedId();
	       
	       // Create the AE
		   ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, ledId);
		   
		   if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){	
			   	// Create the container
			   	RessourceManager.createContainer(ledId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
				content = ObixUtil.getLedDataRep(Led);
				RessourceManager.createDataContentInstance(ledId, DATA, content);
			
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getLedDescriptorRep(ledId,Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, ledId, DESCRIPTOR,content);
		       }  
		    }
		}
	
	
public static class LightListener extends Thread{
		
		private boolean running = true;
		String ledId;
		boolean memorizedState = false;
		boolean memorizedActivated = false;
		int memorizedLumiTh = Integer.MAX_VALUE;
		LED Led ;
		
		@Override
		public void run() {
	        
			while(running){
				// A remplir en fonction de la fonction de Leo pour recuperer, en l'occurrence, les LEDs à allumer selon la préférence
				
				Set<Entry<String, LED>> setHm = lights.entrySet();
			    Iterator<Entry<String, LED>> it = setHm.iterator();
			    while (it.hasNext()) {
			    	Entry<String, LED> nextTag = it.next();
			    	Led = nextTag.getValue();
			    	Led.setThreshold(lumiTh);						
			    	if (Led.getLedId() == "LED1") {
			    		Led.setActivate(led1Pref) ;
			    	}
			    	else if (Led.getLedId() == "LED2") {
			    		Led.setActivate(led2Pref) ;					
			    	}
			    	
			    	Led.setONThreshold();					// Mettra la LED dans un etat en fonction du threshold et de la  
																			// Preference de l'utilisateur
			    	if(memorizedState != Led.getState() || memorizedActivated != Led.getActivate() || memorizedLumiTh != Led.getThreshold()) {
						// Create the data contentInstance
			    		memorizedState = Led.getState();
			    		memorizedActivated = Led.getActivate();
			    		memorizedLumiTh = Led.getThreshold();
			    		
						String content = ObixUtil.getLedDataRep(nextTag.getValue()); 
						RessourceManager.createDataContentInstance(nextTag.getValue().getLedId(), DATA, content);
			    	}
			    }
				try {
					Thread.sleep(500);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
 
		}
 
		public void stopThread(){
			running = false;
		}
 
	}
	
}
