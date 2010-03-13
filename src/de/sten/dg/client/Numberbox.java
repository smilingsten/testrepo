package de.sten.dg.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Numberbox extends TextBox {
	
	Button removeMe;
	TextBox me;
	HTML myHtml;
	Date id;
	

	
	public Numberbox(Date id) {
		this.id = id;
		myHtml=new HTML("<br />");
		removeMe = new Button("-");
		me = this;
		removeMe.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				me.setVisible(false);
				me.setText("");
				me.removeFromParent();
				myHtml.removeFromParent();
				removeMe.removeFromParent();
				
				
				
			}
		});

	
	}
	
	public Button getRemoveMe() {
		return removeMe;
	}


	public HTML getMyHtml() {
		return myHtml;
	}

	public void setMyHtml(HTML myHtml) {
		this.myHtml = myHtml;
	}

	public Date getId() {
		return id;
	}



}
