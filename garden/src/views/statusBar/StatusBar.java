package views.statusBar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import resources.src.Icons;

public class StatusBar extends HBox {
	
	public static final StatusCircle LED_SOCKET = new StatusCircle(new Text("NET"));
	public static final StatusCircle LED_DB = new StatusCircle(new Text("DB"));
	public static final Button BTN_SERWIS = new Button("Serwis");
	
	public StatusBar() {
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		this.setAlignment(Pos.CENTER_LEFT);
		this.setSpacing(5);
		this.setPadding(new Insets(3,3,3,3));
		
		this.setStyle("-fx-border-color:  gray; -fx-border-width: 1px; -fx-border-style: solid;");
		
		BTN_SERWIS.setId("dark-blue-button");
		BTN_SERWIS.setGraphic(Icons.getIcon(Icons.SERWIS_2_16));
		this.getChildren().addAll(BTN_SERWIS,region,LED_SOCKET,LED_DB);
	}
	

}
