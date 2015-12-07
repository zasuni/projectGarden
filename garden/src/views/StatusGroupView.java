package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import views.titlePane.TitlePane;

public class StatusGroupView extends VBox {
	
	public static final VBox GROUP_STATUS_PANE = new VBox();

	public StatusGroupView() {
		build();
	}
	
	
	private void build() {
		this.setSpacing(10);
		this.setAlignment(Pos.TOP_LEFT);
		this.setPadding(new Insets(5,5,5,5));
		
		TitlePane titlePane = new TitlePane();
		titlePane.showCustomTitle("", "Status według grup...");
		this.getChildren().addAll(titlePane);
		
		ScrollPane sp = new ScrollPane();
		sp.setContent(GROUP_STATUS_PANE);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		
		
	}
	

}
