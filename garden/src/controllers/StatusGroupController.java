package controllers;

import javafx.scene.Node;
import views.StatusGroupView;

public class StatusGroupController {
	
	private StatusGroupView viewStatusGroup = new StatusGroupView();
	
	public StatusGroupController() {
	
	}
	
	public Node getView() {
		return viewStatusGroup;
	}

}
