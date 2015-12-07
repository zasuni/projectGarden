package controllers;

import application.Common;
import javafx.application.Platform;
import javafx.scene.Node;
import views.LogView;

public class LogController {

	private LogView viewLog = new LogView();
	
	public LogController() {
	
	}
	
	public void clearLog() {
		viewLog.ta.clear();
	}
	
	public void add(String tekst) {
		final String message=Common.now()+": "+tekst;
		System.out.println(message);
		Platform.runLater(()->putIntoTextArea(message));				
	}
	
	synchronized private void putIntoTextArea(String tekst) {
		viewLog.ta.insertText(0, tekst+"\n");
	}
	
	public Node getView() {
		return viewLog;
	}
	
}
