package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NoName extends BaseModel implements BaseInterface<NoName> {
	
	StringProperty mac = new SimpleStringProperty();
	StringProperty gpioUsed = new SimpleStringProperty();
	StringProperty gpioType = new SimpleStringProperty();
	
	
	public NoName() {
		super();
	}

	public NoName(NoName _from) {
		super(_from);
		kopiujWartosci(_from, this);
	}
	
	@Override
	public void kopiujWartosci(NoName _from, NoName _to) {
		super.kopiujWartosci(_from, _to);
		if(_from==null) return;
		_to.mac.set(_from.mac.get());
		_to.gpioUsed.set(_from.gpioUsed.get());
		_to.gpioType.set(_from.gpioType.get());
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

	
}
