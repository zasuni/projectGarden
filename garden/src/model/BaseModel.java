package model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class BaseModel {

	private LongProperty id = new SimpleLongProperty();

	
	
	public BaseModel() {
		
	}

	public BaseModel(BaseModel _from) {
		kopiujWartosci(_from, this);
	}
	
	public void kopiujWartosci(BaseModel _from, BaseModel _to) {
		if(_from==null) return;
		_to.id.set(_from.id.get());	
	}
	
	public final LongProperty idProperty() {
		return this.id;
	}
	
	public final long getId() {
		return this.idProperty().get();
	}
	
	public final void setId(final long id) {
		this.idProperty().set(id);
	}

	
	
	
}
