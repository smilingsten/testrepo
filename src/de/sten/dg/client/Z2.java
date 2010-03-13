package de.sten.dg.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Z2 implements EntryPoint {

	TextBox namebox, originbox;
	Numberbox numbox;
	Button numplusbtn, sendButton;
	PasswordTextBox pwbox;
	TextArea msgbox;
	Label cntlbl;
	DialogBox dbox;
	VerticalPanel dialogVPanel;
	CheckBox flashcheck;
	HashMap<Date, Numberbox> numboxes;
	

	public void onModuleLoad() {
		numboxes = new HashMap<Date, Numberbox>();
		
		createUIObbjects();
		msgbox.setSize("25em", "10em");
		layOutUI();


	}
	
	
	//adds all UI elements to to RootPanel (HTML page)
	private void layOutUI(){
		RootPanel.get("namef").add(namebox);
		RootPanel.get("pwf").add(pwbox);
		RootPanel.get("originf").add(originbox);
		RootPanel.get("msgf").add(msgbox);
		RootPanel.get("countlabel").add(cntlbl);
		RootPanel.get("numberf").add(numbox);
		RootPanel.get("numberf").add(numbox.getRemoveMe());
		RootPanel.get("numplus").add(numplusbtn);
		RootPanel.get("flash").add(flashcheck);
		RootPanel.get("sendb").add(sendButton);
		
	}
	
	//builds all UI elements and adds event handlers
	private void createUIObbjects(){
		dbox = new DialogBox();
		dbox.setAnimationEnabled(true);
		dbox.setTitle("My Dialog");
		dbox.setText("My Dialog message");
		dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dbox.setWidget(dialogVPanel);
		dbox.hide();
		
		//create all objects
		namebox = new TextBox();
		pwbox = new PasswordTextBox();
		originbox = new TextBox();
		msgbox = new TextArea();
		cntlbl = new Label("counter...");
		Date d = new Date();
		numbox = new Numberbox(d);
		numplusbtn = new Button("+");
		flashcheck = new CheckBox();
		sendButton = new Button("Send");
		
	
		
		//add handlers to buttons and hide first remove button
		numplusbtn.addClickHandler(new NumPlusBtnHandler());
		sendButton.addClickHandler(new SendButtonHandler());
		numbox.getRemoveMe().addClickHandler(new MyRemHandler(d));
		
		//add handler to count the message chars to message box
		msgbox.addKeyUpHandler(new KeyUpHandler() {
			
			public void onKeyUp(KeyUpEvent event) {
				countChars();				
			}
		});
		numbox.getRemoveMe().setVisible(false);
		
		//put numberbox in map to be found later
		numboxes.put(numbox.getId(), numbox);
		
		//execute msgcoutn once to have the correct label set
		countChars();
		
		

	}
	
	
	
	//click handler for the send button
	private final class SendButtonHandler implements ClickHandler{

		public void onClick(ClickEvent event) {	
			SMSFormObject formdata = readForm();
			boolean formIsOK = false;
			try {
				formIsOK = new FormChecker().isFormDataOK(formdata);
			} catch (SMSFormDataException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Form is not ok, msg was:\n"+e.getErrorMsg());
				dialogVPanel.add(new HTML("<b>Form is not ok, msg was:</b>"+e.getErrorMsg()));
				dbox.setText("Ups...");
				dbox.center();
				return;
			}
			
			
		}		
	}

	//count the number if message chars and compute odg message chars (some chars like {, }, ], € 
	//count twice, set the counterlabel to this message
	void countChars(){
		
		char[] msgchars = msgbox.getText().toCharArray();
		char[] specialchars= {'|','^', '€', '{' ,'}', '[', ']', '~',0x0a};
		
		int odgcharcnt = msgchars.length;
		
		for (int i = 0; i < msgchars.length; i++) {
			
				for (int j = 0; j < specialchars.length; j++) {
					if (msgchars[i] == specialchars[j]){
						odgcharcnt++;
					}
				
			}
			
		}
		String chopmsg ="";
		int charfrac=odgcharcnt/765;
		if (odgcharcnt>765){
			int parts = ( charfrac);
			if (charfrac<((double)odgcharcnt)/765) {
				parts++;
			}
			
			chopmsg="Die Nachticht ist zu lang und wird deshalb auf "+parts+" SMS aufgeteilt.";
		}
		
		cntlbl.setText("Die Nachricht hat "+odgcharcnt+" Zeichen. "+chopmsg);
		
		
	}
	
	//gets the data from the form and puts them into a form object
	private SMSFormObject readForm(){
		
		//put all numbers of recipients in ArrayList
		ArrayList<String> numbers = new ArrayList<String>();
		Iterator i = numboxes.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			Numberbox nb = (Numberbox) entry.getValue();
			String x = nb.getText();
			String numberstring = x.replaceAll("([^0-9])", "");
			if (x.contains("+"))numberstring="+"+numberstring;
			numbers.add(numberstring);			
			
		}
		
		//read all input fields and create form object
		SMSFormObject formdata = new SMSFormObject( 
				namebox.getText().trim().replaceAll("([^0-9a-zA-Z@.öäü])", ""),
				pwbox.getText(),
				originbox.getText().trim(),   msgbox.getText().trim(), 
				numbers,   flashcheck.getValue());		
		return formdata;
	}
	
	//the ClickHandler for the add recipient button
	private final class NumPlusBtnHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			//since with that click we have at least two recipient boxes, we make all
			//remove buttons visible
			Iterator i = numboxes.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				Numberbox nb = (Numberbox) entry.getValue();
				Button btn = nb.getRemoveMe();
				btn.setVisible(true);
			}	
			
			//create new numberbos with Date as ID and add removeClickHandler
			final Date da = new Date();
			Numberbox nb = new Numberbox(da);
			nb.getRemoveMe().addClickHandler(new MyRemHandler(da));
			
			//add box and butoon to Html Page
			RootPanel.get("numberf").add(nb);
			RootPanel.get("numberf").add(nb.getRemoveMe());
			
			//put numberbox in map to be found 
			numboxes.put(nb.getId(), nb);
		}
		
	}
	
	//clickHandler for the remove number box buttons
	//removes the numberbox he belongs to and itself
	private final class MyRemHandler implements ClickHandler {
		Date da;
		public MyRemHandler(Date da) {
			this.da = da;
		}

		public void onClick(ClickEvent event) {
			//tell the numbox to remove itself from parent
			numboxes.remove(da);
			
			//if there is only one numbox left, make remove button invisible
			if (numboxes.size() <= 1) {
				Iterator i = numboxes.entrySet().iterator();
				while (i.hasNext()) {
					Map.Entry entry = (Map.Entry) i.next();
					Numberbox nb = (Numberbox) entry.getValue();
					Button btn = nb.getRemoveMe();
					btn.setVisible(false);
				}

			}

		}
	}



	
	
}


