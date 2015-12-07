package views;

import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class LogView extends StackPane {

	public TextArea ta;
	
	public LogView() {
		build();
	}
	
	private void build() {
		ta = new TextArea();
		ta.setEditable(false);
		ta.setId("log");
		this.getChildren().add(ta);
	}
	
	
}
