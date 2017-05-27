/******************************************************************************/
/*        @TITRE : ObixUtil.java                                              */
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
 
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.obix.Bool;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
 
public class ObixUtil {
 
	
	public static String getUserDataRep(User user) {
		Obj obj = new Obj();
		obj.add(new Bool("home", user.isHome()));
		obj.add(new Int("tempTh", user.getTempTh()));
		obj.add(new Int("lumiTh", user.getLumiTh()));
		
		String ledId;
		boolean value;
		Set<Entry<String, Boolean>> setHm = user.getLights().entrySet();
	    Iterator<Entry<String, Boolean>> it = setHm.iterator();
	    while (it.hasNext()) {
	       Entry<String, Boolean> nextTag = it.next();
	       ledId = nextTag.getKey();
	       value = nextTag.getValue();
	       obj.add(new Bool(ledId, value));
	    }
	    
	    obj.add(new Int("red",user.getRGB().getR()));
	    obj.add(new Int("green",user.getRGB().getG()));
	    obj.add(new Int("blue",user.getRGB().getB()));
	    
		return ObixEncoder.toString(obj);
	}
	
	public static String getUserDescriptorRep(String userId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + userId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/" + UserProfile.DATA + "/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
		
		Op opTempUp = new Op();
		opTempUp.setName("Temperature - UP");
		opTempUp.setHref(new Uri(prefix + "?op=tth/u"));
		opTempUp.setIs(new Contract("execute"));
		obj.add(opTempUp);
		
		Op opTempDown = new Op();
		opTempDown.setName("Temperature - DOWN");
		opTempDown.setHref(new Uri(prefix + "?op=tth/d"));
		opTempDown.setIs(new Contract("execute"));
		obj.add(opTempDown);
 
		Op opLumiUp = new Op();
		opLumiUp.setName("Luminosity - UP");
		opLumiUp.setHref(new Uri(prefix + "?op=lth/u"));
		opLumiUp.setIs(new Contract("execute"));
		obj.add(opLumiUp);
		
		Op opLumiDown = new Op();
		opLumiDown.setName("Luminosity - DOWN");
		opLumiDown.setHref(new Uri(prefix + "?op=lth/d"));
		opLumiDown.setIs(new Contract("execute"));
		obj.add(opLumiDown);
 
		return ObixEncoder.toString(obj);
	}
 
	public static String getDoorDataRep(boolean value) {
		Obj obj = new Obj();
		obj.add(new Bool("unlock", value));
		return ObixEncoder.toString(obj);
	}
	
	public static String getDoorDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/" + Doorlock.DATA + "/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
		
		Op opON = new Op();
		opON.setName("UNLOCK");
		opON.setHref(new Uri(prefix + "?op=unlock"));
		opON.setIs(new Contract("execute"));
		obj.add(opON);
		
 
		return ObixEncoder.toString(obj);
	}

 
}