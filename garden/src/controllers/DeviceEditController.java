package controllers;

import java.io.IOException;

import application.Common;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import library.walidator.TypCechy;
import library.walidator.Walidacja;
import model.EspDevice;
import modelDao.Dao;
import resources.src.Icons;

public class DeviceEditController {
	
	@FXML private StackPane pane;
	@FXML private Button btnZapisz;
	@FXML private Button btnAnuluj;
	@FXML private TextField tfNazwa;
	
	private Pane parent;
	private int opc;
	private EspDevice espDevice;
	
	private Walidacja walidacja;
	
	public DeviceEditController() {
		FXMLLoader fxmlLoader = null;		
		try {
			fxmlLoader = new FXMLLoader(getClass().getResource("/views/DeviceEditView.fxml"));
			fxmlLoader.setController(this);
            fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void show(Pane parent, EspDevice espDevice, int opc) {
		this.parent = parent;
		this.parent.getChildren().add(this.pane);
		this.opc = opc;
		this.espDevice = espDevice;
		
		init();
		
		if(opc==1) tfNazwa.setText(espDevice.getDeviceName());
		
		Platform.runLater(()->tfNazwa.requestFocus());
	}
	
	private void init() {
		btnZapisz.setGraphic(Icons.getIcon(Icons.ZAPISZ_16));
		btnAnuluj.setGraphic(Icons.getIcon(Icons.ANULUJ_16));
		
		btnZapisz.setId("dark-blue-button");
		btnAnuluj.setId("dark-blue-button");
		
		btnZapisz.setOnAction(a->zapisz());
		btnAnuluj.setOnAction(a->close());
		
		walidacja();

	}
	
	private void walidacja() {
		Platform.runLater(new Runnable() {
			public void run() {
				walidacja = new Walidacja();
				walidacja.dodaj(tfNazwa);
				walidacja.ustawCeche(tfNazwa, TypCechy.POLE_PUSTE, true);
				walidacja.ustawCeche(tfNazwa, TypCechy.DUZE_LITERY, true);
				btnZapisz.disableProperty().bind(walidacja.prawidlowyProperty());				
			}
		});
	}
	
	private void zapisz() {
		boolean wynik = false;
		if(tfNazwa.getText().length()==0) {
			Common.LOG.add("Pole nie może być puste !");
			tfNazwa.requestFocus();
			return;
		}
		
		espDevice.setDeviceName(tfNazwa.getText());

		switch(opc) {
		case 0: {
			wynik = Dao.daoEspDevice.zapisz(espDevice);
			break;
		}
		case 1: {
			wynik = Dao.daoEspDevice.zmien(espDevice);
			break;
		}
		}
		if(wynik) close();
	}
	
	private void close() {
		parent.getChildren().remove(this.pane);
	}
	
}
