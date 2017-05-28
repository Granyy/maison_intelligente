/******************************************************************************/
/*        @TITRE : Monitor.java                                               */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 23, 2017                                                */
/* @MODIFICATION :                                                            */
/*      @AUTEURS : Gianni D'Amico & Leo Granier                               */
/*    @COPYRIGHT : Copyright (c) 2017                                         */
/*                 Paul GUIRBAL                                               */
/*                 Joram FILLOL-CARLINI                                       */
/*                 Gianni D'AMICO                                             */
/*                 Matthieu BOUGEARD                                          */
/*                 Leo GRANIER                                                */
/*      @LICENSE : MIT License (MIT)                                          */
/******************************************************************************/

package org.eclipse.om2m.bedroom;

import org.eclipse.om2m.bedroom.ButtonLed.ButtonListener;
import org.eclipse.om2m.bedroom.Light.LightListener;
import org.eclipse.om2m.bedroom.Night.NightListener;
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.core.service.CseService;
 
public class Monitor {
 
	static CseService CSE;
	static String CSE_ID = Constants.CSE_ID;
	static String CSE_NAME = Constants.CSE_NAME;
	static String REQUEST_ENTITY = Constants.ADMIN_REQUESTING_ENTITY;
	static String ipeId = "bedroom";
 
	private NightListener nightListener;
	private LightListener lightListener;
	private ButtonListener buttonListener;
 
	public Monitor(CseService cseService){
		CSE = cseService;
	}
 
	public void start(){
		Night.createNightResources();
		Light.createLightsResources();
		ButtonLed.createButtonResources();
		listenToNight();
		listenToLight();
		listenToButton();
	}
 
	public void stop(){
		if(nightListener != null && nightListener.isAlive()){
			nightListener.stopThread();
		}
		if(lightListener != null && lightListener.isAlive()){
			lightListener.stopThread();
		}
		if(buttonListener != null && buttonListener.isAlive()){
			buttonListener.stopThread();
		}
	}
 
 
	public void listenToNight(){
		nightListener = new NightListener();
		nightListener.start();
	}
 
	public void listenToLight(){
		lightListener = new LightListener();
		lightListener.start();
	}
	
	public void listenToButton(){
		buttonListener = new ButtonListener();
		buttonListener.start();
	}
 
 
	
}