package de.sten.dg.server;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.sten.dg.client.SMSService;
import de.sten.dg.shared.SMSMessageObject;
import de.sten.dg.shared.SMSRequestObject;
import de.sten.dg.shared.SMSResponseObject;
import de.sten.dg.shared.SMSSendException;
import de.sten.dg.shared.STSException;


public class SMSServiceImpl extends RemoteServiceServlet implements
SMSService{
	URLFetchService urlFetchService = URLFetchServiceFactory
	.getURLFetchService();
	

	public SMSResponseObject sendSMS(SMSRequestObject smsro)
			throws SMSSendException {
			System.out.println("server says hello");
			String username = smsro.getUsername();
			String password = smsro.getPassword();
			String originator = smsro.getOrginator();
			ArrayList<SMSMessageObject> messages = smsro.getMessages();
			String environment = smsro.getEnvironment();
			boolean flash = smsro.isFlash();
			
//			System.out.println("uname is "+username+"\npw is "+password
//					+"\norigin is "+originator+"\nenvironment is "+environment+"\nand flash is "+flash);
//			System.out.println("going to send "+messages.size()+" messages");
			for (int i = 0; i < messages.size(); i++) {
			//	System.out.println("message"+i+" to number "+messages.get(i).getNumber()+" with text "+messages.get(i).getMessage());
			}
			
			String token = null;
			
			try {
				token = getSTSToken(username, password);
			} catch (IOException e) {
				SMSSendException ex = new SMSSendException();
				ex.setResponseCode(-1);
				ex.setErrorMsg("<p><b>Verbindungsproblem beim Abholen des Sicherheitstokens</b><p>");
				System.out.println(ex.getErrorMsg());
				throw ex;
			} catch (STSException e) {

				SMSSendException ex = new SMSSendException();
				ex.setResponseCode(-1);
				ex.setErrorMsg("<p><b>Fehler Abholen des Sicherheitstokensens :-(</b> <br /><br />Deine Zugangsdaten scheinen nicht korrekt zu sein<p>");
				System.out.println(ex.getErrorMsg());
				throw ex;
			}
			if (token ==null) {
				SMSSendException ex = new SMSSendException();
				ex.setResponseCode(-1);
				ex.setErrorMsg("<p><b>Fehler Abholen des Sicherheitstokensens :-(</b> <br /><br />Deine Zugangsdaten scheinen nicht korrekt zu sein<p>");
				System.out.println(ex.getErrorMsg());
				throw ex;
			}
			
			String authheader = "TAuth realm=\"https://odg.t-online.de\",tauth_token=\""
				+ token + "\"";
		ArrayList<String> errorMessages = new ArrayList<String>();	
		SMSSendReport report = new SMSSendReport();
			for (int i=0; i<messages.size();i++){
				try {
					SMSResponseObject resp = sendSingleMessage(authheader, messages.get(i).getNumber(), 
							messages.get(i).getMessage(), originator, environment, flash);
					report.addSentMsg(messages.get(i).getNumber());
					System.out.println("keine exception beim send try/catch");
					
						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					SMSResponseObject resp = new SMSResponseObject();
					resp.setStatuscode(-1);
					resp.setStatusmessage("IOException beim Senden an "+messages.get(i).getNumber());
					report.addBadMsg(messages.get(i).getNumber());
					System.out.println("IO exception beim send try/catch");
					errorMessages.add(e.getMessage());


					
				}
				catch (SMSSendException e) {
					SMSResponseObject resp = new SMSResponseObject();
					resp.setStatuscode(-1);
					resp.setStatusmessage("IOException beim Senden an "+messages.get(i).getNumber()+
							"\n Antwort des ODG Servers war: "+e.getMessage()+
							"\n HTTP Statuscode war: "+e.getResponseCode());
					report.addBadMsg(messages.get(i).getNumber());
					System.out.println("SendSMSException beim send try/catch");
					errorMessages.add(e.getErrorMsg());


				}
			}
		StringBuilder sb = new StringBuilder("");
		sb.append("<b>Das Ergebnis des Sendevorgangs war: "
				+report.getOverallStatusCode()
				+"</b>&nbsp;&nbsp;&nbsp;<br /><br />Es wurden "+report.getTotalMsgSent()+" Nachrichten an "+report.getGoodRecipients()
				+" Empfänger versandt");
		if (report.getBadRecipients()>0){
			sb.append("<br /><br />Leider wurden "+report.getTotalMsgBad()+" Nachrichten an "+report.getBadRecipients()
					+" Empfänger nicht versandt");
			
		}
		
		for (int i = 0; i < errorMessages.size(); i++) {
			sb.append("<br /><br/>Fehlermeldung "+i+":"+errorMessages.get(i));
			
		}
		SMSResponseObject smsresponse =new SMSResponseObject();
		smsresponse.setStatuscode(0);
		smsresponse.setStatusmessage(sb.toString());
		return smsresponse;
	}
	
	private SMSResponseObject sendSingleMessage(String authheader, String number, String message,String originator, 
			String environment, boolean flash) throws IOException, SMSSendException{
		
		URL url = new URL(
				"https://gateway.developer.telekom.com/p3gw-mod-odg-sms/rest/"+environment+"/sms?");
		HTTPRequest httpRequest = new HTTPRequest(url, HTTPMethod.POST);
		httpRequest.addHeader(new HTTPHeader("Authorization", authheader));

		String number2 = URLEncoder.encode(number, "UTF-8");
		String message2 = URLEncoder.encode(message, "UTF-8");
		String originator2 = URLEncoder.encode(originator, "UTF-8");
		String flashstr = "false";
		if (flash) flashstr = "true";
		String flashstr2 = URLEncoder.encode(flashstr, "UTF-8");
		String payload = "number=" + number2 + "&message=" + message2
				+ "&originator=" + originator2+"&flash="+flashstr2;
		httpRequest.setPayload(payload.getBytes());
		HTTPResponse response2 = urlFetchService.fetch(httpRequest);
		String responsestr2 = new String(response2.getContent());
		if (response2.getResponseCode() == 200) {
			System.out.println("message to "+number+" sent, got response 200from sms server");
			SMSResponseObject resp = new SMSResponseObject();
			resp.setStatuscode(1);
			resp.setStatusmessage("Nachricht an "+number+" versandt :-)");
			return resp;
		}
		else {			

			System.out.println("message to "+number+ " not sent! response code was "+response2.getResponseCode()+
					"serverantwort war "+responsestr2);
			SMSSendException e = new SMSSendException();
			e.setResponseCode(-1);
			e.setErrorMsg("Die Nachricht an "+number+" konnte nicht gesendet werden. Servernachricht war "+responsestr2 );
			throw e;
		}
	
	}
	
private String getSTSToken(String username, String password) throws IOException, STSException{
		
		
		
		URL url = new URL("https://sts.idm.telekom.com/rest-v1/tokens/odg");
		URLFetchService urlFetchService = URLFetchServiceFactory
				.getURLFetchService();
		HTTPRequest httpRequest = new HTTPRequest(url, HTTPMethod.GET);

	
		String creds = username + ":" + password;
		String headerstr = "Basic " + Base64.encode(creds.getBytes("UTF-8"));
		httpRequest.addHeader(new HTTPHeader("Authorization", headerstr));

		HTTPResponse response = urlFetchService.fetch(httpRequest);
		String responsestr = new String(response.getContent());

		if (response.getResponseCode() != 200) {
			String error = ("getststoken: Problem beim abholen des Security-Tokens.");
									
			System.out.println(error);
			STSException e = new STSException();
			e.setHttpCode(response.getResponseCode());
			e.setTServerMessage(responsestr);
			e.setMyMessage(error);
			throw e;
		}
	

		Pattern pattern = Pattern.compile("\"token\":\"(.*)\"");
		Matcher matcher = pattern.matcher(responsestr);

		if (!matcher.find()) {
			String error =("getststoken: Dieser Fehler sollte nicht vorkommen! Problem beim abholen des Security-Tokens.");
			System.out.println(error);
			STSException e = new STSException();
			e.setHttpCode(response.getResponseCode());
			e.setTServerMessage(responsestr);
			e.setMyMessage(error);
			throw e;
		}

		String SecToken = matcher.group(1);
		
		return SecToken;
	}
}


