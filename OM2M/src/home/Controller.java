

package org.eclipse.om2m.home;


import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.home.alarme.Alarm;
import org.eclipse.om2m.home.alarme.WindowSensor;
import org.eclipse.om2m.home.environment.EnvironmentController;
import org.eclipse.om2m.home.environment.Luminosite;
import org.eclipse.om2m.home.environment.Temperature;
import org.eclipse.om2m.home.environment.Ventilateur;
import org.eclipse.om2m.home.notifier.Notifier;
import org.eclipse.om2m.home.rfid.Doorlock;
import org.eclipse.om2m.home.rfid.UserProfile;
import org.eclipse.om2m.interworking.service.InterworkingService;
 
public class Controller implements InterworkingService{
 
	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		String[] parts = request.getTo().split("/");
		String appId = parts[3];
		ResponsePrimitive response = new ResponsePrimitive(request);
		
		if(request.getQueryStrings().containsKey("op")){
			String valueOp = request.getQueryStrings().get("op").get(0);
			
			if (UserProfile.profile.containsKey(appId)) {
				response = UserProfile.UserController(appId, valueOp, response);
				return response;
			}
			else if (appId.equals(Doorlock.appId)) {
				response = Doorlock.DoorController(valueOp, response);
				return response;
			}
			else if (appId.equals(Notifier.appId)) {
				response = Notifier.NotifierController(valueOp, response);
				return response;
			}
			else if (appId.equals(Temperature.appId) || appId.equals(Ventilateur.appId) || appId.equals("LED1") || appId.equals("LED2") || appId.equals(Luminosite.appId) || appId.equals("BOUTON")){
				response = EnvironmentController.Controller(appId, valueOp, response);
				return response;
				
			} else if (appId.equals(Alarm.appId)) {
				response = Alarm.BuzzerController(valueOp, response);
				return response;
			}
			else if (appId.equals(WindowSensor.appId)) {
				response = WindowSensor.WindowSensorController(appId, valueOp, response);
				return response;
			}
			else {
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
		}
		else {
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}
	
	@Override
	public String getAPOCPath() {
		return Monitor.ipeId;
	}
 
}
