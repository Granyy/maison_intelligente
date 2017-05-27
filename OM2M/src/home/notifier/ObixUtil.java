/******************************************************************************/
/*        @TITRE : ObixUtil.java                                              */
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

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
 
public class ObixUtil {

 
	public static String getNotifDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/" + Notifier.DATA + "/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
 
		return ObixEncoder.toString(obj);
	}
 
	public static String getNotifDataRep(int value) {
		Obj obj = new Obj();
		obj.add(new Int("Unread emails", value));
		return ObixEncoder.toString(obj);
	}
	
	public static String getNotifDataRep(int value, Color col) {
		Obj obj = new Obj();
		obj.add(new Int("Unread emails", value));
	    obj.add(new Int("red",col.getR()));
	    obj.add(new Int("green",col.getG()));
	    obj.add(new Int("blue",col.getB()));
		return ObixEncoder.toString(obj);
	}
}