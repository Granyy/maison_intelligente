/******************************************************************************/
/*        @TITRE : SensorButton.java                                          */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 23, 2017                                                */
/* @MODIFICATION :                                                            */
/*      @AUTEURS : Gianni D'Amico                                             */
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

public class SensorButton {
	private int m_port ;
	private boolean m_state;
	private Gpio m_gpio;
	
	public SensorButton(int port) {
		m_port = port ;
		m_state = false ;
		m_gpio = new Gpio(port) ;
		m_gpio.dir(Dir.DIR_IN);
	}
	
	public int getPort() {
		return m_port;
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
}

