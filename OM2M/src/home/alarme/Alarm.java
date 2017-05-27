/******************************************************************************/
/*        @TITRE : Alarm.java                                              */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 10, 2017                                                */
/* @MODIFICATION :                                                            */
/*      @AUTEURS : Matthieu Bougeard                                                */
/*    @COPYRIGHT : Copyright (c) 2017                                         */
/*                 Paul GUIRBAL                                               */
/*                 Joram FILLOL-CARLINI                                       */
/*                 Gianni D'AMICO                                             */
/*                 Matthieu BOUGEARD                                          */
/*                 Leo GRANIER                                                */
/*      @LICENSE : MIT License (MIT)                                          */
/******************************************************************************/

package org.eclipse.om2m.home.alarme;


import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;


public class Alarm {

	/**
	 * Constantes de classe :
	 * - String appId : ID de l'AE
	 * - String DESCRIPTOR : ID du descriptor container
	 * - String DATA : ID du data container
	 */

	public static String appId = "ALARM";
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "ALARM";
	
	 /**  Variables de classe :
	 *  - 
	 *  - boolean intrusion : situation d'effraction d�t�ct�e
	 */
	static boolean intrusion=false;
	static boolean ring;
	private static Buzzer buzzer;
	
	/**
	 * createAlarmResources
	 * - cr�e un AE
	 * - cr�e un data container
	 * - cr�e un descriptor container
	 * - les noms sont configur�s � partir des diff�rentes constantes de classe
	 */
	
	public static void createAlarmResources(){
		String content;
	       
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
				content = ObixUtil.getBuzzerDataRep(false);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getWindowSensorDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content);
	     }
	    
		Buzzer buzzer = new Buzzer();   
		buzzer.init_Buzzer(8);
		
	 }
	
	/**
	 * WindowSensorlockListener
	 * - fonction principale s'ex�cutant dans le monitor
	 * - surveille les changements de la variable intrusion :
	 * 		- true : sonner buzzer
	 * 		- false : arr�ter buzzer
	 */
	
	
	public static class BuzzerListener extends Thread{
		 
		private boolean running = true;
		private boolean memorizedOpenValue = false;
 
		@Override
		public void run() {
			
			while(running){
				// If the actuator state has changed
				if(memorizedOpenValue != intrusion){
					// Memorize the new window state
					memorizedOpenValue = intrusion;
					// Create a data contentInstance
					String content = ObixUtil.getBuzzerDataRep(intrusion);
					RessourceManager.createDataContentInstance(appId, DATA, content);
					// Wait for 5 seconds
					if (intrusion==true){
						buzzer.sonner_Buzzer();
						intrusion = false;
					}
					else if (intrusion==false) {
						buzzer.stopper_Buzzer();
					}
				}
			try{
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
	
	
	/**
	 * BuzzerController
	 * - fonction s'ex�cutant dans le Controller, g�rant les requ�tes � destination de Doorlock
	 * - query g�r�s :
	 * 		- get : renvoie la valeur intrusion
	 * 		- intrusion : intrusion vaut true, le buzzer sonne
	 * @param : 
	 * - String valueOp : query de la requ�te re�ue
	 * - ResponsePrimitive responsein : reponse partiellement construite par le Controller g�n�ral
	 */
	
	public static ResponsePrimitive BuzzerController(String valueOp, ResponsePrimitive responsein) {
		ResponsePrimitive response = responsein;
		switch(valueOp){
		case "get":
			response.setContent(ObixUtil.getBuzzerDataRep(intrusion));
			response.setResponseStatusCode(ResponseStatusCode.OK);
			return response;
		case "ring":
			intrusion = true;
			response.setResponseStatusCode(ResponseStatusCode.OK);
			return response;
		default:
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}

}