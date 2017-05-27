package org.eclipse.om2m.home.alarme;
 
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.obix.Bool;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
 
public class ObixUtil {
 
	
	public static String getWindowSensorDataRep(boolean active, boolean open) {
		Obj obj = new Obj();
		obj.add(new Bool("active", active));
		obj.add(new Bool("intrusion", open));
		return ObixEncoder.toString(obj);
	}
	
	public static String getWindowSensorDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/" + appId + "/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
		
	
		return ObixEncoder.toString(obj);
	}
 
	public static String getBuzzerDataRep(boolean value) {
		Obj obj = new Obj();
		obj.add(new Bool("ring", value));
		return ObixEncoder.toString(obj);
	}
	
	public static String getBuzzerDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/" + Alarm.DATA + "/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
		
		Op opON = new Op();
		opON.setName("RING");
		opON.setHref(new Uri(prefix + "?op=ring"));
		opON.setIs(new Contract("execute"));
		obj.add(opON);
		
 
		return ObixEncoder.toString(obj);
	}
 
}