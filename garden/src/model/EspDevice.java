package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EspDevice extends BaseModel implements BaseInterface<EspDevice> {
	
	IntegerProperty idEspModul = new SimpleIntegerProperty();
	IntegerProperty gpioNo = new SimpleIntegerProperty();
	IntegerProperty gpioType = new SimpleIntegerProperty();
	IntegerProperty gpioState = new SimpleIntegerProperty();
	StringProperty deviceName = new SimpleStringProperty();
	
	public EspDevice() {
		super();
	}
	
	public EspDevice(EspDevice _from) {
		super(_from);
		kopiujWartosci(_from, this);
	}

	@Override
	public void kopiujWartosci(EspDevice _from, EspDevice _to) {
		super.kopiujWartosci(_from, _to);
		_to.idEspModul.set(_from.idEspModul.get());
		_to.gpioNo.set(_from.gpioNo.get());
		_to.gpioType.set(_from.gpioType.get());
		_to.gpioState.set(_from.gpioState.get());
		_to.deviceName.set(_from.deviceName.get());
	}

	public final IntegerProperty idEspModulProperty() {
		return this.idEspModul;
	}
	

	public final int getIdEspModul() {
		return this.idEspModulProperty().get();
	}
	

	public final void setIdEspModul(final int idEspModul) {
		this.idEspModulProperty().set(idEspModul);
	}
	

	public final IntegerProperty gpioNoProperty() {
		return this.gpioNo;
	}
	

	public final int getGpioNo() {
		return this.gpioNoProperty().get();
	}
	

	public final void setGpioNo(final int gpioNo) {
		this.gpioNoProperty().set(gpioNo);
	}
	

	public final StringProperty deviceNameProperty() {
		return this.deviceName;
	}
	

	public final java.lang.String getDeviceName() {
		return this.deviceNameProperty().get();
	}
	

	public final void setDeviceName(final java.lang.String deviceName) {
		this.deviceNameProperty().set(deviceName);
	}

	public final IntegerProperty gpioStateProperty() {
		return this.gpioState;
	}
	

	public final int getGpioState() {
		return this.gpioStateProperty().get();
	}
	

	public final void setGpioState(final int gpioState) {
		this.gpioStateProperty().set(gpioState);
	}

	public final IntegerProperty gpioTypeProperty() {
		return this.gpioType;
	}
	

	public final int getGpioType() {
		return this.gpioTypeProperty().get();
	}
	

	public final void setGpioType(final int gpioType) {
		this.gpioTypeProperty().set(gpioType);
	}
	
	
	
	

}
