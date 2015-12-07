package controllers;

import application.Common;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.scene.layout.Pane;
import resources.src.Icons;
import views.MainView;
import views.statusBar.StatusBar;

public class MainController {

	private MainView viewMain = new MainView();
	private StatusGroupController controllerStatusGrup = new StatusGroupController();
	
	public static final IntegerProperty SYSTEM_STATUS = new SimpleIntegerProperty(0);
	
	public MainController() {
		initialize();
	}
	
	public void zakoncz(Event e) {
		if(Common.SOCKET_SERVER.status.get()==1) {
			Common.LOG.add("Nalezy zakonczyc prace serwera !");
			if(e!=null) e.consume();
		}
	}
	
	private void initialize() {
		MainView.BTN_SYSTEM.setText("START");
		MainView.BTN_SYSTEM.setOnAction(a-> {
			systemStartStop();		
		});
		MainView.WORK_PANE.getChildren().add(controllerStatusGrup.getView());
		
		
		
		StatusBar.BTN_SERWIS.setOnAction(a->{
			MainView.SYSTEM_PANE.setDisable(true);
			SerwisController serwis = new SerwisController();
			serwis.show(MainView.WORK_PANE);
		});
	}
	
	private void systemStartStop() {
		if(SYSTEM_STATUS.get()==0) {
			new Thread(() -> Common.SOCKET_SERVER.start()).start();
			new Thread(() -> Common.daoFactory.makeConnection()).start();
			Platform.runLater(() -> MainView.BTN_SYSTEM.setGraphic(Icons.getIcon(Icons.ZAMKNIJ_16)));
			SYSTEM_STATUS.set(1);
		} else {
			new Thread(()-> Common.SOCKET_SERVER.stop()).start();
			new Thread(()-> Common.daoFactory.closeConnection()).start();	
			Platform.runLater(() -> MainView.BTN_SYSTEM.setGraphic(Icons.getIcon(Icons.START_16)));
			SYSTEM_STATUS.set(0);
		}
	}
	
	
	public Pane getView() {
		return viewMain;
	}
}
