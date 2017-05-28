/******************************************************************************/
/*        @TITRE : Controller.java                                            */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 05 23, 2017                                                */
/* @MODIFICATION :                                                            */
/*      @AUTEURS : Leo Granier                                                */
/*    @COPYRIGHT : Copyright (c) 2017                                         */
/*                 Paul GUIRBAL                                               */
/*                 Joram FILLOL-CARLINI                                       */
/*                 Gianni D'AMICO                                             */
/*                 Matthieu BOUGEARD                                          */
/*                 Leo GRANIER                                                */
/*      @LICENSE : MIT License (MIT)                                          */
/******************************************************************************/

package org.eclipse.om2m.bedroom;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.interworking.service.InterworkingService;
 
public class Controller implements InterworkingService{
 
	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		String[] parts = request.getTo().split("/");
		String appId = parts[3];
		ResponsePrimitive response = new ResponsePrimitive(request);
 
		if(request.getQueryStrings().containsKey("op")){
			String valueOp = request.getQueryStrings().get("op").get(0);
 
			switch(valueOp){
			case "get":
				if(appId.equals(Night.appId)){
					response.setContent(ObixUtil.getNightDataRep(Night.buttonValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (Light.lights.containsKey(appId)){
					response.setContent(ObixUtil.getLedDataRep(Light.lights.get(appId).getState()));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if(appId.equals(ButtonLed.appId)){
					response.setContent(ObixUtil.getButtonDataRep(ButtonLed.buttonValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "true": case "false":
				if(appId.equals(Night.appId)){
					Night.buttonValue = Boolean.parseBoolean(valueOp);
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} if (Light.lights.containsKey(appId)){
					Light.lights.get(appId).setState(Boolean.parseBoolean(valueOp));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			default:
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
		} else {
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}
 
	@Override
	public String getAPOCPath() {
		return Monitor.ipeId;
	}
 
}