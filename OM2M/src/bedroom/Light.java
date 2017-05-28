/******************************************************************************/
/*        @TITRE : Light.java                                                 */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 23, 2017                                                */
/* @MODIFICATION :                                                            */
/*      @AUTEURS : Gianni D'Amico & Leo Granier                               */
/*    @COPYRIGHT : Copyright (c) 2017                                         */
/*                 Paul GUIRBAL                                               */
/*                 Joram FILLOL-CARLINI                                       */
/*                 Gianni D'AMICO                                             */
/*                 Matthieu BOUGEARD                                          */
/*                 Leo GRANIER                                                */
/*      @LICENSE : MIT License (MIT)                                          */
/******************************************************************************/

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
	public static LED Led3 ;
	public static LED Led4 ; 
	
	public static void createLightsMap () {
		Led3 = new LED(3,"LED3");
		Led4 = new LED(4,"LED4");
		lights = new HashMap<>();
		lights.put("LED3", Led3); 
		lights.put("LED4", Led4); 
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
