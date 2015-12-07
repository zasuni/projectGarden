package controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import views.statusBar.StatusBar;
import views.statusBar.StatusCircle;


public class StatusBarController {

	private StatusBar viewStatusBar = new StatusBar();
	public IntegerProperty socketServerStatus = new SimpleIntegerProperty(0);
	public IntegerProperty dbServerStatus = new SimpleIntegerProperty(0);
	
	public StatusBarController() {
		socketServerStatus.addListener((v,ov,nv)-> changeStatusCircle(StatusBar.LED_SOCKET, nv.intValue()));
		dbServerStatus.addListener((v,ov,nv)-> changeStatusCircle(StatusBar.LED_DB, nv.intValue()));
	}
	
	private void changeStatusCircle(StatusCircle circle, int status) {
		Color color = null;
		if(status==1) color=Color.LIME; else color=Color.LIGHTGRAY;
		
		circle.getCircle().setFill(color);
		circle.getCircle().setEffect(new InnerShadow(5, color.darker().darker().darker()));
	}
	
	public Node getView() {
		return viewStatusBar;
	}
}
