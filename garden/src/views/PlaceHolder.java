package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import resources.src.Graphics;


/**
 * @author Tomasz Załęski
 *
 */
public class PlaceHolder {
	
	private static VBox box = new VBox();
	private static PlaceHolder instance;
	private ImageView imageView = Graphics.getGraphic(Graphics.INDICATOR_50x50);
	Label label = new Label();
	private Button btnClose;
	private Button btnReconnect;
	private HBox hBox = new HBox(10);
	
	
	/**
	 * Metoda zwraca VBox do użycia np. jako Holder w TableView
	 * 
	 * @param opc: 0 - dla tableView, które nie posiada rekordów (brak rekordów w bazie)
	 * 		       1 - dla tableView, które oczekuje na listę rekordów
	 * 
	 */
	public static Pane getHolder(int opc) {

		
		if(instance==null){
			instance = new PlaceHolder();
			
			instance.hBox.setPadding(new Insets(10,10,10,10));
			
			box.setDisable(false);
			box.setAlignment(Pos.CENTER);
			box.setStyle("-fx-background-color: #ffffff");
			
			box.getChildren().addAll(instance.label, instance.imageView, instance.hBox);
			
		}
		
	
		switch(opc) {
		case 0: {
			instance.imageView.setVisible(false);
			instance.label.setText("Brak danych do wyświetlenia...");
			break;
		}
		case 1: {
			instance.imageView.setVisible(true);
			instance.label.setText("Proszę czekać.... Trwa ładowanie danych.");
			break;
		}
		case 2: {
			
			StackPane stackPane = new StackPane(); 
			instance.imageView.setVisible(true);
			instance.label.setText("Połączenie z bazą danych, zostało utracone...");
			instance.btnClose = new Button("Zamknij PI");
			instance.btnReconnect = new Button("Połącz ponownie");
			
			instance.hBox.getChildren().add(instance.btnClose);
			instance.hBox.getChildren().add(instance.btnReconnect);
			box.getChildren().add(instance.hBox);
			stackPane.setStyle("-fx-background-color: transparent");
			stackPane.setMouseTransparent(false);
			
			stackPane.getChildren().add(box);

			break;
		}
		}
		
		return box;
	}

}
