package game.cycle.scene.ui.widgets.windows;

import resources.Resources;
import resources.tex.Tex;
import ui.Alignment;
import ui.Window;
import ui.widgets.used.EquipmentWidget;
import ui.widgets.used.Image;
import ui.widgets.used.InventoryWidget;
import game.cycle.scene.ui.list.UIGame;
import game.script.ui.game.ui_CorpseUpdate;
import game.state.location.creature.Creature;

public class WindowCorpse extends Window {
	
	public static final String uiBackground = "-back";
	public static final String uiInventory = "-inventory";
	public static final String uiEquipment = "-equipment";
	
	public Image background;
	private InventoryWidget inventory;
	private EquipmentWidget equipment;
	
	public WindowCorpse(String title, UIGame ui, int layer, int inventorySizeX, int inventorySizeY) {
		super(title, ui, Alignment.CENTER, 390, 24, 0, 0, layer);
		this.setTexNormal(Resources.getTex(Tex.UI_LIST_LINE));
		this.setText("Container");
		loadWidgets(ui, inventorySizeX, inventorySizeY);
	}
	
	private void loadWidgets(UIGame ui, int sizeX, int sizeY) {
		this.closeButton(true);
		this.closeButton.setScript(new ui_CorpseUpdate(this));
		this.lockButton(true);
		
		background = new Image(this.title + uiBackground);
		background.setSize(390, 218);
		background.setPosition(Alignment.UPCENTER, 0, -24);
		this.add(background);
		
		inventory = new InventoryWidget(this.title+uiInventory, ui, 1, -110, -12);
		this.add(inventory);
		
		equipment = new EquipmentWidget(this.title+uiEquipment, ui, 2, 90, -90);
		this.add(equipment);
	}
	
	public void showCreature(Creature creature) {
		equipment.setCreature(creature);
		showContainer(creature);
	}
	
	private void showContainer(Creature creature) {
		if(creature != null){
			if(inventory == null){
				this.inventory.showContainer(null);
				this.setVisible(false);
			}
			else{
				this.setText("Corpse GUID: " + creature.getGUID());
				this.inventory.showContainer(creature.inventory);
				this.setVisible(true);
			}
		}
		else{
			this.setVisible(false);
		}
	}

	public void update() {
		inventory.update();
	}
}