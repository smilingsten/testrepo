package de.sten.dg.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SMSSendReport {
	
	
	HashMap<String, Integer> sentMsgCnt;
	HashMap<String, Integer> badMsgCnt;
	public  SMSSendReport() {
		sentMsgCnt = new HashMap<String, Integer>();
		badMsgCnt = new HashMap<String, Integer>();
	}
	
	
	public int getGoodRecipients(){
		return sentMsgCnt.size();
	}
	public int getBadRecipients(){
		return badMsgCnt.size();
	}
	
	public void addSentMsg(String number){
//		System.out.println("adding good report for number "+number);
		Integer c = new Integer(0);
		c = sentMsgCnt.get(number);
		if (c==null) c =0;
		c++;
		sentMsgCnt.put(number, c);
		
	}
	public void addBadMsg(String number){
	//	System.out.println("adding bad report for number "+number);

		Integer c = new Integer(0);
		 c = badMsgCnt.get(number);
		if (c==null) c =0;
		c++;
		badMsgCnt.put(number, c);
		
	}
	
	
	public String getOverallStatusCode(){
		String sc = "not set";
		if ( (sentMsgCnt.size()==0 ) && (badMsgCnt.size()==0 ) ) sc=  "ganz komisch... ?";
		if ( (sentMsgCnt.size()>0 ) && (badMsgCnt.size()==0 ) )  sc=  "Super... :-)";
		if ( (sentMsgCnt.size()==0 ) && (badMsgCnt.size()>0 ) )  sc=  "Schlecht... :-(";
		if ( (sentMsgCnt.size()>0 ) && (badMsgCnt.size()>0 ) )  sc=  "Mittelmäßig... :-|";
	//	System.out.println("returning overall status code "+sc);

		return sc;
		
	}
	
	
	public int getMsgSentPerNumber(String number){
		int sentmsgcnt = sentMsgCnt.get(number);
	//	System.out.println("returning goddmsgcnd per number for number "+number+". number of msg is "+sentmsgcnt);
		return sentmsgcnt;
	}
	public int getBadMsgPerNumber(String number){
		int badmsgcnt = badMsgCnt.get(number);
	//	System.out.println("returning badmsgcnt per number for number "+number+". number of msg is "+badmsgcnt);

		return badmsgcnt;
	}
	public int getTotalMsgSent(){
		int msgcount = 0;
		Iterator i = sentMsgCnt.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			int n = (Integer) entry.getValue();
			msgcount+=n;
		}
	//	System.out.println("returning totak msg sent "+msgcount);
		return msgcount;
	}
	public int getTotalMsgBad(){
		int msgcount = 0;
		Iterator i = badMsgCnt.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			int n = (Integer) entry.getValue();
			msgcount+=n;
		}
	//	System.out.println("returning totak msg not sent "+msgcount);

		return msgcount;
	}
	
	
	public HashMap<String, Integer> getSentMsgCnt() {
	//	System.out.println("returning sentMSG map");
		return sentMsgCnt;
	}
	public HashMap<String, Integer> getBadMsgCnt() {
	//	System.out.println("returning badsmg map");

		return badMsgCnt;
	}

	
	public int getTotalMsgCountforNumber(String number){
		int c = getMsgSentPerNumber(number) + getBadMsgPerNumber(number);
	//	System.out.println("returning total msg cnt for number "+number);
		return c;
		
	}
	
	

}
