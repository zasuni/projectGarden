package controllers;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import views.ClientView;
import views.MainView;
import views.statusBar.StatusCircle;

public class ClientController {
	
	ClientView viewClient;
	public static final IntegerProperty STATUS_CLIENT = new SimpleIntegerProperty(0);
	
	public ClientController(String nazwa) {
		viewClient = new ClientView(nazwa);
		STATUS_CLIENT.addListener((v,ov,nv)-> Platform.runLater(()->changeStatusCircle(ClientView.LED_CLIENT, nv.intValue())));
	}
	
	private void changeStatusCircle(StatusCircle circle, int status) {
		Color color = null;
		if(status==1) color=Color.RED; else color=Color.LIGHTGRAY;		
		circle.getCircle().setFill(color);
		circle.getCircle().setEffect(new InnerShadow(5, color.darker().darker().darker()));
	}
	
	public Node getView() {
		return viewClient;
	}
	
	public void setClientName(String nazwa) {
		viewClient.setClientName(nazwa);
	}

	synchronized public void setVisible(boolean visible) {
		if(visible) MainView.CLIENT_PANE.getChildren().add(viewClient); else MainView.CLIENT_PANE.getChildren().remove(viewClient);
	}
}
