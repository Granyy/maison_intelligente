/******************************************************************************/
/*        @TITRE : Alarm.java                                              */
/*      @VERSION : 0.3                                                    */
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
import org.eclipse.om2m.home.rfid.UserProfile;


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
	static boolean ring = false;
	private static Buzzer buzzer;
	private static boolean windowForgot=false;
	
	/**
	 * createAlarmResources
	 * - cr�e un AE
	 * - cr�e un data container
	 * - cr�e un descriptor container
	 * - les noms sont configur�s � partir des diff�rentes constantes de classe
	 */
	
	public static void createAlarmResources(){
		String content;
	    buzzer = new Buzzer(4);
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
				content = ObixUtil.getBuzzerDataRep(buzzer.get_Active(),false, false);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getBuzzerDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content);
	     }
	    
		
	 }
	
	/**
	 * WindowSensorkListener
	 * - fonction principale s'ex�cutant dans le monitor
	 * - g�re deux modes de s�curit�
	 * 		-quand la derniere personne qui part oublie de fermer la fenetre => envoi d'un mail
	 * 		-quand personne n'est � la maison, une ouverture de la fenetre d�clenche l'alarme
	 */
	
	
	public static class BuzzerListener extends Thread{
		 
		private boolean running = true;
		private boolean memorizedHomeValue = false; //ancienne valeur pour UserProfile.isSomeoneHome
		private boolean memorizedRingValue = false;
		
		
		@Override
		public void run() {
			
			while(running){
						
				if (memorizedHomeValue != UserProfile.isSomeoneHome()){
					
					memorizedHomeValue=UserProfile.isSomeoneHome();
					
					if (memorizedHomeValue) {
						buzzer.set_Active(false);
						windowForgot=false;
						ring = false;
					}
					else if (memorizedHomeValue == false) {
						buzzer.set_Active(true);
						if (Alarm.intrusion==true){
							windowForgot=true;
							//System.out.println("WINDOW FORGOT = TRUE");
						}else{
							windowForgot=false;
							//System.out.println("WINDOW FORGOT = FALSE");
						}
					}
					
					//System.out.println("CHANGEMENT ACTIVE");
					String content = ObixUtil.getBuzzerDataRep(buzzer.get_Active(), ring, windowForgot);
					RessourceManager.createDataContentInstance(appId, DATA, content);
				}
				
				
			//gestion alarme intrusion
				if (buzzer.get_Active()==true && intrusion==true && windowForgot==false){
					ring = true;
					buzzer.sonner_Buzzer();
				}
				else {
					ring = false;
					buzzer.stopper_Buzzer();
				}
				
				if (memorizedRingValue!=ring) {
					String content = ObixUtil.getBuzzerDataRep(buzzer.get_Active(), ring, windowForgot);
					RessourceManager.createDataContentInstance(appId, DATA, content);
				}
				
				
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
			response.setContent(ObixUtil.getBuzzerDataRep(buzzer.get_Active(),ring,windowForgot));
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