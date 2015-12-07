package views.statusBar;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class StatusCircle extends StackPane {

	Circle circle = new Circle(10,Color.LIGHTGRAY);
	
	public StatusCircle(Text tekst) {
		circle.setStrokeWidth(1);
		circle.setStroke(Color.GRAY);
		tekst.setStyle("-fx-font-size: 6pt; -fx-font-weight: bold;");
		this.getChildren().addAll(circle, tekst);
	}
	
	public Circle getCircle() {
		return circle;
	}
}