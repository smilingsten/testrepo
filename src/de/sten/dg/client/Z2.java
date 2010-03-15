package de.sten.dg.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.sten.dg.shared.SMSMessageObject;
import de.sten.dg.shared.SMSRequestObject;
import de.sten.dg.shared.SMSResponseObject;
import de.sten.dg.shared.SMSSendException;

public class Z2 implements EntryPoint {

	TextBox namebox, originbox;
	Numberbox numbox;
	Button numplusbtn, sendButton, validateButton;
	PasswordTextBox pwbox;
	TextArea msgbox;
	Label cntlbl;
	HTML dialoghtml, somehtml;
	DialogBox dbox;
	VerticalPanel dialogVPanel;
	final Button closeButton = new Button("Schließen");
	
	Timer t;
	CheckBox flashcheck, sandbox, staylogbox;
	HashMap<Date, Numberbox> numboxes;
	private final SMSServiceAsync smsService = GWT
	.create(SMSService.class);
	
	public void onModuleLoad() {
		numboxes = new HashMap<Date, Numberbox>();

		createUIObbjects();
		msgbox.setSize("25em", "10em");
		layOutUI();

	}

	// adds all UI elements to to RootPanel (HTML page)
	private void layOutUI() {
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
		RootPanel.get("sandbox").add(sandbox);
		RootPanel.get("stayloggedin").add(somehtml);
		RootPanel.get("stayloggedin").add(staylogbox);
	//	RootPanel.get("validate").add(validateButton);

		

		

	}
	public final class TestBtnHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
System.out.println("testbutton clicked");	
	SessionManager sessionManager = new SessionManager();
	sessionManager.setSessionCookie("Sten"	, "MyPass!_);.Word_");
	sessionManager.deleteSessionCookie();
	System.out.println("valid cokie exists: "+sessionManager.validCookieExists());
		}
		
	}

	// builds all UI elements and adds event handlers
	private void createUIObbjects() {
		validateButton = new Button("testbtn");
		validateButton.addClickHandler(new TestBtnHandler());
		dialoghtml = new HTML("Here is the dialog message");
		dbox = new DialogBox();
		dbox.setAnimationEnabled(true);
		closeButton.setWidth("100%");
		
		dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		dialogVPanel.add(dialoghtml);
		dbox.setWidget(dialogVPanel);
		dialogVPanel.addStyleName("diapnl");
		somehtml= new HTML("<p>eingelogged bleiben<br />(nicht empfohlen)</p>"); 
		dialogVPanel.add(closeButton);
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dbox.hide();
			}
		});
		
		dbox.hide();

		// create all objects
		namebox = new TextBox();
		pwbox = new PasswordTextBox();
		originbox = new TextBox();
		msgbox = new TextArea();
		cntlbl = new Label("counter...");
		Date d = new Date();
		numbox = new Numberbox(d);
		numplusbtn = new Button("+");
		flashcheck = new CheckBox();
		sandbox = new CheckBox();
		staylogbox = new CheckBox();
		sendButton = new Button("Send");

		// add handlers to buttons and hide first remove button
		numplusbtn.addClickHandler(new NumPlusBtnHandler());
		sendButton.addClickHandler(new SendButtonHandler());
		numbox.getRemoveMe().addClickHandler(new MyRemHandler(d));

		// add handler to count the message chars to message box
		msgbox.addKeyUpHandler(new KeyUpHandler() {

			public void onKeyUp(KeyUpEvent event) {
				countChars();
			}
		});
		numbox.getRemoveMe().setVisible(false);

		// put numberbox in map to be found later
		numboxes.put(numbox.getId(), numbox);

		// execute msgcoutn once to have the correct label set
		countChars();
		sandbox.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				countChars();
			}
		});

	}

	// click handler for the send button
	private final class SendButtonHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
		
			SMSFormObject formdata = readForm();
			try {
				new FormChecker().isFormDataOK(formdata);
			} catch (SMSFormDataException e) {

				dialoghtml
						.setHTML("<p><b>Ich kann so nicht arbeiten!!!</b></p><br /> "
								+ e.getErrorMsg() + "<br />");
				closeButton.setVisible(true);

				dbox.center();

				return;
			}
			
			dialoghtml
			.setHTML("<p><b>Sending message(s)...</b></p><br /><br /> "
					+"<p align=\"center\"><img src=\"odie.gif\" /></p>");	
			dialoghtml.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			closeButton.setVisible(false);
	dbox.center();


	
			ArrayList<SMSMessageObject> smsmo =  getMessages2send(formdata);
			SMSRequestObject smsro = new SMSRequestObject();
			
			 t = new Timer() {
			      public void run() {
			    	  dialoghtml
						.setHTML("<p><b>Zeitüberschreitung beim Senden</b></p><br /> "
								+"<p>Ich kann nicht sagen, ob die Nachricht gesendet wurde :-(<br /><br /></p>");
				closeButton.setVisible(true);

				dbox.center();
			      }
			    };
			    int xy = smsmo.size()*500;
			    t.schedule(16666+xy);
			
			smsro.setUsername(formdata.getUsername());
			smsro.setPassword(formdata.getPassword());
			smsro.setOrginator(formdata.getOrginator());
			smsro.setFlash(formdata.isFlash());
			smsro.setEnvironment(formdata.getEnvironment());
			smsro.setMessages(smsmo);
			
			send(smsro);
		}
	}
	
	
	//send the message(s)
	void send(SMSRequestObject smsrobj){
		smsService.sendSMS(smsrobj,
				new AsyncCallback<SMSResponseObject>() {
					public void onFailure(Throwable caught) {

						t.cancel();
						System.out.println("rpc failure");
						SMSSendException myex = (SMSSendException) caught;
						dialoghtml
						.setHTML("<p>"+myex.getErrorMsg()+"</p><br /> ");
						closeButton.setVisible(true);
				dbox.center();
				
				System.out.println("caught says "+myex.getErrorMsg());
					}

					public void onSuccess(SMSResponseObject result) {
						
						t.cancel();
						System.out.println("rpc success");
						System.out.println("answer was: "+result.getStatusmessage());
						dialoghtml
						.setHTML("<p>"+result.getStatusmessage()+"</p><br /> ");
						closeButton.setVisible(true);

				dbox.center();

					}
				});
		
		
	}
	
	
