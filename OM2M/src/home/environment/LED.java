package org.eclipse.om2m.home.environment;

import mraa.Gpio;
import mraa.Dir;

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
	
	public void initLed(int port) {
		m_gpio = new Gpio(port);
		m_gpio.dir(Dir.DIR_OUT);
	}
	
	public void setThreshold(int threshold) {
		m_lumiTh = threshold ;
	}

	public int getThreshold() {
		return m_lumiTh ;
	}
	
	public void test_event() {
		boolean event ;
		event = m_lumiTh > Luminosite.lumiValue ;
		
		if(m_eventOld != event) {
			m_eventOld=event ;
		}
	}
	
	public void setPort(int port) {
		m_port = port ;
	}
	
	public int getPort() {
		return m_port ;
	}
	
	public void setState(boolean state) {
		if (state==true) {
			m_gpio.write(1);
		} else {
			m_gpio.write(0);
		}
		
		m_state = state ;
	}

/* Fonction principale à appeler pour respecter les preferences d'un utilisateur */
	/* Sert à l'activation automatique de la LED dans le cas où l'evenement à */
	/* l'instant T est avéré et que la LED fait partie des pref de l'user*/
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
		
	
	public void setOFF() {
		m_gpio.write(0);
		m_state = false ;
	}
	
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
	
	public String getLedId(){
		return m_ledId;
	}
	
	
/* Fonction pour changer la préférence d'allumage de la LED de l'user  */
	public void setActivate(boolean activate) {
		m_activate = activate ;
	}
	
	public boolean getActivate() {
		return m_activate ;
	}
}
