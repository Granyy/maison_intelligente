/******************************************************************************/
/*        @TITRE : LedRGB.java                                                */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 17, 2017                                                */
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

package org.eclipse.om2m.home.notifier;

public class LedRGB {
	
	 /**  Attributs:
	 *  - upm_p9813.P9813 led : reference a la led RGB
	 *  - color : couleur de la led RGB
	 *  - boolean state : led allumee (true) ou eteinte (false)
	 */
	
	private upm_p9813.P9813 led;
	private boolean state;
	private Color color;
	
	/**
	 * Constructeur
	 * - met par défaut la couleur à blanc et allume la led
	 * @param clock
	 * @param data
	 */
	
	public LedRGB(int clock, int data) {
		led = new upm_p9813.P9813(1, clock, data, false);
		color = new Color(255,255,255);
		off();
	}
	
	/**
	 * setLedColor
	 *  - met à jours l'attribut couleur
	 * @param col
	 */
	
	public void setLedColor(Color col) {
		color.setColor(col);
	}
	
	/**
	 * ledOn
	 * - allume la led
	 * - met state à true
	 */
	
	public void on() {
		led.setAllLeds((short)color.getR(), (short)color.getG(), (short)color.getB());
		setState(true);
	}
	
	/**
	 * ledOff
	 * - eteint la led
	 * - met state à false
	 */
	
	public void off() {
		led.setAllLeds((short)0, (short)0, (short)0);
		setState(false);
	}
	
	/**
	 * setState
	 * @param b
	 */

	public void setState(boolean b) {
		state = b;
	}
	
	/**
	 * isState
	 * @return state
	 */

	public boolean isState() {
		return state;
	}
	
	/**
	 * getColor
	 * @return color
	 */
	
	public Color getColor() {
		return color;
	}
	
	/**
	 * setColor
	 * @param color
	 */

	public void setColor(Color color) {
		this.color = color;
	}

}
