/******************************************************************************/
/*        @TITRE : UserProfile.java                                           */
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


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.Monitor;
import org.eclipse.om2m.home.RessourceManager;
import org.eclipse.om2m.home.environment.EnvironmentPreferences;
import org.eclipse.om2m.home.environment.Light;
import org.eclipse.om2m.home.notifier.Color;


public class UserProfile {
	
	/**
	 * Constantes de classe :
	 * - String DESCRIPTOR : ID du descriptor container
	 * - String DATA : ID du data container
	 * - int TEMPTHD : Temperature seuil par defaut d'un profil utilisateur
	 * - int LUMITHD : Luminosite seuil par defaut d'un profil utilisateur
	 */

	static String DESCRIPTOR = "DESCRIPTOR";
	static String DATA = "PROFILE";
	static final int TEMPTHD = 20;
	static final int LUMITHD = 20;
	static final Color RGBD = new Color(255,255,255);
	
	
	/**
	 * Variables de classe :
	 * - RFID : reference au capteur RFID
	 * - Map<String, User> profile : contient les profils des utilisateurs ranges par nom
	 * - User USER1, USER2, USER3 : panel d'utilisateurs qui va �tre instancie dans Map
	 * - User GENERAL : profil general
	 */
	
	static RFID sRFID;
	
	public static Map<String, User> profile;
	public static User USER1;
	public static User USER2;
	public static User USER3;
	public static User GENERAL;
	public static User NOBODY;


	/**
	 * createUserMap()
	 * - instancie les differents utilisateurs avec le profil par defaut
	 * - range les utilisateurs dans la Map profile
	 */
	
	public static void createUserMap() {
		USER1 = new User("USER1",TEMPTHD,LUMITHD,Light.lights,RGBD);
		USER2 = new User("USER2",TEMPTHD,LUMITHD,Light.lights,RGBD);
		USER3 = new User("USER3",TEMPTHD,LUMITHD,Light.lights,RGBD);
		GENERAL = new User("GENERAL",TEMPTHD,LUMITHD, Light.lights,RGBD);
		NOBODY = new User("NOBODY", TEMPTHD, LUMITHD, Light.lights,RGBD);
		profile = new HashMap<>();
		profile.put(USER1.getUserId(),USER1);
		profile.put(USER2.getUserId(),USER2);
		profile.put(USER3.getUserId(),USER3);
		profile.put(GENERAL.getUserId(),GENERAL);
		profile.put(NOBODY.getUserId(),NOBODY);
	}
	
	/**
	 * isSomeoneHome()
	 * - si un utilisateur est a la maison renvoie true
	 * - si aucune utilisateur a la maison renvoie false
	 * @return someoneHome (boolean)
	 */
	
	public static boolean isSomeoneHome() {
		boolean someoneHome = false;
		someoneHome = !(profile.get("NOBODY").isHome());
	    return someoneHome;
	}
	
	/**
	 * howManyHome()
	 * - renvoie le nombre d'utilisateurs present a la maison
	 * @return nrAtHome (int)
	 */
	
	public static int howManyHome() {
		User user;
		int nrAtHome = 0;
		
		Set<Entry<String, User>> setHm = profile.entrySet();
	    Iterator<Entry<String, User>> it = setHm.iterator();
	    while (it.hasNext()) {
	       Entry<String, User> nextTag = it.next();
	       user = nextTag.getValue();
	       if ((user.isHome())&&(user.getUserId()!="NOBODY")&&(user.getUserId()!="GENERAL")) {
	    	   nrAtHome++;
	       }
	    }
	    return nrAtHome;
	}
	
	/**
	 * manageProfile
	 * - active le profil qui correspond lors de l'entree/sortie d'un utilisateur
	 * @param ID
	 * @param state
	 */
	
	public static void manageProfile(String ID, boolean state) {
		int nrAtHome = howManyHome();
		
		if (nrAtHome==0) {
			EnvironmentPreferences.setProfile(profile.get("NOBODY"));
			profile.get("NOBODY").setHome(true);
			String content = ObixUtil.getUserDataRep(profile.get("NOBODY"));
			RessourceManager.createDataContentInstance("NOBODY", DATA, content);
			
		}
		else if ((nrAtHome==1)) {
			if (state) {
				EnvironmentPreferences.setProfile(profile.get(ID));
			}
			if (profile.get("NOBODY").isHome()) {
				profile.get("NOBODY").setHome(false);
				String content = ObixUtil.getUserDataRep(profile.get("NOBODY"));
				RessourceManager.createDataContentInstance("NOBODY", DATA, content);
			}
			if (profile.get("GENERAL").isHome()) {
				profile.get("GENERAL").setHome(false);
				String content = ObixUtil.getUserDataRep(profile.get("GENERAL"));
				RessourceManager.createDataContentInstance("GENERAL", DATA, content);
			}	
		}
		else if ((nrAtHome>1)&&state) {
			EnvironmentPreferences.setProfile(profile.get("GENERAL"));
			profile.get("GENERAL").setHome(true);
			String content = ObixUtil.getUserDataRep(profile.get("GENERAL"));
			RessourceManager.createDataContentInstance("GENERAL", DATA, content);
		}
		
	}
	
	/**
	 * createProfileResources()
	 * - Par utilisateur :
	 * 		- cree un AE
	 * 		- cree un data container
	 * 		- cree un descriptor container
	 * - Instancie le capteur RFID
	 * - Parametre les valeurs pour le profil NOBODY
	 */
	
