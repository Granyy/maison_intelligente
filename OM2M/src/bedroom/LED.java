/******************************************************************************/
/*        @TITRE : LED.java                                                   */
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

import mraa.Gpio;
import mraa.Dir;

public class LED {
	
	private Gpio m_gpio ;
	private int m_port ;
	private boolean m_state ;
	private String m_ledId ;
	private boolean m_old_value;
	
	public LED(int port, String ledId) {
		m_port = port ;
		m_state = false ;
		m_ledId = ledId ;
		initLed(port);
	}
	
	public void initLed(int port) {
		m_gpio = new Gpio(port);
		m_gpio.dir(Dir.DIR_OUT);
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
	
	public void setON() {
		m_gpio.write(1);
		m_state = true;
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

	public boolean isOldValue() {
		return m_old_value;
	}

	public void setOldValue(boolean m_old_value) {
		this.m_old_value = m_old_value;
	}
	
}
