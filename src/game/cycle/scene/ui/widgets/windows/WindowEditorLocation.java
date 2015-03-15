package game.cycle.scene.ui.widgets.windows;

import java.util.ArrayList;
import java.util.HashMap;

import game.cycle.scene.game.SceneGame;
import game.cycle.scene.game.world.database.Database;
import game.cycle.scene.game.world.map.LocationProto;
import game.cycle.scene.ui.list.UIGame;
import game.cycle.scene.ui.widgets.Button;
import game.cycle.scene.ui.widgets.List;
import game.cycle.scene.ui.widgets.ListItem;
import game.cycle.scene.ui.widgets.Window;
import game.resources.Resources;
import game.resources.Tex;
import game.script.ui.app.ui_LocationAddMenuCancel;
import game.script.ui.app.ui_LocationDel;
import game.script.ui.app.ui_LocationLoad;
import game.script.ui.app.ui_UIGameEditor;
import game.tools.Const;

public class WindowEditorLocation extends Window {
	
	private UIGame uigame;
	
	public static final String uiEditorLocationLoad ="editor-location-load";
	public static final String uiEditorLocationAdd ="editor-location-add";
	public static final String uiEditorLocationDelete ="editor-location-delete";
	public static final String uiEditorLocationEdit ="editor-location-edit";
	public static final String uiEditorLocationList = "editor-location-list";
	
	public Button editorLocationLoad;
	public Button editorLocationAdd;
	public Button editorLocationDelete;
	public Button editorLocationEdit;
	public List   editorListLocation;
	
	public WindowEditorLocation(String title, UIGame ui, int layer, SceneGame scene) {
		super(title, ui, Alignment.CENTER, 326, 24, 0, 0, layer);
		this.setTexNormal(Resources.getTex(Tex.uiListLine));
		this.uigame = ui;
		this.setText("Locations");
		
		loadWidgets(scene);
		loadLocationList();
	}

	private void loadWidgets(SceneGame scene) {
		this.closeButton(true);
		this.closeButton.setScript(new ui_UIGameEditor(uigame, UIGame.uiEditorLocation));
		
		editorLocationLoad = new Button(uiEditorLocationLoad, "Load");
		editorLocationLoad.setSize(64, 32);
		editorLocationLoad.setPosition(Alignment.UPRIGTH, -262, -24);
		editorLocationLoad.setScript(new ui_LocationLoad(uigame, scene));
		this.add(editorLocationLoad);
		
		editorLocationAdd = new Button(uiEditorLocationAdd, "Add");
		editorLocationAdd.setSize(64, 32);
		editorLocationAdd.setPosition(Alignment.UPRIGTH, -262, -58);
		editorLocationAdd.setScript(new ui_LocationAddMenuCancel(uigame));
		this.add(editorLocationAdd);
	
		editorLocationDelete = new Button(uiEditorLocationDelete, "Delete");
		editorLocationDelete.setSize(64, 32);
		editorLocationDelete.setPosition(Alignment.UPRIGTH, -262, -92);
		editorLocationDelete.setScript(new ui_LocationDel(uigame));
		this.add(editorLocationDelete);
		
		editorLocationEdit = new Button(uiEditorLocationEdit, "Edit");
		editorLocationEdit.setSize(64, 32);
		editorLocationEdit.setPosition(Alignment.UPRIGTH, -262, -126);
		this.add(editorLocationEdit);

		editorListLocation = new List(uiEditorLocationList);
		editorListLocation.setSize(260, 300);
		editorListLocation.setVisible(16);
		editorListLocation.setPosition(Alignment.UPRIGTH, -0, -24);
		this.add(editorListLocation);
	}
	
	public void loadLocationList() {
		editorListLocation.clear();
		HashMap<Integer, LocationProto> base = Database.getBaseLocations();
		
		ArrayList<Boolean> mask = new ArrayList<Boolean>();
		mask.add(0, true);
		mask.add(1, false);
		mask.add(2, false);
		
		for(Integer key: base.keySet()){
			ArrayList<String> data = new ArrayList<String>();
			data.add(0, "" + key);
			data.add(1, "ID: " + key);
			data.add(2, " \""+base.get(key).title+"\"");
			
			ListItem item = new ListItem(data, mask);
			item.setFormatter("");
			editorListLocation.addElement(item);
		}
	}
	
	public int getSelectedListLocation() {
		ListItem item = editorListLocation.getSelected();
		
		if(item != null){
			return Integer.parseInt(item.get(0));
		}
		else{
			return Const.invalidId;
		}
	}
}