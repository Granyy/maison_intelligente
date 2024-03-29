package org.eclipse.om2m.home.environment;

import mraa.Gpio;
import mraa.Dir;

public class SensorButton {
	private int m_port ;
	private boolean m_state;
	private Gpio m_gpio;
	
	/**
	 * Constructeur
	 * Besoin du port
	 * @param port
	 */
	public SensorButton(int port) {
		m_port = port ;
		m_state = false ;
		m_gpio = new Gpio(port) ;
		m_gpio.dir(Dir.DIR_IN);
	}
	
	/**
	 * getPort
	 * @return port
	 */
	public int getPort() {
		return m_port;
	}
	
	/**
	 * getState
	 * Lit directement le gpio
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
}
