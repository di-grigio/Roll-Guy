package game.script.ui.editor;

import tools.Const;
import game.cycle.scene.ui.list.UIGame;
import game.script.Script;
import game.state.database.Database;
import game.state.location.manager.LocationManager;

public class ui_LocationDel implements Script {

	private UIGame ui;
	
	public ui_LocationDel(UIGame ui) {
		this.ui = ui;
	}

	@Override
	public void execute() {
		int id = ui.getSelectedListLocation();
		
		if(id != Const.INVALID_ID && id != 0){
			if(LocationManager.deleteLocation(id)){
				Database.deleteLocation(id);
				Database.loadLocations();
				ui.location.loadLocationList();
			}
		}
	}
}