//ist noch wackelig...
	private ArrayList<SMSMessageObject> getMessages2send(SMSFormObject smsfo) {
		ArrayList<String> messagechunks = new ArrayList<String>();
		String message = smsfo.getMessage();
		char[] specialchars = { '|', '^', '€', '{', '}', '[', ']', '~', 0x0a };
		int divisor = 765;
		if (sandbox.getValue()) divisor = 160;
		if (!(sandbox.getValue())) divisor = 765;
		int charcnt =0;
		StringBuilder sb = new StringBuilder("");
		char[] msgchars = message.toCharArray();
		for (int i = 0; i < msgchars.length; i++) {
			 if (charcnt>divisor-1){
				 messagechunks.add(sb.toString());	
				 sb = new StringBuilder("");
				 charcnt=0;
			 }
			 
			sb.append(msgchars[i]);
			charcnt++;
			 for (int j = 0; j < specialchars.length; j++) {
				 if (msgchars[i] == specialchars[j]) {
					 charcnt++;
				 }
			 }
		
			
			 
		}
		if (sb.toString().length()>0)messagechunks.add(sb.toString());
		ArrayList<SMSMessageObject> smsmobjects = new ArrayList<SMSMessageObject>();
		for (int i =0; i<smsfo.getNumbers().size();i++){
			
			for (int j=0; j<messagechunks.size();j++){
				SMSMessageObject smso = new SMSMessageObject();
				smso.setNumber(smsfo.getNumbers().get(i));
				smso.setMessage(messagechunks.get(j));
				smsmobjects.add(smso);
	
			}
			
		}
		return smsmobjects;
	}

	// count the number if message chars and compute odg message chars (some
	// chars like {, }, ], €
	// count twice, set the counterlabel to this message
	private void countChars() {

		char[] msgchars = msgbox.getText().toCharArray();
		char[] specialchars = { '|', '^', '€', '{', '}', '[', ']', '~', 0x0a };

		int odgcharcnt = msgchars.length;

		for (int i = 0; i < msgchars.length; i++) {

			for (int j = 0; j < specialchars.length; j++) {
				if (msgchars[i] == specialchars[j]) {
					odgcharcnt++;
				}

			}

		}
		int divisor = 765;
		if (sandbox.getValue())
			divisor = 160;
		if (!(sandbox.getValue()))
			divisor = 765;
		String chopmsg = "";
		int charfrac = odgcharcnt / divisor;
		if (odgcharcnt > divisor) {
			int parts = (charfrac);
			if (charfrac < ((double) odgcharcnt) / divisor) {
				parts++;
			}

			chopmsg = "Die Nachticht ist zu lang und wird deshalb auf " + parts
					+ " SMS aufgeteilt.";
		}

		cntlbl.setText("Die Nachricht hat " + odgcharcnt + " Zeichen. "
				+ chopmsg);

	}

	// gets the data from the form and puts them into a form object
	private SMSFormObject readForm() {

		// put all numbers of recipients in ArrayList
		ArrayList<String> numbers = new ArrayList<String>();
		Iterator i = numboxes.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			Numberbox nb = (Numberbox) entry.getValue();
			String x = nb.getText();
			String numberstring = x.replaceAll("([^0-9])", "");
			if (x.contains("+"))
				numberstring = "+" + numberstring;
			if( (!(numberstring.equals(""))) && ( !(numberstring ==null))    )
			numbers.add(numberstring);

		}
		
		// read all input fields and create form object
		String environment = "sandbox";
		if (!(sandbox.getValue())) environment = "production";
		SMSFormObject formdata = new SMSFormObject(namebox.getText().trim()
				.replaceAll(" ", ""), pwbox.getText(),
				originbox.getText().trim().replace(" ", "").replaceAll("[-/()]", ""), msgbox.getText(), numbers,
				flashcheck.getValue(),environment);
		return formdata;
	}

	// the ClickHandler for the add recipient button
	private final class NumPlusBtnHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			// since with that click we have at least two recipient boxes, we
			// make all
			// remove buttons visible
			Iterator i = numboxes.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				Numberbox nb = (Numberbox) entry.getValue();
				Button btn = nb.getRemoveMe();
				btn.setVisible(true);
			}

			// create new numberbos with Date as ID and add removeClickHandler
			final Date da = new Date();
			Numberbox nb = new Numberbox(da);
			nb.getRemoveMe().addClickHandler(new MyRemHandler(da));

			// add box and butoon to Html Page
			RootPanel.get("numberf").add(nb);
			RootPanel.get("numberf").add(nb.getRemoveMe());

			// put numberbox in map to be found
			numboxes.put(nb.getId(), nb);
		}

	}

	// clickHandler for the remove number box buttons
	// removes the numberbox he belongs to and itself
	private final class MyRemHandler implements ClickHandler {
		Date da;

		public MyRemHandler(Date da) {
			this.da = da;
		}

		public void onClick(ClickEvent event) {
			// tell the numbox to remove itself from parent
			numboxes.remove(da);

			// if there is only one numbox left, make remove button invisible
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
