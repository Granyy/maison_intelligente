package org.eclipse.om2m.home.environment;


import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;




public class Ventilateur {
	
	public static String appId = "FAN";
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "LEVEL";
	
	public static ActuatorFan clim ;
	public static int fanLevel = 0;
	public static double tempTh = 25 ;
	
	
	public static void createFanResources(){
		String content;
		
		clim = new ActuatorFan(6) ;
		
	//	clim.init_ventilo();
	       
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(Ventilateur.appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
	    	   	content = ObixUtil.getFanDataRep(0);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getFanDescriptorRep(Ventilateur.appId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, Ventilateur.appId, DESCRIPTOR, content) ;
	     }
	}
	
	
	
	
	
	/**
	 * FanListener
	 * Thread qui active le ventilateur en fonction de l'ecart entre la temperature ambiante et le seuil 
	 * correspondant a la variable globale tempTh mis a jour sur evenement
	 */
	public static class FanListener extends Thread{
		 
		private boolean running = true;
		private int memorizedFanLevel = 0;
		private double memorizedtempTh = 0;
 
		@Override
		public void run() {
		
			
			
			while(running){
				
				
				clim.setThreshold(tempTh);
				
				clim.testTemp();
				
				// If the actuator state has changed
				if(memorizedFanLevel != fanLevel || memorizedtempTh != tempTh){
					// Memorize the new actuator state
					memorizedFanLevel = fanLevel;
					memorizedtempTh = tempTh ;
					
			        clim.chooseDuty() ;
			        clim.set_duty();
			        
					// Create a data contentInstance
			        String content = ObixUtil.getFanDataRep(fanLevel);
					RessourceManager.createDataContentInstance(appId, DATA, content);
				}
				
				
				
				
				// Wait for 2 seconds
				try{
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
