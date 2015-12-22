package controllers;

import java.io.IOException;

import application.Common;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.EspModul;
import modelDao.EspModulDao;
import resources.src.Icons;
import views.MainView;
import views.titlePane.TitlePane;

public class SerwisController {

	@FXML private StackPane pane;
	@FXML private VBox vb1Serwis;
	@FXML private Button btnGroup;
	@FXML private Button btnNoName;
	@FXML private Button btnZamknij;
	@FXML private Button btnZakoncz;
	@FXML private Button btnKonfigEsp;
	
	private Pane parent;
	
	public SerwisController() {
		FXMLLoader fxmlLoader = null;		
		try {
			fxmlLoader = new FXMLLoader(getClass().getResource("/views/SerwisView.fxml"));
			fxmlLoader.setController(this);
            fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public void show(Pane parent) {
		this.parent=parent;
		parent.getChildren().add(this.pane);
		init();
	}
	
	private void close() {
		MainView.SYSTEM_PANE.setDisable(false);
		parent.getChildren().remove(this.pane);
	}
	
	public void init() {	
		btnGroup.setGraphic(Icons.getIcon(Icons.FOLDER_16));
		btnNoName.setGraphic(Icons.getIcon(Icons.PRZYPISZ_16));
		btnKonfigEsp.setGraphic(Icons.getIcon(Icons.PRZYPISZ_16));
		btnZamknij.setGraphic(Icons.getIcon(Icons.ZAMKNIJ_16));
		btnZakoncz.setGraphic(Icons.getIcon(Icons.WYLOGUJ_16));

		btnGroup.setId("dark-blue-button");		
		btnNoName.setId("dark-blue-button");		
		btnKonfigEsp.setId("dark-blue-button");		
		btnZamknij.setId("dark-blue-button");
		btnZakoncz.setId("dark-blue-button");
		
		btnGroup.setOnAction(a-> {
			GroupController gp = new GroupController();
			gp.show(this.parent, 0, null);
		});
		btnNoName.setOnAction(a-> {
			NoNameController nn = new NoNameController();
			nn.show(this.parent);
		});
		btnKonfigEsp.setOnAction(a-> konfigESP());
		btnZamknij.setOnAction(a->close());		
		btnZakoncz.setOnAction(a->System.exit(0));
		
		TitlePane tp = new TitlePane();
		tp.showCustomTitle("Menu serwisowe", "Wybierz jedną z opcji..");
		vb1Serwis.getChildren().add(0, tp);
	}
	
	public Pane getPane() {
		return this.pane;
	}
	
	
	private void konfigESP(){
		EspModulDao<EspModul> daoModul = new EspModulDao<EspModul>(Common.daoFactory, false);
		EspModul espModul = daoModul.get(1);
		EspKonfigController espKonfigFrm = new EspKonfigController();
		espKonfigFrm.show(this.parent, espModul, null, 1);
		espModul.setIdGroup(-1);
	}
	
}
