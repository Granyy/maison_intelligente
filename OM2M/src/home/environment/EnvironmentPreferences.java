package org.eclipse.om2m.home.environment;

import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.home.rfid.User;
import org.eclipse.om2m.home.environment.Ventilateur;

public class EnvironmentPreferences {

	public static void setProfile(User user) {
		Ventilateur.tempTh = user.getTempTh() ;
		Light.lumiTh = user.getLumiTh() ;
		Light.Led1.setActivate(user.getLights().get("LED1"));
		Light.led1Pref = user.getLights().get("LED1");
		Light.Led2.setActivate(user.getLights().get("LED2"));
		Light.led2Pref = user.getLights().get("LED2");
		
		
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
