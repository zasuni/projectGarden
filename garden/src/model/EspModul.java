package model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EspModul extends BaseModel implements BaseInterface<EspModul> {
	
	LongProperty idGroup = new SimpleLongProperty();
	StringProperty mac = new SimpleStringProperty();
	StringProperty gpioUsed = new SimpleStringProperty();
	StringProperty gpioType = new SimpleStringProperty();
	StringProperty gpioState = new SimpleStringProperty();
	StringProperty modulName = new SimpleStringProperty();
	
	
	public EspModul() {
		super();
	}

	public EspModul(EspModul _from) {
		super(_from);
		kopiujWartosci(_from, this);
	}
	
	@Override
	public void kopiujWartosci(EspModul _from, EspModul _to) {
		super.kopiujWartosci(_from, _to);
		if(_from==null) return;
		_to.idGroup.set(_from.idGroup.get());
		_to.mac.set(_from.mac.get());
		_to.gpioUsed.set(_from.gpioUsed.get());
		_to.gpioType.set(_from.gpioType.get());
		_to.modulName.set(_from.modulName.get());
	}

	public final StringProperty macProperty() {
		return this.mac;
	}
	

	public final java.lang.String getMac() {
		return this.macProperty().get();
	}
	

	public final void setMac(final java.lang.String mac) {
		this.macProperty().set(mac);
	}
	

	public final StringProperty gpioUsedProperty() {
		return this.gpioUsed;
	}
	

	public final java.lang.String getGpioUsed() {
		return this.gpioUsedProperty().get();
	}
	

	public final void setGpioUsed(final java.lang.String gpioUsed) {
		this.gpioUsedProperty().set(gpioUsed);
	}
	

	public final StringProperty gpioTypeProperty() {
		return this.gpioType;
	}
	

	public final java.lang.String getGpioType() {
		return this.gpioTypeProperty().get();
	}
	

	public final void setGpioType(final java.lang.String gpioType) {
		this.gpioTypeProperty().set(gpioType);
	}
	

	public final StringProperty modulNameProperty() {
		return this.modulName;
	}
	

	public final java.lang.String getModulName() {
		return this.modulNameProperty().get();
	}
	

	public final void setModulName(final java.lang.String modulName) {
		this.modulNameProperty().set(modulName);
	}

	public final LongProperty idGroupProperty() {
		return this.idGroup;
	}
	

	public final long getIdGroup() {
		return this.idGroupProperty().get();
	}
	

	public final void setIdGroup(final long idGroup) {
		this.idGroupProperty().set(idGroup);
	}
	

	public final StringProperty gpioStateProperty() {
		return this.gpioState;
	}
	

	public final java.lang.String getGpioState() {
		return this.gpioStateProperty().get();
	}
	

	public final void setGpioState(final java.lang.String gpioState) {
		this.gpioStateProperty().set(gpioState);
	}
	
	
}
