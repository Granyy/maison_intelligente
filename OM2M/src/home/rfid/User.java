/******************************************************************************/
/*        @TITRE : User.java                                              */
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
import java.util.Map.*;

import org.eclipse.om2m.home.environment.LED;
import org.eclipse.om2m.home.notifier.Color;

public class User {
	
	/**
	 * Attributs de User
	 * - userId : nom de l'utilisateur
	 * - tempTh : temperature seuil
	 * - lumiTh : luminosite seuil
	 * - home : presence non de l'utilisateur
	 * - lights : Map contenant les preference concernant les leds
	 */
	
	private String userId;
	private int tempTh;
	private int lumiTh;
	private boolean home;
	private Map<String,Boolean> lights;
	private Color RGB;
	private int r;
	private int g;
	private int b;
	
	/**
	 * Constructeur User
	 * - Par defaut, les attributs home=false
	 * @param user : nom de l'utilisateur
	 * @param temp :  temperature seuil
	 * @param lumi : luminosite seuil
	 * @param led : map des differentes leds (physique)
	 */
	
	public User(String user, int temp, int lumi, Map<String,LED> led, Color rgb) {
		userId = user;
		tempTh = temp;
		lumiTh = lumi;
		home = false;
		RGB = new Color(rgb);
		r = 255;
		g = 253;
		b = 252;
		
		lights = new HashMap<>();
		String ledId;
		Set<Entry<String, LED>> setHm = led.entrySet();
	    Iterator<Entry<String, LED>> it = setHm.iterator();
	    while (it.hasNext()) {
	       Entry<String, LED> nextTag = it.next();
	       ledId = nextTag.getValue().getLedId();
	       lights.put(ledId, false);
	    }
	}
	
	/**
	 * getUserId
	 * @return userId
	 */
	
	public String getUserId() {
		return userId;
	}
	
	/**
	 * setUserId
	 * @param user
	 */
	
	public void setUserId(String user) {
		userId = user;
	}
	
	/**
	 * getTempTh
	 * @return tempTh 
	 */
	
	public int getTempTh() {
		return tempTh;
	}

	/**
	 * setTempTh
	 * @param tempTh
	 */
	
	public void setTempTh(int tempTh) {
		this.tempTh = tempTh;
	}
	
	/**
	 * getLumiTh
	 * @return lumiTh
	 */

	public int getLumiTh() {
		return lumiTh;
	}
	
	/**
	 * setLumiTh
	 * @param lumiTh
	 */
	
	public void setLumiTh(int lumiTh) {
		this.lumiTh = lumiTh;
	}

	/**
	 * isHome
	 * @return home
	 */
	
	public boolean isHome() {
		return home;
	}
	
	/**
	 * setHome
	 * @param home
	 */

	public void setHome(boolean home) {
		this.home = home;
	}
	
	/**
	 * getLights
	 * @return lights
	 */

	public Map<String,Boolean> getLights() {
		return lights;
	}
	
	/**
	 * setLights
	 * @param lights
	 */

	public void setLights(Map<String,Boolean> lights) {
		this.lights = lights;
	}

	public Color getRGB() {
		return RGB;
	}

	public void setRGB(Color rGB) {
		RGB.setColor(rGB.getR(), rGB.getG(), rGB.getB());
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
	
}