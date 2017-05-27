package org.eclipse.om2m.bedroom;


import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class Night {
	
	public static String appId = "NIGHT";			
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "STATE";

	public static SensorButton sButton ;
	
	
	public static boolean buttonValue = false;
	public static boolean previousValue = false;
	
	public static void createNightResources(){
		String content;
		
		sButton = new SensorButton(5);
	       
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
	    	   	content = ObixUtil.getNightDataRep(false);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getNightDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content) ;
	     }
	}
	
	public static class NightListener extends Thread{
		 
		private boolean running = true;
 
		@Override
		public void run() {
			
	        
			while(running){
				// Simulate a random measurement of the sensor
				buttonValue = sButton.getState();
				
				if(previousValue!=buttonValue) {
					previousValue = buttonValue ;
					// Create the data contentInstance
					String content = ObixUtil.getNightDataRep(buttonValue);
					RessourceManager.createDataContentInstance(appId, DATA, content);	
				}
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
