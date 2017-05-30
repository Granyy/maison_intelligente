/******************************************************************************/
/*        @TITRE : Doorlock.java                                              */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 06, 2017                                                */
/* @MODIFICATION :                                                            */
/*      @AUTEURS : Leo GRANIER                                                */
/*    @COPYRIGHT : Copyright (c) 2017                                         */
/*                 Paul GUIRBAL                                               */
/*                 Joram FILLOL-CARLINI                                       */
/*                 Gianni D'AMICO                                             */
/*                 Matthieu BOUGEARD                                          */
/*                 Leo GRANIER                                                */
/*      @LICENSE : MIT License (MIT)                                          */
/******************************************************************************/

package org.eclipse.om2m.home.rfid;


import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;



public class Doorlock {

	/**
	 * Constantes de classe :
	 * - String appId : ID de l'AE
	 * - String DESCRIPTOR : ID du descriptor container
	 * - String DATA : ID du data container
	 */

	public static String appId = "DOORLOCK";
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "UNLOCK";
	
	 /**  Variables de classe :
	 *  - Lock lock : reference a la serrure representee par le servo
	 *  - boolean unlock : situation de la serrure (true pour ouverte)
	 */
	
	static boolean unlock;
	static Lock lock;
	
	/**
	 * createDoorlockResources
	 * - cree un AE
	 * - cree un data container
	 * - cree un descriptor container
	 * - les noms sont configures a partir des differentes constantes de classe
	 * - instancie la variable lock representant la serrure
	 */
	
	public static void createDoorlockResources(){
		String content;
	       
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
				content = ObixUtil.getDoorDataRep(false);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getDoorDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content);
	     }
	    
		lock = new Lock(5);
		lock.setLock();
	    
	 }
	
	/**
	 * DoorlockListener
	 * - fonction principale s'executant dans le monitor
	 * - surveille les changements de la variable unlock :
	 * 		- true : ouverture pendant 5s
	 * 		- false : fermeture
	 */
	
	
	public static class DoorlockListener extends Thread{
		 
		private boolean running = true;
		private boolean memorizedUnlockValue = false;
 
		@Override
		public void run() {
			
			while(running){
				// If the actuator state has changed
				if(memorizedUnlockValue != unlock){
					// Memorize the new door state
					memorizedUnlockValue = unlock;
					// Create a data contentInstance
					String content = ObixUtil.getDoorDataRep(unlock);
					RessourceManager.createDataContentInstance(appId, DATA, content);
					// Wait for 5 seconds
					if (unlock==true){
						lock.setUnlock();
						try{
							Thread.sleep(5000);
						} catch (InterruptedException e){
							e.printStackTrace();
						}
						unlock = false;
					}
					else if (unlock==false) {
						lock.setLock();
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
	 * DoorController
	 * - fonction s'executant dans le Controller, gerant les requetes a destination de Doorlock
	 * - query geres :
	 * 		- get : renvoie la valeur unlock
	 * 		- unlock : unlock vaut true, la serrure est deverouillee
	 * @param : 
	 * - String valueOp : query de la requete recue
	 * - ResponsePrimitive responsein : reponse partiellement construite par le Controller general
	 */
	
	public static ResponsePrimitive DoorController(String valueOp, ResponsePrimitive responsein) {
		ResponsePrimitive response = responsein;
		switch(valueOp){
		case "get":
			response.setContent(ObixUtil.getDoorDataRep(unlock));
			response.setResponseStatusCode(ResponseStatusCode.OK);
			return response;
		case "unlock":
			unlock = true;
			response.setResponseStatusCode(ResponseStatusCode.OK);
			return response;
		default:
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}

}
