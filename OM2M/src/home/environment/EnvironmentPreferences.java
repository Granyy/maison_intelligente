package org.eclipse.om2m.home.environment;

import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.home.notifier.Notifier;
import org.eclipse.om2m.home.rfid.User;
import org.eclipse.om2m.home.environment.Ventilateur;


public class EnvironmentPreferences {

	/**
	 * setProfile
	 * Fonction permettant de mettre a jour toutes les variables globales en fonction des preferences de l'user en argument
	 * - tempTh
	 * - lumiTh
	 * - pref des Led
	 * - pref couleur ledRGB
	 * 
	 * @param user
	 */
	public static void setProfile(User user) {
		Ventilateur.tempTh = user.getTempTh() ;
		Light.lumiTh = user.getLumiTh() ;
		Light.Led1.setActivate(user.getLights().get("LED1"));
		Light.led1Pref = user.getLights().get("LED1");
		Light.Led2.setActivate(user.getLights().get("LED2"));
		Light.led2Pref = user.getLights().get("LED2");
		Notifier.ledRGB.setLedColor(user.getRGB());
		
		LED Led ;
		Set<Entry<String, LED>> setHm = Light.lights.entrySet();
	    Iterator<Entry<String, LED>> it = setHm.iterator();
	    while (it.hasNext()) {
	       Entry<String, LED> nextTag = it.next();
	       Led = nextTag.getValue();
	       Led.setONThresholdProfile();
	    }
	    
	}
	
	
}
