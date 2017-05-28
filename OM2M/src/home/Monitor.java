package org.eclipse.om2m.home;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.home.alarme.Alarm;
import org.eclipse.om2m.home.alarme.Alarm.BuzzerListener;
import org.eclipse.om2m.home.alarme.WindowSensor;
import org.eclipse.om2m.home.alarme.WindowSensor.WindowSensorListener;
import org.eclipse.om2m.home.environment.Temperature;
import org.eclipse.om2m.home.environment.Ventilateur;
import org.eclipse.om2m.home.environment.Temperature.TemperatureListener;
import org.eclipse.om2m.home.environment.Ventilateur.FanListener;
import org.eclipse.om2m.home.environment.Luminosite;
import org.eclipse.om2m.home.environment.Luminosite.LuminositeListener;
import org.eclipse.om2m.home.environment.Light;
import org.eclipse.om2m.home.environment.Light.LightListener;
import org.eclipse.om2m.home.environment.ButtonLed;
import org.eclipse.om2m.home.environment.ButtonLed.ButtonListener;
import org.eclipse.om2m.home.notifier.Notifier;
import org.eclipse.om2m.home.notifier.Notifier.NotifierListener;
import org.eclipse.om2m.home.rfid.Doorlock;
import org.eclipse.om2m.home.rfid.UserProfile;
import org.eclipse.om2m.home.rfid.Doorlock.DoorlockListener;
import org.eclipse.om2m.home.rfid.UserProfile.UserListener;
 
public class Monitor {
 
	static CseService CSE;
	static String CSE_ID = Constants.CSE_ID;
	static String CSE_NAME = Constants.CSE_NAME;
	static String REQUEST_ENTITY = Constants.ADMIN_REQUESTING_ENTITY;
	public static String ipeId = "app";
 
	private NotifierListener notifierListener;
	private DoorlockListener doorlockListener;
	private UserListener userListener;
	private TemperatureListener temperatureListener;
	private FanListener fanListener;
	private LightListener lightListener;
	private LuminositeListener luminositeListener;
	private ButtonListener buttonListener;
	private WindowSensorListener windowSensorListener;
	private BuzzerListener buzzerListener;
 
	public Monitor(CseService cseService){
		CSE = cseService;
	}
	
	public void listenToUser(){
		userListener = new UserListener();
		userListener.start();
	}
 
	public void listenToDoorlock(){
		doorlockListener = new DoorlockListener();
		doorlockListener.start();
	}
	
	public void listenToNotifier(){
		notifierListener = new NotifierListener();
		notifierListener.start();
	}
	
	public void listenToTemperature(){
		temperatureListener = new TemperatureListener();
		temperatureListener.start();
	}
	
	public void listenToFan(){
		fanListener= new FanListener();
		fanListener.start();
	}
	
	public void listenToLight(){
		lightListener = new LightListener();
		lightListener.start();
	}
	
	public void listenToLuminosite() {
		luminositeListener = new LuminositeListener();
		luminositeListener.start();
	}
	
	public void listenToButton() {
		buttonListener = new ButtonListener();
		buttonListener.start();
	}
	
	public void listenToWindow(){
		windowSensorListener = new WindowSensorListener();
		windowSensorListener.start();
	}
	
	public void listenToBuzzer(){
		buzzerListener = new BuzzerListener();
		buzzerListener.start();
	}
 
	public void start(){
		Ventilateur.createFanResources();
		Temperature.createTemperatureResources();
		Luminosite.createLuminositeResources();
		Light.createLightsResources();
		Notifier.createNotifierResource();
		UserProfile.createProfileResources();
		Doorlock.createDoorlockResources();
		ButtonLed.createButtonResources();
		WindowSensor.createIRSensorResources();
		Alarm.createAlarmResources();
		
		listenToTemperature();
		listenToFan();
		listenToLuminosite();
		listenToButton();
		listenToLight();
		listenToUser();
		listenToDoorlock();
		listenToNotifier();
		listenToWindow();
		listenToBuzzer();
	}
 
	public void stop(){
		if(temperatureListener != null && temperatureListener.isAlive()){
			temperatureListener.stopThread();
		}
		if(fanListener != null && fanListener.isAlive()){
			fanListener.stopThread();
		}
		if(luminositeListener != null && luminositeListener.isAlive()){
			luminositeListener.stopThread();
		}
		if(lightListener != null && lightListener.isAlive()){
			lightListener.stopThread();
		}
		if(userListener != null && userListener.isAlive()){
			userListener.stopThread();
		}
		if(doorlockListener != null && doorlockListener.isAlive()){
			doorlockListener.stopThread();
		}
		if(buttonListener != null && buttonListener.isAlive()){
			buttonListener.stopThread();
		}
		if(windowSensorListener != null && windowSensorListener.isAlive()){
			windowSensorListener.stopThread();
		}
		if(buzzerListener != null && buzzerListener.isAlive()){
			buzzerListener.stopThread();
		}
	}
 
}
