package views;

import application.Common;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import resources.src.Icons;
import views.titlePane.TitlePane;

public class MainView extends StackPane {
	
	public static final StackPane WORK_PANE = new StackPane();
	public static final VBox SYSTEM_PANE = new VBox();
	public static final VBox CLIENT_PANE = new VBox();
	public static Button BTN_SYSTEM = new Button();
	
	public MainView() {
		build();
	}
	
	private void build() {
		this.setStyle("-fx-background-color: #e5ffe5");
		SplitPane splitPaneWork = new SplitPane(WORK_PANE,Common.LOG.getView());
		splitPaneWork.setOrientation(Orientation.VERTICAL);
		splitPaneWork.setDividerPosition(0, 0.80);
		
		SplitPane splitPane = new SplitPane(splitPaneWork,SYSTEM_PANE);
		splitPane.setDividerPosition(0, 0.70);

		CLIENT_PANE.setSpacing(10);
		CLIENT_PANE.setAlignment(Pos.TOP_LEFT);
		CLIENT_PANE.setPadding(new Insets(5,5,5,5));
		
		
		WORK_PANE.setStyle(Common.KOLOR_TLA);

		SYSTEM_PANE.setSpacing(10);
		SYSTEM_PANE.setAlignment(Pos.TOP_LEFT);
		SYSTEM_PANE.setPadding(new Insets(5,5,5,5));
		SYSTEM_PANE.setStyle(Common.KOLOR_TLA);
		
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER_LEFT);
			
		TitlePane titlePane = new TitlePane();
		titlePane.showGraphic();
		titlePane.showMainTitle();
		
		Region reg1 = new Region();
		VBox.setVgrow(reg1, Priority.ALWAYS);
		SYSTEM_PANE.getChildren().addAll(titlePane, BTN_SYSTEM, reg1,Common.STATUS_BAR.getView());
		BTN_SYSTEM.setId("dark-blue-button");
		BTN_SYSTEM.setGraphic(Icons.getIcon(Icons.START_16));
		BTN_SYSTEM.setMaxWidth(Double.MAX_VALUE);
		this.getChildren().add(splitPane);
	}
}
