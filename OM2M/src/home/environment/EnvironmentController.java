package org.eclipse.om2m.home.environment;
 
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
 
public class EnvironmentController {
	
	static boolean old_toggle = false ;
 
	public static ResponsePrimitive Controller(String appId, String valueOp, ResponsePrimitive responsein) {
		ResponsePrimitive response = responsein;
			switch(valueOp){
			case "get":
				if(appId.equals(Temperature.appId)){
					response.setContent(ObixUtil.getSensorDataRep(Temperature.tempValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (appId.equals(Ventilateur.appId)){
					response.setContent(ObixUtil.getFanDataRep(Ventilateur.fanLevel));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if(appId.equals(ButtonLed.appId)){
					response.setContent(ObixUtil.getButtonDataRep(ButtonLed.buttonValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (appId.equals(Light.Led1.getLedId())){
					response.setContent(ObixUtil.getLedDataRep(Light.Led1));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (appId.equals(Light.Led2.getLedId())){
					response.setContent(ObixUtil.getLedDataRep(Light.Led2));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (appId.equals(Luminosite.appId)){
					response.setContent(ObixUtil.getLumiDataRep(Luminosite.lumiValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				}else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "level1" :
				if(appId.equals(Ventilateur.appId)){
					Ventilateur.fanLevel = 1 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "level2" :
				if(appId.equals(Ventilateur.appId)){
					Ventilateur.fanLevel = 2 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "level3" :
				if(appId.equals(Ventilateur.appId)){
					Ventilateur.fanLevel = 3 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "thr_up" :
				if(appId.equals(Ventilateur.appId)) {
					Ventilateur.tempTh = Ventilateur.tempTh + 1 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if(Light.lights.containsKey(appId)) {
					Light.lumiTh = Light.lumiTh + 1 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "thr_down" :
				if(appId.equals(Ventilateur.appId)) {
					Ventilateur.tempTh = Ventilateur.tempTh - 1 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if(Light.lights.containsKey(appId)) {
					Light.lumiTh = Light.lumiTh - 1 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "false" : 
				if (appId.equals(Light.Led1.getLedId())){
					Light.Led1.setState(false);
					response.setResponseStatusCode(ResponseStatusCode.OK);
					
				} else if (appId.equals(Light.Led2.getLedId())){
					Light.Led2.setState(false);
					response.setResponseStatusCode(ResponseStatusCode.OK);
					
				} else if (appId.equals(Ventilateur.appId)){
					Ventilateur.fanLevel = 0 ;
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "true" :
				if (appId.equals(Light.Led1.getLedId())){
					Light.Led1.setState(true);
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (appId.equals(Light.Led2.getLedId())){
					Light.Led2.setState(true);
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "ALLfalse" : 
					if (Light.lights.containsKey(appId)){
						LED Led ;
						Set<Entry<String, LED>> setHm = Light.lights.entrySet();
					    Iterator<Entry<String, LED>> it = setHm.iterator();
					    while (it.hasNext()) {
					       Entry<String, LED> nextTag = it.next();
					       Led = nextTag.getValue();
					       Led.setState(false);
					    }
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "ALLtrue" : 
				if (Light.lights.containsKey(appId)){
					LED Led ;
					Set<Entry<String, LED>> setHm = Light.lights.entrySet();
				    Iterator<Entry<String, LED>> it = setHm.iterator();
				    while (it.hasNext()) {
				       Entry<String, LED> nextTag = it.next();
				       Led = nextTag.getValue();
				       Led.setState(true);
				    }
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "toggle" :
				old_toggle = !old_toggle;
				
				if (old_toggle == true) {
					if (Light.lights.containsKey(appId)){
						LED Led ;
						Set<Entry<String, LED>> setHm = Light.lights.entrySet();
					    Iterator<Entry<String, LED>> it = setHm.iterator();
					    while (it.hasNext()) {
					       Entry<String, LED> nextTag = it.next();
					       Led = nextTag.getValue();
					       Led.setState(false);
					    }
					    response.setResponseStatusCode(ResponseStatusCode.OK);
					}
				} else if (old_toggle == false) {
					if (Light.lights.containsKey(appId)){
						LED Led ;
						Set<Entry<String, LED>> setHm = Light.lights.entrySet();
					    Iterator<Entry<String, LED>> it = setHm.iterator();
					    while (it.hasNext()) {
					       Entry<String, LED> nextTag = it.next();
					       Led = nextTag.getValue();
					       Led.setState(true);
					    }
						response.setResponseStatusCode(ResponseStatusCode.OK);
					}
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				
			default:
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
		return response;
	}
 
}