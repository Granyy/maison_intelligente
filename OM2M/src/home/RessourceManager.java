/******************************************************************************/
/*        @TITRE : RessourceManager.java                                      */
/*      @VERSION : 0.1                                                        */
/*     @CREATION : 04 20, 2017                                                */
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

package org.eclipse.om2m.home;

import java.math.BigInteger;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class RessourceManager {
	
	static String CSE_ID = Constants.CSE_ID;
	static String CSE_NAME = Constants.CSE_NAME;
	static String REQUEST_ENTITY = Constants.ADMIN_REQUESTING_ENTITY;
	
	
	public static ResponsePrimitive createDescriptionContentInstance(String ipeId, String appId, String descriptor, String content) {
		String targetId;
		targetId = "/" + CSE_ID + "/" + CSE_NAME + "/" + appId + "/" + descriptor;
		ContentInstance cin = new ContentInstance();
		cin.setContent(content);
		cin.setContentInfo(MimeMediaType.OBIX);
		return RequestSender.createContentInstance(targetId, cin);
	}
	
	
	public static ResponsePrimitive createDataContentInstance(String appId, String data, String content) {
		String targetId;
		targetId = "/" + CSE_ID + "/" + CSE_NAME + "/" + appId + "/" + data;
		ContentInstance cin = new ContentInstance();
		cin.setContent(content);
		cin.setContentInfo(MimeMediaType.OBIX);
		return RequestSender.createContentInstance(targetId, cin);
	}
	
	
	public static ResponsePrimitive createAE(String ipeId, String appId) {
		AE ae = new AE();
		ae.setRequestReachability(true);
		ae.setAppID(ipeId);
		ae.getPointOfAccess().add(ipeId);
		ResponsePrimitive response = RequestSender.createAE(ae, appId);
		return response;
	}
	
	
	public static void createContainer(String appId, String descriptor, String data, int nrOfInstances) {
		String targetId = "/" + CSE_ID + "/" + CSE_NAME + "/" + appId;
		Container cnt = new Container();
		cnt.setMaxNrOfInstances(BigInteger.valueOf(nrOfInstances));
		RequestSender.createContainer(targetId, descriptor, cnt);
		RequestSender.createContainer(targetId, data, cnt);
	}
}
