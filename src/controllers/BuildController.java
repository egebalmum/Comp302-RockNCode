package controllers;

import domain.Building;
import domain.BuildingTracker;
import domain.BuildingType;
import factory.PanelType;
import factory.ViewType;
import main.EscapeFromKoc;

public class BuildController {
	
	private BuildingTracker buildingList;
	
	public BuildController() {
		buildingList = new BuildingTracker();
	}

	public void openHelpScreen() {
		EscapeFromKoc.getInstance().changePanel(EscapeFromKoc.getInstance().getCurPanel(), 
		EscapeFromKoc.getInstance().getView(ViewType.GameView).getPanel(PanelType.Help));
	}

	public Building nextBuilding() {

		if (buildingList.getBuildingList().get(buildingList.getCurrentIndex()).getType() == BuildingType.SNA) {
			return buildingList.getBuildingList().get(buildingList.getCurrentIndex());
		}

		buildingList.setCurrentIndex(buildingList.getCurrentIndex() + 1);
		return buildingList.getBuildingList().get(buildingList.getCurrentIndex());	
	}

	public void startRun() {		
		EscapeFromKoc.getInstance().changePanel(EscapeFromKoc.getInstance().getCurPanel(), 
		EscapeFromKoc.getInstance().getView(ViewType.GameView).getPanel(PanelType.Run));
		
		
	}

} 
