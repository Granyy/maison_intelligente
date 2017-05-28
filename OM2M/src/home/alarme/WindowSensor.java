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
	public static String appId = "WINDOW";
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "WINDOW";
	
	
	/**
	 * Variables de classe :
	 * - IRSensor : r�f�rence au capteur IR fenetre
	 */
	
	public static capteurIR IRSensor;

	/**
	 * createIRSensorResources()
	 * - Instancie le capteur IR
	 */
	
	public static void createIRSensorResources(){
		
		String content;
		
		IRSensor = new capteurIR(7);

	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
				content = ObixUtil.getWindowSensorDataRep(IRSensor.get_Capteur());
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getWindowSensorDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content);
	     }
		}
		
		/**
		 * WindowSensorListener
		 * - fonction principale s'ex�cutant dans le monitor
		 * - Si personne n'est � la maison : 
		 * 		-lis le capteur IR r�gul�rement
		 * 		-passe l'alarme active
		 * - si une intrusion est d�t�ct�e :
		 * 		- change le statut
		 */
		
		public static class WindowSensorListener extends Thread{
			 
			private boolean running = true;
			private boolean memorizedIntrusionValue = false;
			
			@Override
			public void run() {
			
			
				while(running){

						//si le capteur fenetre capte une presence
						//System.out.println("Y'A PERSONNE A LA MAISON !");
						
						
						if (IRSensor.get_Capteur()==true){
							//System.out.println("INTRUSION");
							Alarm.intrusion=true;
						}else{
							//System.out.println("TOUT VA BIEN");
							Alarm.intrusion=false;		
							}
						if (memorizedIntrusionValue != Alarm.intrusion){
							//System.out.println("CHANGEMENT VAL INTRUSION");
							String content = ObixUtil.getWindowSensorDataRep(Alarm.intrusion);
							RessourceManager.createDataContentInstance(appId, DATA, content);
							memorizedIntrusionValue = Alarm.intrusion;
						}
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e){
						e.printStackTrace();
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
				response.setContent(ObixUtil.getWindowSensorDataRep(IRSensor.get_Capteur()));
				response.setResponseStatusCode(ResponseStatusCode.OK);
				return response;
			default:
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
			return response;
		}
		

}