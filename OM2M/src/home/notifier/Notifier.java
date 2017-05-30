/******************************************************************************/
/*        @TITRE : Notifier.java                                              */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 17, 2017                                                */
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

package org.eclipse.om2m.home.notifier;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;


public class Notifier {
	
	/**
	 * Constantes de classe :
	 * - String DESCRIPTOR : ID du descriptor container
	 * - String DATA : ID du data container
	 * - String appId : ID de l'AE
	 */
	
	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "NOTIFICATIONS";
	public static String appId = "NOTIFIER";
	
	/**
	 * Variables de classe :
	 * - LedRGB ledRGB : reference a la led RGB 
	 * - nrOfEmail : nombre d'emails non lus
	 */
	
	public static LedRGB ledRGB;
	static int nrOfEmail = 0;
	
	/**
	 * createNotifierResource()
	 * - cree un AE
	 * - cree un data container
	 * - cree un descriptor container
	 * - les noms sont configures a partir des differentes constantes de classe
	 * - instancie la variable ledRGB qui fait reference a la led RGB
	 */
	
	public static void createNotifierResource(){
		String content;
	       
	    // Create the AE
	    ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, appId);
	       
	    if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
	    	   	// Create the container
	    	   	RessourceManager.createContainer(appId, DESCRIPTOR, DATA, 1);
				
				// Create the DATA contentInstance
				content = ObixUtil.getNotifDataRep(0);
				RessourceManager.createDataContentInstance(appId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getNotifDescriptorRep(appId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, appId, DESCRIPTOR, content);
	     }
	    
	    ledRGB = new LedRGB(8,9);
	}
	
	/**
	 * NotifierListener
	 * - fonction principale s'executant dans le monitor
	 * - surveille les changements de la variable nrOfEmail
	 * - si nrOfEmail>0 : fait clignoter la led RGB
	 */
	
	
	public static class NotifierListener extends Thread{
		 
		private boolean running = true;
		private boolean notif = false;
		private int memorizedNrofEmail = 0;
		private boolean toggle = false;
 
		@Override
		public void run() {
			
			while(running){
				ledRGB.off();
				if(memorizedNrofEmail != nrOfEmail){
					// Memorize the new door state
					memorizedNrofEmail = nrOfEmail;
					// Create a data contentInstance
					String content = ObixUtil.getNotifDataRep(nrOfEmail, ledRGB.getColor());
					RessourceManager.createDataContentInstance(appId, DATA, content);
					if (nrOfEmail>0) notif = true;
					else notif = false;
				}
				
				if (notif&&toggle){
					ledRGB.on();
					toggle = false;
				}
				else {
					toggle = true;
				}
				
				try{
					Thread.sleep(2000);
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
	 * NotifierController
	 * - query pris en charge
	 * 		- get : renvoi le nombre d'emails non lus
	 * 		- email/n : fixe le nombre d'emails sur n
	 * 		- rgb/(int)/(int)/(int) : parametre de la couleur de la ledRGB
	 * @param valueOp
	 * @param responsein
	 * @return response
	 */
	
	public static ResponsePrimitive NotifierController(String valueOp, ResponsePrimitive responsein) {
		ResponsePrimitive response = responsein;
		String[] query = valueOp.split("/");
		String op = query[0];
		String value = "";
		if (query.length==2) value = query[1];
		switch(op){
		case "get":
			response.setContent(ObixUtil.getNotifDataRep(nrOfEmail, ledRGB.getColor()));
			response.setResponseStatusCode(ResponseStatusCode.OK);
			return response;
		case "email":
			if (value.matches("^\\p{Digit}+$")){
				nrOfEmail = (Integer.parseInt(value));
			} 
			response.setResponseStatusCode(ResponseStatusCode.OK);
			return response;
		case "rgb":
			if ((query.length==4)&&((query[1].matches("^\\p{Digit}+$")))) {
				Color col = new Color(Integer.parseInt(query[1]), Integer.parseInt(query[2]), Integer.parseInt(query[3]));
				ledRGB.setLedColor(col);
				response.setResponseStatusCode(ResponseStatusCode.OK);
				String content = ObixUtil.getNotifDataRep(nrOfEmail,ledRGB.getColor());
				RessourceManager.createDataContentInstance(appId, DATA, content);
				return response;

			} else {
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				return response;
			}
		default:
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}

}
