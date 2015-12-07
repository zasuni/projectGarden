package controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import library.walidator.TypCechy;
import library.walidator.Walidacja;
import model.EspDevice;
import model.EspModul;
import model.Group;
import model.NoName;
import modelDao.Dao;
import resources.src.Icons;
import views.PlaceHolder;

public class EspKonfigController {
	
	@FXML private StackPane pane;
	
	@FXML private TextField tfMac;
	@FXML private TextField tfGrupa;
	@FXML private TextField tfNazwa;
	
	@FXML private TableView<EspDevice> tvDevice;
	@FXML private TableColumn<EspDevice, Number> colPort;
	@FXML private TableColumn<EspDevice, String> colNazwa;
	@FXML private TableColumn<EspDevice, Number> colSygnal;
	
	@FXML private Button btnZapisz;
	@FXML private Button btnZmien;
	@FXML private Button btnZamknij;
	@FXML private Button btnGrupa;
	
	private Pane parent;
	private EspModul espModul;
	private NoName noName;
	private Group grupa;
	private Walidacja walidacja = new Walidacja();
	
	public EspKonfigController() {
		FXMLLoader fxmlLoader = null;		
		try {
			fxmlLoader = new FXMLLoader(getClass().getResource("/views/EspKonfigView.fxml"));
			fxmlLoader.setController(this);
            fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Jeśli opc=0 oznacza, że okno zostało otwarte na potrzeby przypisania ESP.
	 * Jeśli opc=1 oznaczam że okno zostało otwarte w celu modyfikacji już przypisanego ESP.
	 * Różnica między opc 0 i 1 jest taka, że dla opc=0 na wstępie nie jest widoczny TableView.
	 * @param parent
	 * @param espModul
	 * @param opc
	 */
	public void show(Pane parent, EspModul espModul, NoName noName, int opc) {
		this.parent=parent;
		this.espModul=espModul;
		this.noName=noName;
		parent.getChildren().add(this.pane);
		init();
		
		tfMac.setText(this.espModul.getMac());
		tfMac.setEditable(false);
		tfGrupa.setEditable(false);

		switch(opc) {
		case 0 : {
			tvDevice.setVisible(false);
			btnZmien.setVisible(false);
			break;
		}
		case 1 : {
			btnZapisz.setVisible(false);
			refresh();
			break;
		}
		}
		
		Platform.runLater(()-> tfNazwa.requestFocus());
	}
	
	private void close() {
		parent.getChildren().remove(this.pane);
	}
	
	
	private void init() {	
		btnGrupa.setGraphic(Icons.getIcon(Icons.SZUKAJ_16));
		btnZapisz.setGraphic(Icons.getIcon(Icons.ZAPISZ_16));
		btnZmien.setGraphic(Icons.getIcon(Icons.EDYTUJ_16));
		btnZamknij.setGraphic(Icons.getIcon(Icons.ZAMKNIJ_16));
		
		btnGrupa.setId("dark-blue-button");
		btnZapisz.setId("dark-blue-button");
		btnZmien.setId("dark-blue-button");
		btnZamknij.setId("dark-blue-button");
		
		colPort.setCellValueFactory(k->k.getValue().gpioNoProperty());
		colNazwa.setCellValueFactory(k->k.getValue().deviceNameProperty());
		colSygnal.setCellValueFactory(k->k.getValue().gpioTypeProperty());
		
		colSygnal.setCellFactory( p -> {			
			TableCell<EspDevice, Number> cell = new TableCell<EspDevice, Number>() {				
				@Override
				protected void updateItem(Number t, boolean bln) {
					super.updateItem(t, bln);
					setAlignment(Pos.CENTER);
					setStyle(null);
					if(t!=null) {
						switch(t.intValue()){
						case 0 : {
							setText("Zapis");
							break;
						}
						case 1 : {
							setText("Odczyt");
							break;
						}
						case 2 : {
							setText("Automat");
							break;
						}
						}
					} else setText("");
				}
				
			};
			return cell;
		});
		
		espModul.idGroupProperty().addListener((v,ov,nv)->showGrupa());
		btnGrupa.setOnAction(a-> {
			GroupController gp = new GroupController();
			gp.show(this.parent, 1, espModul);
		});
		btnZapisz.setOnAction(a-> przypisz());
		btnZamknij.setOnAction(a-> close());
		
		walidacja();
	}
	
	
	private void walidacja() {
		Platform.runLater(new Runnable() {
			public void run() {
				walidacja = new Walidacja();
				walidacja.dodaj(tfNazwa);
				walidacja.ustawCeche(tfNazwa, TypCechy.POLE_PUSTE, true);
				walidacja.ustawCeche(tfNazwa, TypCechy.DUZE_LITERY, true);

				walidacja.dodaj(tfGrupa);
				walidacja.ustawCeche(tfGrupa, TypCechy.POLE_PUSTE, true);
				walidacja.ustawCeche(tfGrupa, TypCechy.DUZE_LITERY, true);

				btnZapisz.disableProperty().bind(walidacja.prawidlowyProperty());				
			}
		});
	}
	
	
	private void przypisz() {
		espModul.setModulName(tfNazwa.getText());
		if(Dao.daoEspModul.przypiszModul(espModul, noName)) {
			btnZmien.setVisible(true);
			btnZapisz.setVisible(false);
			tvDevice.setVisible(true);
		}
	}
	
	public Pane getPane() {
		return this.pane;
	}
	
	private void showGrupa() {
		grupa = Dao.daoGroup.get(espModul.getIdGroup());
		if(grupa!=null) tfGrupa.setText(grupa.getGroupName());
	}
	
	private void refresh() {
		tvDevice.getSelectionModel().clearSelection();
		tvDevice.setItems(null);
		
		Task<Integer> task = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {			
				Dao.daoEspDevice.getAll();				
				return 0;
			}
		};
		
		task.stateProperty().addListener((ChangeListener<State>) (o, oldValue, newValue) -> {
			
			if(newValue==State.SCHEDULED) {
				tvDevice.setPlaceholder(PlaceHolder.getHolder(1));
			}
			
			if(newValue==State.SUCCEEDED || newValue==State.FAILED) {
				
				tvDevice.setPlaceholder(PlaceHolder.getHolder(0));
				tvDevice.setItems(Dao.daoEspDevice.getLista());
				tvDevice.getSelectionModel().selectFirst();
			}
			
		});

		new Thread(task).start();

	}
	
	
}
