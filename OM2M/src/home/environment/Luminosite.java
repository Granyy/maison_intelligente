package org.eclipse.om2m.home.environment;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;



public class Luminosite {
	
	public static String appId = "LUMINOSITE";			
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "LUMI";

	public static SensorLumi sLumi ;
	
	public static int lumiValue = 0;
	
	public static void createLuminositeResources(){
		String content;
	       
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
	    	   	content = ObixUtil.getLumiDataRep(-1);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getLuminositeDescriptorRep(Luminosite.appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content) ;
	     }
	}
	
	/**
	 * LuminositeListener
	 * Lit regulierement la luminosite
	 *
	 */
	public static class LuminositeListener extends Thread{
		
		private boolean running = true;
 
		@Override
		public void run() {
			sLumi = new SensorLumi(1);
			sLumi.initSensor();
	        
			while(running){
				
				sLumi.readLumi();
				
				lumiValue = sLumi.getLumi();
 
				// Create the data contentInstance
				String content = ObixUtil.getLumiDataRep(lumiValue);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				try {
					Thread.sleep(1000);
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
