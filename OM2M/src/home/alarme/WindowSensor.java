package org.eclipse.om2m.home.alarme;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;
import org.eclipse.om2m.home.rfid.UserProfile;


public class WindowSensor {
	
	/**
	 * Constantes de classe :
	 * - String DESCRIPTOR : ID du descriptor container
	 * - String DATA : ID du data container
	 */
	static String appId = "WINDOW";
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "WindowSensor";
	
	
	/**
	 * Variables de classe :
	 * - IRSensor : référence au capteur IR fenetre
	 */
	
	public static capteurIR IRSensor;

	/**
	 * createIRSensorResources()
	 * - Instancie le capteur IR
	 */
	
	public static void createIRSensorResources(){
		
		String content;
		capteurIR IRSensor;
		
		IRSensor = new capteurIR();
		IRSensor.init_Capteur(7);
		
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
				content = ObixUtil.getWindowSensorDataRep(IRSensor.get_Active(),IRSensor.get_Capteur());
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getWindowSensorDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content);
	     }
		}
		
		/**
		 * WindowSensorListener
		 * - fonction principale s'exécutant dans le monitor
		 * - Si personne n'est à la maison : 
		 * 		-lis le capteur IR régulèrement
		 * 		-passe l'alarme active
		 * - si une intrusion est détéctée :
		 * 		- change le statut
		 */
		
		public static class WindowSensorListener extends Thread{
			 
			private boolean running = true;
	 
			@Override
			public void run() {
				
				while(running){
					//si personne n'est à la maison
					if (UserProfile.isSomeoneHome() == false){
						IRSensor.set_Active(true); //active l'alarme
						//si le capteur fenetre capte une presence
						if (IRSensor.get_Capteur()==true){
							Alarm.intrusion=true;
						}else{Alarm.intrusion=false;}	
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
		
		

		public static ResponsePrimitive WindowSensorController(String userId, String valueOp, ResponsePrimitive responsein) {
			ResponsePrimitive response = responsein;
			switch(valueOp){
			case "get":
				response.setContent(ObixUtil.getWindowSensorDataRep(IRSensor.get_Active(), IRSensor.get_Capteur()));
				response.setResponseStatusCode(ResponseStatusCode.OK);
				return response;
			default:
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
			return response;
		}
		

}