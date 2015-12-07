package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Group extends BaseModel implements BaseInterface<Group> {

	StringProperty groupName = new SimpleStringProperty();
	IntegerProperty state = new SimpleIntegerProperty();
	
	public Group() {
		super();
	}
	
	public Group(Group _from) {
		super(_from);
		kopiujWartosci(_from, this);
	}
	
	@Override
	public void kopiujWartosci(Group _from, Group _to) {
		super.kopiujWartosci(_from, _to);
		if(_from==null) return;
		_to.groupName.set(_from.groupName.get());
		_to.state.set(_from.state.get());
	}

	public final StringProperty groupNameProperty() {
		return this.groupName;
	}
	

	public final java.lang.String getGroupName() {
		return this.groupNameProperty().get();
	}
	

	public final void setGroupName(final java.lang.String groupName) {
		this.groupNameProperty().set(groupName);
	}
	

	public final IntegerProperty stateProperty() {
		return this.state;
	}
	

	public final int getState() {
		return this.stateProperty().get();
	}
	

	public final void setState(final int state) {
		this.stateProperty().set(state);
	}
	
	

}
