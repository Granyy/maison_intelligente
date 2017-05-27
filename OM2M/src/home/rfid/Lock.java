/******************************************************************************/
/*        @TITRE : Lock.java                                                  */
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

public class Lock {
	
	/**
	 * Constantes de classe :
	 * - int locked : position en degres du servo en position fermee
	 * - int unlocked : position en degres du servo en position ouverte
	 */
	
	static final int locked = 0;
	static final int unlocked = 90;
	
	 /**  Attribut :
	 *  - pm_servo.ES08A servo : reference au servo
	 */
	
	static upm_servo.ES08A servo;
	
	/**
	 *  Constructeur Lock()
	 *  - initialise un servo sur la pin 5
	 */
	
	Lock()
	{
		servo = new upm_servo.ES08A(5);
	}
	
	/**
	 *  Constructeur Lock(int pin)
	 *  - initialise un servo sur la pin en parametre
	 *  @param : int pin
	 */
	
	Lock(int pin)
	{
		servo = new upm_servo.ES08A(pin);
	}
	
	/**
	 * setUnlock()
	 * - positionne le servo sur la position ouverte
	 */
	
	void setUnlock() {
		servo.setAngle(unlocked);
	}
	
	/**
	 * setUnlock()
	 * - positionne le servo sur la position ferme
	 */
	
	void setLock() {
		servo.setAngle(locked);
	}

}
