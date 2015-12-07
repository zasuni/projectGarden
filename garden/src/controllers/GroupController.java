package controllers;

import java.io.IOException;

import application.Common;
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
import model.Group;
import modelDao.Dao;
import resources.src.Icons;
import views.PlaceHolder;

public class GroupController {
	
	@FXML private StackPane pane;
	@FXML private TableView<Group> tvGroup;
	@FXML private TableColumn<Group, String> colName;
	
	@FXML private Button btnDodaj;
	@FXML private Button btnZmien;
	@FXML private Button btnUsun;
	@FXML private Button btnWybierz;
	@FXML private Button btnZamknij;
	
	private Pane parent;
	private EspModul espModul;

	public GroupController() {
		FXMLLoader fxmlLoader = null;		
		try {
			fxmlLoader = new FXMLLoader(getClass().getResource("/views/GroupsView.fxml"));
			fxmlLoader.setController(this);
            fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Jeśli opc=0 pokazuje okno w trybie normalnym a jeśli opc=1 to w trybie "wyboru" pozycji z listy. 
	 * Nie ma wówczas przycisków Dodaj/Zmień/Usuń a jest przycisk Wybierz.
	 * 
	 * @param parent
	 * @param opc
	 */
	public void show(Pane parent, int opc, EspModul espModul ) {
		this.parent=parent;
		this.espModul = espModul;
		parent.getChildren().add(this.pane);
		init();
		
		switch(opc){
		case 0 : {
				btnWybierz.setVisible(false);
			break;
		}
		case 1 : {
				btnWybierz.setVisible(true);
				btnDodaj.setVisible(false);
				btnZmien.setVisible(false);
				btnUsun.setVisible(false);
			break;
		}
		}
		refresh();
	}
	
	private void close() {
		parent.getChildren().remove(this.pane);
	}
	
	
	private void init() {	
		btnDodaj.setGraphic(Icons.getIcon(Icons.DODAJ_16));
		btnZmien.setGraphic(Icons.getIcon(Icons.EDYTUJ_16));
		btnUsun.setGraphic(Icons.getIcon(Icons.USUN_16));
		btnWybierz.setGraphic(Icons.getIcon(Icons.WYBIERZ_WER_3_16));
		btnZamknij.setGraphic(Icons.getIcon(Icons.ZAMKNIJ_16));
		
		btnDodaj.setId("dark-blue-button");
		btnZmien.setId("dark-blue-button");
		btnUsun.setId("dark-blue-button");
		btnWybierz.setId("dark-blue-button");
		btnZamknij.setId("dark-blue-button");
		
		colName.setId("cellBold");
		colName.setCellValueFactory(k->k.getValue().groupNameProperty());
		
		btnDodaj.setOnAction(a->dodaj());
		btnUsun.setOnAction(a->usun());
		btnZmien.setOnAction(a->zmien());
		btnWybierz.setOnAction(a->wybierz());
		btnZamknij.setOnAction(a-> close());
		
		
	}
	
	private void dodaj() {
		Group item = new Group();
		GroupEditController editFrm = new GroupEditController();
		editFrm.show(parent, item, 0);
	}

	private void zmien() {
		Group item = tvGroup.getSelectionModel().getSelectedItem();
		if(item!=null) {
			GroupEditController editFrm = new GroupEditController();
			editFrm.show(parent, item, 1);
		}
	}

	private void usun() {
		Group item = tvGroup.getSelectionModel().getSelectedItem();
		if(item!=null) {
			if(Common.daoFactory.deleteRekord(item.getId(), "groups")) {
				Dao.daoGroup.getLista().remove(item);
			}
		}
	}
	
	private void wybierz() {
		Group item = tvGroup.getSelectionModel().getSelectedItem();
		if(item!=null) {
			espModul.setIdGroup(item.getId());
			close();
		}
	}
	
	
	
	public Pane getPane() {
		return this.pane;
	}
	
	private void refresh() {
		tvGroup.getSelectionModel().clearSelection();
		tvGroup.setItems(null);
		
		Task<Integer> task = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {			
				Dao.daoGroup.getAll();				
				return 0;
			}
		};
		
		task.stateProperty().addListener((ChangeListener<State>) (o, oldValue, newValue) -> {
			
			if(newValue==State.SCHEDULED) {
				tvGroup.setPlaceholder(PlaceHolder.getHolder(1));
			}
			
			if(newValue==State.SUCCEEDED || newValue==State.FAILED) {
				
				tvGroup.setPlaceholder(PlaceHolder.getHolder(0));
				tvGroup.setItems(Dao.daoGroup.getLista());
				tvGroup.getSelectionModel().selectFirst();
			}
			
		});
		
		
		new Thread(task).start();

	}
	
}
