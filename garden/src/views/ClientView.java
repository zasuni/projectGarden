package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import views.statusBar.StatusCircle;

public class ClientView extends HBox {
	
	public static final StatusCircle LED_CLIENT = new StatusCircle(new Text(""));
	private Label lblNazwa;
	
	public ClientView(String nazwa) {
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		
		this.setId("clientView");
		lblNazwa = new Label(nazwa);
		
		this.setPadding(new Insets(5,5,5,5));
		this.setAlignment(Pos.CENTER_LEFT);
		this.getChildren().addAll(lblNazwa,region,LED_CLIENT);
	}
	
	public void setClientName(String nazwa) {
		this.lblNazwa.setText(nazwa);
	}
	
}
