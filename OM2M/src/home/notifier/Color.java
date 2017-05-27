/******************************************************************************/
/*        @TITRE : Color.java                                                */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 20, 2017                                                */
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

public class Color {
	
	
	 /**  Attributs:
	 *  - int r : rouge
	 *  - int g : vert
	 *  - int b : bleu
	 */
	
	
	private int r;
	private int g;
	private int b;
	
	/**
	 * Constructeur Color
	 * @param r
	 * @param g
	 * @param b
	 */
	
	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Constructeur Color
	 * @param color
	 */
	
	public Color(Color color) {
		this.r = color.getR();
		this.g = color.getG();
		this.b = color.getB();
	}
	
	/**
	 * setColor
	 * @param r
	 * @param g
	 * @param b
	 */
	
	public void setColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * setColor
	 * @param col
	 */
	
	public void setColor(Color col) {
		this.r = col.r;
		this.g = col.g;
		this.b = col.b;
	}
	
	/**
	 * getR
	 * @return r
	 */
	
	public int getR() {
		return r;
	}
	
	/**
	 * getG
	 * @return g
	 */
	
	public int getG() {
		return g;
	}
	
	/**
	 * getB
	 * @return b
	 */
	
	public int getB() {
		return b;
	}
	
}
