package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import application.Common;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import views.MainView;

public class Server {
	
	private ServerSocket serverSocket;
	public IntegerProperty status = new SimpleIntegerProperty(0); 

	public Server() {
		status.addListener((v,ov,nv)->{
			Common.STATUS_BAR.socketServerStatus.set(nv.intValue());
			switch (nv.intValue()) {
			case 0: {
				Common.LOG.add("<Socket serwer> STOP...");				
				Platform.runLater(() -> MainView.BTN_SYSTEM.setText("START"));
				break;
			}
			case 1: {
				Common.LOG.add("<Socket serwer> START...");
				Platform.runLater(() -> MainView.BTN_SYSTEM.setText("STOP"));
				break;
			}
			}
		});
	}

	public void stop(){
		try {
			serverSocket.close();
			status.set(0);
		} catch (IOException e) {
			Common.LOG.add("Blad zatrzymania serwera !");
		}
	}
	
	public void start(){
		try {
			serverSocket = new ServerSocket(Common.PORT_NUMBER);
			status.set(1);
			
			while (true) {
				Socket socket = serverSocket.accept();
				if(status.get()==1) {
					ClientConnection clientConnection = new ClientConnection(socket);
					clientConnection.start();
				}
			}
		} catch (IOException e) {
			
		} 
	}
	
}
