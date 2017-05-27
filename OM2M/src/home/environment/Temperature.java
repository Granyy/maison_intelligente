package org.eclipse.om2m.home.environment;


import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;

public class Temperature {
	
	public static String appId = "TEMPERATURE";			
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "TEMP";

	public static SensorTemp temp ;
	
	public static double tempValue = 0.0;
	
	public static void createTemperatureResources(){
		String content;
	       
		temp = new SensorTemp(0);
		temp.initSensor();
		
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
	    	   	content = ObixUtil.getSensorDataRep(0);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getTemperatureDescriptorRep(Temperature.appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content) ;
	     }
	}
	
	public static class TemperatureListener extends Thread{
		 
		private boolean running = true;
 
		@Override
		public void run() {
			
	        
			while(running){
				// Simulate a random measurement of the sensor
				temp.readTemp();
				
				tempValue = temp.getTemp();
 
				// Create the data contentInstance
				String content = ObixUtil.getSensorDataRep(tempValue);
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
