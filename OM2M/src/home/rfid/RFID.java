/******************************************************************************/
/*        @TITRE : RFID.java                                                  */
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


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mraa.Result;
import mraa.Uart;
import mraa.UartParity;

public class RFID {
	
	/**
	 * Constante de classe :
	 * - int uTimeout : timeout de la fonction readID en microsecondes
	 */
	
	public static final int uTimeout=1000;
	
	 /**  Variables de classe :
	 *  - Map<String,String> tagRFID : contient les IDs
	 *  - Uart uart : objet pour utiliser l'UART
	 */
	
	public Map<String,String> tagRFID;
	public static Uart uart;
	
	
	/**
	 *  Constructeur RFID()
	 *  - construit une map avec les differentes IDs
	 *  - initialise et configure l'uart utilise pour lire les IDs
	 */
	
	public RFID() {
		uart = new Uart("/dev/ttyMFD1");
		
	    if (uart.setBaudRate(9600) != Result.SUCCESS) {
	        System.err.println("Error setting baud rate");
	        System.exit(1);
	    }
	
	    if (uart.setMode(8, UartParity.UART_PARITY_NONE, 1) != Result.SUCCESS) {
	        System.err.println("Error setting mode");
	        System.exit(1);
	    }
	
	    if (uart.setFlowcontrol(false, false) != Result.SUCCESS) {
	        System.err.println("Error setting flow control");
	        System.exit(1);
	    }
	    
	    if (uart.setTimeout(1, 0, 0) != Result.SUCCESS) {
	    	 System.err.println("Error setting timeout");
		     System.exit(1);
		 }
	    
	    tagRFID = new HashMap<>();
	    tagRFID.put("09002DD48171", "USER1"); //Jaune
	    tagRFID.put("A90052D188A2", "USER2"); //Rouge
	    tagRFID.put("38006895EA2F", "USER3"); //Bleu
	    
	}
	
	/**
	 * readID
	 * - Delais uTimeout pour presenter le tag RFID
	 * - Si aucune donnee recue, renvoi "null"
	 * - Si donnee correspondante a un code a 12 chiffres
	 * 		- Reconnu : renvoi l'ID
	 * 		- Non connu : renvoi "UNAUTHORIZED"
	 * @return ID (string)
	 */
	
	public String readID() {
		String codeRFID = " ";
		String ID = null;
		int valide=0;
		
		if  (uart.dataAvailable(uTimeout)) {
		codeRFID=uart.readStr(13);
		}
		
		if (codeRFID.length()==13) {
		    System.out.println("Lecture RFID : " + codeRFID);
		    Set<Entry<String, String>> setHm = tagRFID.entrySet();
		    Iterator<Entry<String, String>> it = setHm.iterator();
		    while((it.hasNext())&&(valide<8)){
		       valide = 0;
		       Entry<String, String> nextTag = it.next();
		       for(int i=0; i < nextTag.getKey().length(); i++) {
		        	if (nextTag.getKey().charAt(i)==codeRFID.charAt(i+1)) {
		        		valide++;
		        	}
		        }
		       if (valide>8) {
		    	   ID= nextTag.getValue();
		       }
		       else {
		    	   ID = "UNAUTHORIZED";
		       }
		    }
		}
		return ID;
	}

}