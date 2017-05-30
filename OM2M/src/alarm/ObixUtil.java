package org.eclipse.om2m.home.alarme;
 
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.obix.Bool;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
 
public class ObixUtil {
 
	
	public static String getWindowSensorDataRep(boolean open) {
		Obj obj = new Obj();
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
 
	public static String getBuzzerDataRep(boolean active, boolean value, boolean winForgot) {
		Obj obj = new Obj();
		obj.add(new Bool("active", active));
		obj.add(new Bool("ring", value));
		obj.add(new Bool("windowForgot", winForgot));
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
		
		Op opRing = new Op();
		opRing.setName("RING");
		opRing.setHref(new Uri(prefix + "?op=ring"));
		opRing.setIs(new Contract("execute"));
		obj.add(opRing);
		
		Op opActive = new Op();
		opActive.setName("ACTIVE");
		opActive.setHref(new Uri(prefix + "?op=active"));
		opActive.setIs(new Contract("execute"));
		obj.add(opActive);
 
		Op opWinFor = new Op();
		opWinFor.setName("WINDOW_FORGOT");
		opWinFor.setHref(new Uri(prefix + "?op=winFor"));
		opWinFor.setIs(new Contract("execute"));
		obj.add(opWinFor);
		
		return ObixEncoder.toString(obj);
	}
 
}