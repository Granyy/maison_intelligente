package org.eclipse.om2m.home.environment;
 
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Real;
import org.eclipse.om2m.commons.obix.Bool;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
 
public class ObixUtil {
 
	
	
							/*						*/
							/*		Temperature		*/
							/*						*/
	
	public static String getTemperatureDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("Temperature ?");
		opGet.setHref(new Uri(prefix + "/DATA/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("Temperature ? (Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
 
		return ObixEncoder.toString(obj);
	}
	
	
	public static String getSensorDataRep(double value) {
		Obj obj = new Obj();
		obj.add(new Real("temp", value));
		return ObixEncoder.toString(obj);
	}
	
	
	
	
	
							/*						*/
							/*		Ventilateur		*/
							/*						*/
	
	public static String getFanDescriptorRep(String appId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
 
		Op opGet = new Op();
		opGet.setName("Quel niveau?");
		opGet.setHref(new Uri(prefix + "/DATA/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
 
		Op opGetDirect = new Op();
		opGetDirect.setName("Quel niveau? (Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
 
		Op level1 = new Op();
		level1.setName("Une caresse");
		level1.setHref(new Uri(prefix + "?op=level1"));
		level1.setIs(new Contract("execute"));
		obj.add(level1);
		
		Op level2 = new Op();
		level2.setName("Une brise");
		level2.setHref(new Uri(prefix + "?op=level2"));
		level2.setIs(new Contract("execute"));
		obj.add(level2);
		
		Op level3 = new Op();
		level3.setName("Une tempete");
		level3.setHref(new Uri(prefix + "?op=level3"));
		level3.setIs(new Contract("execute"));
		obj.add(level3);
 
		Op opOFF = new Op();
		opOFF.setName("Stop");
		opOFF.setHref(new Uri(prefix + "?op=false"));
		opOFF.setIs(new Contract("execute"));
		obj.add(opOFF);
		
		Op opTempUp = new Op();
		opTempUp.setName("Threshold - UP");
		opTempUp.setHref(new Uri(prefix + "?op=thr_up"));
		opTempUp.setIs(new Contract("execute"));
		obj.add(opTempUp);
		
		Op opTempDown = new Op();
		opTempDown.setName("Threshold - DOWN");
		opTempDown.setHref(new Uri(prefix + "?op=thr_down"));
		opTempDown.setIs(new Contract("execute"));
		obj.add(opTempDown);
 
		return ObixEncoder.toString(obj);
	}
	
	public static String getFanDataRep(int value) {
		Obj obj = new Obj();
		obj.add(new Int("fanLevel", value));
		obj.add(new Real("tempTh",Ventilateur.clim.getThreshold()));
		return ObixEncoder.toString(obj);
	}
 
	
	
						/*						*/
						/*			LED			*/
						/*						*/
	
	public static String getLedDescriptorRep(String ledId, String ipeId)  {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + ledId;
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
		
		Op opTrue = new Op();
		opTrue.setName("Allumer");
		opTrue.setHref(new Uri(prefix + "?op=true"));
		opTrue.setIs(new Contract("execute"));
		obj.add(opTrue);
		
		Op opFalse = new Op();
		opFalse.setName("Eteindre");
		opFalse.setHref(new Uri(prefix + "?op=false"));
		opFalse.setIs(new Contract("execute"));
		obj.add(opFalse);
		
		Op opALLTrue = new Op();
		opALLTrue.setName("Allumer tout");
		opALLTrue.setHref(new Uri(prefix + "?op=ALLtrue"));
		opALLTrue.setIs(new Contract("execute"));
		obj.add(opALLTrue);
		
		Op opALLFalse = new Op();
		opALLFalse.setName("Eteindre tout");
		opALLFalse.setHref(new Uri(prefix + "?op=ALLfalse"));
		opALLFalse.setIs(new Contract("execute"));
		obj.add(opALLFalse);
		
		Op opLumiUp = new Op();
		opLumiUp.setName("Threshold - UP");
		opLumiUp.setHref(new Uri(prefix + "?op=thr_up"));
		opLumiUp.setIs(new Contract("execute"));
		obj.add(opLumiUp);
		
		Op opLumiDown = new Op();
		opLumiDown.setName("Threshold - DOWN");
		opLumiDown.setHref(new Uri(prefix + "?op=thr_down"));
		opLumiDown.setIs(new Contract("execute"));
		obj.add(opLumiDown);
		
		return ObixEncoder.toString(obj);
	}
 
	
	
	public static String getLedDataRep(LED led) {
		Obj obj = new Obj();
		obj.add(new Bool("state", led.getState())); 
		obj.add(new Real("lumiTh",led.getThreshold()));
		obj.add(new Bool("activated",led.getActivate()));
		return ObixEncoder.toString(obj);
	}
 



								/*						*/
								/*		Luminosite		*/
								/*						*/

	public static String getLuminositeDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();
	
		Op opGet = new Op();
		opGet.setName("Luminosite ?");
		opGet.setHref(new Uri(prefix + "/DATA/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);
	
		Op opGetDirect = new Op();
		opGetDirect.setName("Luminosite ? (Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=get"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);
	
		return ObixEncoder.toString(obj);
	}
	
	public static String getLumiDataRep(int value) {
		Obj obj = new Obj();
		obj.add(new Int("lumi", value));
		return ObixEncoder.toString(obj);
	}
		
									/*						*/
									/*		 Bouton			*/
									/*						*/

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