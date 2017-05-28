/******************************************************************************/
/*        @TITRE : ObixUtil.java                                              */
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

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.obix.Bool;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
 
public class ObixUtil {
 
	public static String getSensorDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/DATA/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
 
		return ObixEncoder.toString(obj);
	}
 
	public static String getLedDescriptorRep(String ledId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + ledId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/DATA/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
 
		Op opON = new Op();
		opON.setName("ON");
		opON.setHref(new Uri(prefix + "?op=true"));
		opON.setIs(new Contract("execute"));
		obj.add(opON);
 
		Op opOFF = new Op();
		opOFF.setName("OFF");
		opOFF.setHref(new Uri(prefix + "?op=false"));
		opOFF.setIs(new Contract("execute"));
		obj.add(opOFF);
 
		return ObixEncoder.toString(obj);
	}
 
	public static String getLedDataRep(boolean value) {
		Obj obj = new Obj();
		obj.add(new Bool("state", value));
		return ObixEncoder.toString(obj);
	}
 
	public static String getSensorDataRep(int value) {
		Obj obj = new Obj();
		obj.add(new Int("data", value));
		return ObixEncoder.toString(obj);
	}
	

	public static String getNightDescriptorRep(String appId, String ipeId) {
	String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
	Obj obj = new Obj();
	
	Op opGet = new Op();
	opGet.setName("Etat ?");
	opGet.setHref(new Uri(prefix + "/DATA/la"));
	opGet.setIs(new Contract("retrieve"));
	obj.add(opGet);
	
	Op opGetDirect = new Op();
	opGetDirect.setName("Etat ? (Direct)");
	opGetDirect.setHref(new Uri(prefix + "?op=get"));
	opGetDirect.setIs(new Contract("execute"));
	obj.add(opGetDirect);
	
	return ObixEncoder.toString(obj);
	}
	
	public static String getNightDataRep(boolean value) {
	Obj obj = new Obj();
	obj.add(new Bool("state", value));
	return ObixEncoder.toString(obj);
	}
	
	public static String getButtonDescriptorRep(String appId, String ipeId) {
	String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
	Obj obj = new Obj();
	
	Op opGet = new Op();
	opGet.setName("Etat ?");
	opGet.setHref(new Uri(prefix + "/DATA/la"));
	opGet.setIs(new Contract("retrieve"));
	obj.add(opGet);
	
	Op opGetDirect = new Op();
	opGetDirect.setName("Etat ? (Direct)");
	opGetDirect.setHref(new Uri(prefix + "?op=get"));
	opGetDirect.setIs(new Contract("execute"));
	obj.add(opGetDirect);
	
	return ObixEncoder.toString(obj);
	}
	
	public static String getButtonDataRep(boolean value) {
	Obj obj = new Obj();
	obj.add(new Bool("etat", value));
	return ObixEncoder.toString(obj);
	}
 
}
