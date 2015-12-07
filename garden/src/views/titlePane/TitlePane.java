package views.titlePane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import resources.src.Icons;

public class TitlePane extends HBox {
	
	public TitlePane() {
		setId("titlePane");
		setHeight(10);
		setSpacing(10);
		setAlignment(Pos.BOTTOM_LEFT);
		setMinHeight(55);
	}
	
	private Node graphics() {
		StackPane sp = new StackPane();
		sp.setPadding(new Insets(3,10,3,5));
		sp.setStyle("-fx-background-color: #004d00;");
		sp.getChildren().add(Icons.getIcon(Icons.ROSLINKA_2_32));
		return sp;
	}
	
	private Node mainTitle() {
		VBox vb = new VBox();
		vb.setPadding(new Insets(3,0,3,0));
		Label lblTitle1 = new Label("eGarden");
		Label lblTitle2 = new Label("automatyka ogrodowa");
		lblTitle1.setStyle("-fx-font-family: 'Segoe UI Semibold'; -fx-text-fill: white; -fx-font-weight: bold;"); 
		lblTitle2.setStyle("-fx-font-family: 'Segoe UI Semibold'; -fx-text-fill: white; -fx-font-size: 8pt;"); 
		vb.getChildren().addAll(lblTitle1,lblTitle2);
		return vb;
	}
	
	private Node customTitle(String lbl1, String lbl2) {
		VBox vb = new VBox();
		vb.setPadding(new Insets(3,0,3,5));
		vb.setAlignment(Pos.CENTER_LEFT);
		
		if(lbl1.length()>0) {
			Label lblTitle1 = new Label(lbl1);
			lblTitle1.setStyle("-fx-font-family: 'Segoe UI Semibold'; -fx-text-fill: white; -fx-font-weight: bold;"); 
			vb.getChildren().add(lblTitle1);
		}
		if(lbl2.length()>0) {
			Label lblTitle2 = new Label(lbl2);
			lblTitle2.setStyle("-fx-font-family: 'Segoe UI Semibold'; -fx-text-fill: white; -fx-font-size: 8pt;"); 
			vb.getChildren().add(lblTitle2);
		}		
		return vb;
	}
	
	
	public void showGraphic() {
		this.getChildren().add(graphics());
	}
	
	public void showMainTitle() {
		this.getChildren().add(mainTitle());
	}

	public void showCustomTitle(String lbl1, String lbl2) {
		this.getChildren().add(customTitle(lbl1, lbl2));
	}

}
