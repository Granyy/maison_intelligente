/******************************************************************************/
/*        @TITRE : Night.java                                                 */
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


import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class Night {
	
	public static String appId = "NIGHT";			
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "STATE";

	public static SensorButton sButton;
	
	
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
			
	        boolean allON = false;
			while(running){
				// Simulate a random measurement of the sensor
				buttonValue = sButton.getState();
				
				if(previousValue!=buttonValue) {
					
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
