package controllers;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.EspModul;
import model.NoName;
import modelDao.Dao;
import resources.src.Icons;
import views.PlaceHolder;

public class NoNameController {
	
	@FXML private StackPane pane;
	@FXML private TableView<NoName> tvNoName;
	@FXML private TableColumn<NoName, String> colMac;
	@FXML private TableColumn<NoName, String> colPorty;
	@FXML private TableColumn<NoName, String> colKonfig;
	
	@FXML private Button btnPrzypisz;
	@FXML private Button btnZamknij;
	
	private Pane parent;
	
	public NoNameController() {
		FXMLLoader fxmlLoader = null;		
		try {
			fxmlLoader = new FXMLLoader(getClass().getResource("/views/NoNameView.fxml"));
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
		
		refresh();
	}
	
	private void close() {
		parent.getChildren().remove(this.pane);
	}
	
	
	private void init() {	
		btnPrzypisz.setGraphic(Icons.getIcon(Icons.PRZYPISZ_16));
		btnZamknij.setGraphic(Icons.getIcon(Icons.ZAMKNIJ_16));
		
		btnPrzypisz.setId("dark-blue-button");
		btnZamknij.setId("dark-blue-button");
		
		colMac.setId("cellCenterBold");
		colPorty.setId("cellCenter");
		colKonfig.setId("cellCenter");
		colMac.setCellValueFactory(k->k.getValue().macProperty());
		colPorty.setCellValueFactory(k->k.getValue().gpioUsedProperty());
		colKonfig.setCellValueFactory(k->k.getValue().gpioTypeProperty());
		
		btnPrzypisz.setOnAction(a->przypisz());
		btnZamknij.setOnAction(a-> close());
		
		
	}
	
	private void przypisz() {
		NoName noName = tvNoName.getSelectionModel().getSelectedItem();
		if(noName!=null) {
			EspModul espModul = new EspModul();
			espModul.setMac(noName.getMac());
			espModul.setGpioUsed(noName.getGpioUsed());
			espModul.setGpioType(noName.getGpioType());
			EspKonfigController espKonfigFrm = new EspKonfigController();
			espKonfigFrm.show(this.parent, espModul, noName, 0);
			espModul.setIdGroup(-1);
		}
	}
	
	public Pane getPane() {
		return this.pane;
	}
	
	private void refresh() {
		tvNoName.getSelectionModel().clearSelection();
		tvNoName.setItems(null);
		
		Task<Integer> task = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {			
				Dao.daoNoName.getAll();				
				return 0;
			}
		};
		
		task.stateProperty().addListener((ChangeListener<State>) (o, oldValue, newValue) -> {
			
			if(newValue==State.SCHEDULED) {
				tvNoName.setPlaceholder(PlaceHolder.getHolder(1));
			}
			
			if(newValue==State.SUCCEEDED || newValue==State.FAILED) {
				
				tvNoName.setPlaceholder(PlaceHolder.getHolder(0));
				tvNoName.setItems(Dao.daoNoName.getLista());
				tvNoName.getSelectionModel().selectFirst();
			}
			
		});
		
		
		new Thread(task).start();

	}
	
}
