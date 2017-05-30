package org.eclipse.om2m.home.environment;

import mraa.Gpio;
import mraa.Dir;

/**
 * @author giannidamico
 *
 */
public class LED {
	
	private Gpio m_gpio ;
	private int m_port ;
	private boolean m_state ;
	private String m_ledId ;
	private int m_lumiTh ;
	private boolean m_activate ;
	private boolean m_eventOld ;
	
	
	public LED(int port, String ledId) {
		m_port = port ;
		m_state = false ;
		m_ledId = ledId ;
		m_lumiTh = Integer.MAX_VALUE ;
		m_activate = false ;
		m_eventOld = false ;
		initLed(port);
	}
	
	/**
	 * initLed
	 * Initialise le port GPIO associé à la LED à sa déclaration
	 * @param port
	 */
	public void initLed(int port) {
		m_gpio = new Gpio(port);
		m_gpio.dir(Dir.DIR_OUT);
	}
	
	/**
	 * setThreshold
	 * Pour régler le seuil de luminosité associé à la LED
	 * @param threshold
	 */
	public void setThreshold(int threshold) {
		m_lumiTh = threshold ;
	}

	/**
	 * getThreshold
	 * @return
	 */
	public int getThreshold() {
		return m_lumiTh ;
	}
	
	/**
	 * test_event
	 * @deprecated
	 */
	public void test_event() {
		boolean event ;
		event = m_lumiTh > Luminosite.lumiValue ;
		
		if(m_eventOld != event) {
			m_eventOld=event ;
		}
	}
	
	/**
	 * setPort
	 * @param port
	 */
	public void setPort(int port) {
		m_port = port ;
	}
	
	/**
	 * getPort
	 * @return
	 */
	public int getPort() {
		return m_port ;
	}
	
	/**
	 * Ecrit le gpio à l'état en argument
	 * @param state
	 */
	public void setState(boolean state) {
		if (state==true) {
			m_gpio.write(1);
		} else {
			m_gpio.write(0);
		}
		
		m_state = state ;
	}


	/**
	 * setONThreshold
	 * Fonction principale à appeler pour respecter les preferences d'un utilisateur
	 * Sert à l'activation automatique de la LED dans le cas où l'evenement à
	 * l'instant T est avéré et que la LED fait partie des pref de l'user
	 * Permet d'effectuer des contrôles manuels en parallèle car celle-ci marche sur un évenement
	 */
	public void setONThreshold() {
		boolean event ; 
		event = m_lumiTh > Luminosite.lumiValue ;
		
		if (m_eventOld != event) {
			m_eventOld = event;
			if (event && m_activate){
				m_gpio.write(1);
				m_state = true ;
			}
			else {
				m_gpio.write(0);
				m_state = false ;
			}
		}
	}
	
	/**
	 * setONThresholdProfile
	 * Similaire à setONThreshold mais simplifiée pour être appelé dans set_profile
	 * dès qu'un utilisateur rentre ou sort de l'appartement
	 */
	public void setONThresholdProfile() {
		boolean event ; 
		event = m_lumiTh > Luminosite.lumiValue ;
		
		if (event && m_activate){
			m_gpio.write(1);
			m_state = true ;
		}
		else {
			m_gpio.write(0);
			m_state = false ;
		}
	}
		
	
	/**
	 * setOFF
	 * @deprecated
	 */
	public void setOFF() {
		m_gpio.write(0);
		m_state = false ;
	}
	
	/**
	 * getState
	 * @return state
	 */
	public boolean getState() {
		boolean value ;
		
		if (m_gpio.read() == 1) {
			value = true;
		}
		else {
			value = false ;
		}

		if (m_state != value) {
			m_state = value;
		}
		
		return value ;
	}
	
	/**
	 * getLedId
	 * Retourne l'appId de la LED
	 * @return m_ledId
	 */
	public String getLedId(){
		return m_ledId;
	}
	
	

	/**
	 * setActivate
	 * Fonction pour changer la préférence d'allumage de la LED de l'user
	 * @param activate
	 */
	public void setActivate(boolean activate) {
		m_activate = activate ;
	}
	
	/**
	 * getActivate
	 * @return m_activate
	 */
	public boolean getActivate() {
		return m_activate ;
	}
}
