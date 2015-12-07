package application;
	
import controllers.MainController;
import controllers.StatusBarController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import socket.Server;


public class Main extends Application {	
	
	@Override
	public void start(Stage stage) {
		try {
			Common.SOCKET_SERVER = new Server();
			Common.STATUS_BAR = new StatusBarController();
			Common.MAIN = new MainController();
			
			Scene scene = new Scene(Common.MAIN.getView(),800,480);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.setOnCloseRequest(e-> Common.MAIN.zakoncz(e));
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