	public static void createProfileResources(){
		
		String content, userId;
		User user;
		
		createUserMap();
		sRFID = new RFID();
		
		Set<Entry<String, User>> setHm = profile.entrySet();
	    Iterator<Entry<String, User>> it = setHm.iterator();
	    while (it.hasNext()) {
	       Entry<String, User> nextTag = it.next();
	       user = nextTag.getValue();
	       userId = nextTag.getValue().getUserId();
	       
		       // Create the AE
		   ResponsePrimitive response = RessourceManager.createAE(Monitor.ipeId, userId);
		   
		   if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
			   	// Create the container
			   	RessourceManager.createContainer(userId, DESCRIPTOR, DATA, 5);
				
				// Create the DATA contentInstance
				content = ObixUtil.getUserDataRep(user);
				RessourceManager.createDataContentInstance(userId, DATA, content);
				
				// Create the DESCRIPTOR contentInstance
				content = ObixUtil.getUserDescriptorRep(userId, Monitor.ipeId);
				RessourceManager.createDescriptionContentInstance(Monitor.ipeId, userId, DESCRIPTOR,content);
			} 
		   
		   manageProfile("NOBODY", false);
		}
		}
		
		/**
		 * UserListener
		 * - fonction principale s'executant dans le monitor
		 * - lis le capteur RFID regulierement 
		 * - si jamais un utilisateur est reconnu :
		 * 		- deverouille la serrure (Doorlock.unlock=true)
		 * 		- change le statut dans le profil de l'utilisateur (present/absent)
		 */
	
		public static class UserListener extends Thread{
			 
			private boolean running = true;
	 
			@Override
			public void run() {
				
				while(running){
					String ID;
					ID = sRFID.readID();
					if (profile.containsKey(ID)) {
					Doorlock.unlock = true;
					Boolean state =! profile.get(ID).isHome();
					profile.get(ID).setHome(state);
					manageProfile(ID, state);
					// Create the data contentInstance
					String content = ObixUtil.getUserDataRep(profile.get(ID));
					RessourceManager.createDataContentInstance(ID, DATA, content);
					
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
		
		/**
		 * UserController
		 * - fonction s'executant dans le Controller, gerant les requ�tes � destination de UserProfile
		 * - query geres :
		 * 		- get : renvoie le profil de l'utilisateur
		 * 		- tthu ou tthd : augmente (u) ou diminue (d) la temperature seuil dans le profil
		 * 		- tth/(int) : parametre la valeur de la temperature seuil � (int)
		 * 		- lthu ou lthd : augmente (u) ou diminue (d) la luminosite seuil dans le profil
		 * 		- lth/(int) : parametre la valeur de la luminosite seuil � (int)
		 * 		- ledId/true ou ledId/false : parametre la valeur de la ledId dans le profil
		 * 		- rgb/(int)/(int)/(int) : parametre de la couleur de la ledRGB
		 * @param userId : nom de l'utilisateur
		 * @param valueOp : query de la requ�te re�ue
		 * @param responsein : reponse partiellement construite par le Controller general
		 */
		
		public static ResponsePrimitive UserController(String userId, String valueOp, ResponsePrimitive responsein) {
			ResponsePrimitive response = responsein;
			String[] query = valueOp.split("/");
			String op = query[0];
			String value = "";
			if (query.length==2) value = query[1];
			String content;
			User user = UserProfile.profile.get(userId);
			
			switch(op){
			case "get":
				response.setContent(ObixUtil.getUserDataRep(user));
				response.setResponseStatusCode(ResponseStatusCode.OK);
				return response;
			case "tth":
				int tempTh = user.getTempTh();
				if(value.equals("u")){
					UserProfile.profile.get(userId).setTempTh(tempTh+1);
				} else if (value.equals("d")){
					UserProfile.profile.get(userId).setTempTh(tempTh-1);
				} else if (value.matches("^\\p{Digit}+$")){
					UserProfile.profile.get(userId).setTempTh(Integer.parseInt(value));
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
					return response;
				}
				response.setResponseStatusCode(ResponseStatusCode.OK);
				content = ObixUtil.getUserDataRep(UserProfile.profile.get(userId));
				RessourceManager.createDataContentInstance(userId, DATA, content);
				return response;
			case "lth":
				int lumiTh = user.getLumiTh();
				if(value.equals("u")){
					UserProfile.profile.get(userId).setLumiTh(lumiTh+1);
				} else if (value.equals("d")){
					UserProfile.profile.get(userId).setLumiTh(lumiTh-1);
				} else if (value.matches("^\\p{Digit}+$")){
					UserProfile.profile.get(userId).setLumiTh(Integer.parseInt(value));
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
					return response;
				}
				response.setResponseStatusCode(ResponseStatusCode.OK);
				content = ObixUtil.getUserDataRep(UserProfile.profile.get(userId));
				RessourceManager.createDataContentInstance(userId, DATA, content);
				return response;
			case "rgb":
				if ((query.length==4)&&((query[1].matches("^\\p{Digit}+$")))) {
					UserProfile.profile.get(userId).getRGB().setColor(Integer.parseInt(query[1]), Integer.parseInt(query[2]), Integer.parseInt(query[3]));
					response.setResponseStatusCode(ResponseStatusCode.OK);
					content = ObixUtil.getUserDataRep(UserProfile.profile.get(userId));
					RessourceManager.createDataContentInstance(userId, DATA, content);
					return response;
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
					return response;
				}
			default:
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
			
			if (user.getLights().containsKey(op)) {
				Map<String, Boolean> lights = user.getLights();
				lights.put(op, Boolean.parseBoolean(value));
				UserProfile.profile.get(userId).setLights(lights);
				response.setResponseStatusCode(ResponseStatusCode.OK);
				content = ObixUtil.getUserDataRep(UserProfile.profile.get(userId));
				RessourceManager.createDataContentInstance(userId, DATA, content);
			}
			
			return response;
		}
		

}
