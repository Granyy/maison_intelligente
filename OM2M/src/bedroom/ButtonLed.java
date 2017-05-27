package org.eclipse.om2m.bedroom;


import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class ButtonLed {
	
	public static String appId = "BOUTON";			
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "STATE";

	public static SensorButton sButton ;
	
	
	public static boolean buttonValue = false;
	public static boolean previousValue = false;
	public static boolean allON = false ;
	
	public static void createButtonResources(){
		String content;
		
		sButton = new SensorButton(2);
	       
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
	    	   	content = ObixUtil.getButtonDataRep(false);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getButtonDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content) ;
	     }
	}
	
	public static class ButtonListener extends Thread{
		 
		private boolean running = true;
 
		@Override
		public void run() {
			
	        
			while(running){
				// Simulate a random measurement of the sensor
				buttonValue = sButton.getState();
				
				if(previousValue!=buttonValue) {
					
					// Create the data contentInstance
					String content = ObixUtil.getButtonDataRep(buttonValue);
					RessourceManager.createDataContentInstance(appId, DATA, content);
					
					if(buttonValue==true && previousValue==false) {
						if (allON == true) {
							LED Led ;
							Set<Entry<String, LED>> setHm = Light.lights.entrySet();
						    Iterator<Entry<String, LED>> it = setHm.iterator();
						    while (it.hasNext()) {
						       Entry<String, LED> nextTag = it.next();
						       Led = nextTag.getValue();
						       Led.setState(true);
						    }
						    allON = false;
						}
						else if (allON == false) {
						    LED Led ;
							Set<Entry<String, LED>> setHm = Light.lights.entrySet();
							Iterator<Entry<String, LED>> it = setHm.iterator();
							while (it.hasNext()) {
							    Entry<String, LED> nextTag = it.next();
							    Led = nextTag.getValue();
							    Led.setState(false);
							}
							allON = true;
						}
					}		
					previousValue = buttonValue ;
				
				}
				try {
					Thread.sleep(200);
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
