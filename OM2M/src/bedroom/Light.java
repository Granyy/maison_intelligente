package org.eclipse.om2m.bedroom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.bedroom.Monitor;
import org.eclipse.om2m.bedroom.RessourceManager;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class Light {
	
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "STATE";
	
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
				content = ObixUtil.getLedDataRep(false);
				RessourceManager.createDataContentInstance(ledId, DATA, content);
			
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getLedDescriptorRep(ledId,Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, ledId, DESCRIPTOR,content);
		       }  
		    }
		}
	
	
public static class LightListener extends Thread{
		
		private boolean running = true;
		private boolean ledValue, memorizedLedValue;
		String ledId;
		LED Led ;
		
		@Override
		public void run() {
	        
			while(running){
				
				Set<Entry<String, LED>> setHm = lights.entrySet();
			    Iterator<Entry<String, LED>> it = setHm.iterator();
			    while (it.hasNext()) {
			    	Entry<String, LED> nextTag = it.next();
			    	ledValue = nextTag.getValue().getState();
			    	memorizedLedValue = nextTag.getValue().isOldValue();
				
					if(memorizedLedValue != ledValue){
						// Memorize the new actuator state
						nextTag.getValue().setOldValue(ledValue);
	 
						// Create a data contentInstance
						String content = ObixUtil.getLedDataRep(nextTag.getValue().getState()); 
						RessourceManager.createDataContentInstance(nextTag.getValue().getLedId(), DATA, content);
					}
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
