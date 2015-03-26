package game.cycle.scene.ui.widgets;

import game.cycle.scene.game.world.creature.Creature;
import game.cycle.scene.game.world.creature.items.Equipment;
import game.cycle.scene.game.world.creature.items.Item;
import game.cycle.scene.game.world.creature.items.ItemProto;
import game.cycle.scene.ui.Tooltip;
import game.cycle.scene.ui.list.UIGame;
import game.resources.Cursors;
import game.resources.Tex;
import game.script.ui.game.ui_PlayerDropItem;
import game.script.ui.game.ui_PlayerPickItem;

public class EquipmentWidget extends Window {
	
	public static final String uiSlotHead = "-slot-head";
	public static final String uiSlotChest = "-slot-chest";
	public static final String uiSlotHand1 = "-slot-h1";
	public static final String uiSlotHand2 = "-slot-h2";

	public static final String uiSlotItemHead = "-item-head";
	public static final String uiSlotItemChest = "-item-chest";
	public static final String uiSlotItemHand1 = "-item-h1";
	public static final String uiSlotItemHand2 = "-item-h2";
	
	private Image head;
	private Image chest;
	private Image hand1;
	private Image hand2;

	private UIGame uigame;
	private Equipment equip;
	
	public EquipmentWidget(String title, UIGame ui, int layer, int posX, int posY) {
		super(title, ui, Alignment.CENTER, 200, 0, posX, posY, layer);
		this.uigame = ui;
		loadWidgets();
	}

	private void loadWidgets() {
		head = new Image(this.title+uiSlotHead);
		head.setTexNormal(Tex.uiInventorySlotHead);
		head.setSize(64, 64);
		head.setPosition(Alignment.DOWNCENTER, 0, 0);
		head.setScript(new ui_PlayerDropItem(Equipment.slotHead, this));
		head.setLayer(1);
		this.add(head);
		
		chest = new Image(this.title+uiSlotChest);
		chest.setTexNormal(Tex.uiInventorySlotChest);
		chest.setSize(64, 96);
		chest.setPosition(Alignment.DOWNCENTER, 0, -100);
		chest.setScript(new ui_PlayerDropItem(Equipment.slotChest, this));
		chest.setLayer(1);
		this.add(chest);
		
		hand1 = new Image(this.title+uiSlotHand1);
		hand1.setTexNormal(Tex.uiInventorySlotHand1);
		hand1.setSize(64, 96);
		hand1.setPosition(Alignment.DOWNCENTER, -70, -100);
		hand1.setScript(new ui_PlayerDropItem(Equipment.slotHand1, this));
		hand1.setLayer(1);
		this.add(hand1);
		
		hand2 = new Image(this.title+uiSlotHand2);
		hand2.setTexNormal(Tex.uiInventorySlotHand2);
		hand2.setSize(64, 96);
		hand2.setPosition(Alignment.DOWNCENTER, 70, -100);
		hand2.setScript(new ui_PlayerDropItem(Equipment.slotHand2, this));
		hand2.setLayer(1);
		this.add(hand2);
	}
	
	public void setCreature(Creature creature) {
		if(creature == null){
			this.equip = null;
			setVisible(false);
		}
		else{
			if(visible){
				this.equip = null;
				setVisible(false);
			}
			else{
				this.equip = creature.equipment;
				loadSlots();
				setVisible(true);
			}
		}
	}


	private void loadSlots() {
		if(equip == null){
			return;
		}

		this.remove(this.title+uiSlotItemHead);
		this.remove(this.title+uiSlotItemChest);
		this.remove(this.title+uiSlotItemHand1);
		this.remove(this.title+uiSlotItemHand2);
		
		if(equip.head != null){
			dropHelmet(equip.head);
		}
		if(equip.chest != null){
			dropChest(equip.chest);
		}
		if(equip.hand1 != null){
			dropHand1(equip.hand1);
		}
		if(equip.hand2 != null){
			dropHand2(equip.hand2);
		}
	}

	public void dropItem(int slot, Item item) {
		if(equip == null){
			return;
		}
		if(slot == Equipment.slotHead){
			if(item.proto.type == ItemProto.typeHelemt && equip.head == null){
				dropHelmet(item);
			}
			else{
				Item tmp = equip.head;
				equip.head = null;
				this.remove(this.title+uiSlotItemHead);
				dropHelmet(item);
				Cursors.selectItem(tmp);
			}
		}
		else if(slot == Equipment.slotChest){
			if(item.proto.type == ItemProto.typeChest){
				if(equip.chest == null){
					dropChest(item);
				}
				else{
					Item tmp = equip.chest;
					equip.chest = null;
					this.remove(this.title+uiSlotItemChest);
					dropChest(item);
					Cursors.selectItem(tmp);	
				}
			}
		}
		else if(slot == Equipment.slotHand1 || slot == Equipment.slotHand2){
			if(item.proto.type == ItemProto.typeWeapon1H){
				if(slot == Equipment.slotHand1){
					if(equip.hand1 == null){
						dropHand1(item);
					}
					else{
						Item tmp = equip.hand1;
						equip.hand1 = null;
						this.remove(this.title+uiSlotItemHand1);
						dropHand1(item);
						Cursors.selectItem(tmp);
					}
				}
				else if(slot == Equipment.slotHand2){
					if(equip.hand2 == null){
						dropHand2(item);
					}
					else{
						Item tmp = equip.hand2;
						equip.hand2 = null;
						this.remove(this.title+uiSlotItemHand2);
						dropHand2(item);
						Cursors.selectItem(tmp);
					}
				}
			}
			else if(item.proto.type == ItemProto.typeWeapon2H){
				if(equip.hand1 == null){
					if(equip.hand2 == null){
						dropHand1(item);
						dropHand2(item);
					}
					else{
						Item tmp = equip.hand1;
						equip.hand1 = null;
						equip.hand2 = null;
						this.remove(this.title+uiSlotItemHand1);
						this.remove(this.title+uiSlotItemHand2);
						dropHand1(item);
						dropHand2(item);
						Cursors.selectItem(tmp);
					}
				}
			}
		}
	}
	
	private void dropHelmet(Item item){
		Image img = new Image(this.title+uiSlotItemHead);
		img.setSize(item.tex.getWidth(), item.tex.getHeight());
		img.setPosition(Alignment.DOWNCENTER, 0, 0);
		img.setTexNormal(item.tex);
		img.setScript(new ui_PlayerPickItem(Equipment.slotHead, this));
		img.setTooltip(new Tooltip(item.proto.title, "mass: "+item.proto.mass+"\nguid: "+item.guid));
		img.setLayer(2);
		this.add(img);
		equip.head = item;
		uigame.selectItem(null);
		setVisible(true);
	}
	
	private void dropChest(Item item) {
		Image img = new Image(this.title+uiSlotItemChest);
		img.setSize(item.tex.getWidth(), item.tex.getHeight());
		img.setPosition(Alignment.DOWNCENTER, 0, -100);
		img.setTexNormal(item.tex);
		img.setScript(new ui_PlayerPickItem(Equipment.slotChest, this));
		img.setTooltip(new Tooltip(item.proto.title, "mass: "+item.proto.mass+"\nguid: "+item.guid));
		img.setLayer(2);
		this.add(img);
		equip.chest = item;
		uigame.selectItem(null);
		setVisible(true);
	}
	
	private void dropHand1(Item item) {
		Image img = new Image(this.title+uiSlotItemHand1);
		img.setSize(item.tex.getWidth(), item.tex.getHeight());
		img.setPosition(Alignment.DOWNCENTER, -70, -100);
		img.setTexNormal(item.tex);
		img.setScript(new ui_PlayerPickItem(Equipment.slotHand1, this));
		img.setTooltip(new Tooltip(item.proto.title, "mass: "+item.proto.mass+"\nguid: "+item.guid));
		img.setLayer(2);
		this.add(img);
		equip.hand1 = item;
		uigame.selectItem(null);
		setVisible(true);
	}
	
	private void dropHand2(Item item) {
		Image img = new Image(this.title+uiSlotItemHand2);
		img.setSize(item.tex.getWidth(), item.tex.getHeight());
		img.setPosition(Alignment.DOWNCENTER, 70, -100);
		img.setTexNormal(item.tex);
		img.setScript(new ui_PlayerPickItem(Equipment.slotHand2, this));
		img.setTooltip(new Tooltip(item.proto.title, "mass: "+item.proto.mass+"\nguid: "+item.guid));
		img.setLayer(2);
		this.add(img);
		equip.hand2 = item;
		uigame.selectItem(null);
		setVisible(true);
	}

	// Pick item
	public void pickItem(int slot) {
		switch(slot){
			case Equipment.slotHead:
				Cursors.selectItem(equip.head);
				equip.head = null;
				this.remove(this.title+uiSlotItemHead);
				break;
				
			case Equipment.slotChest:
				Cursors.selectItem(equip.chest);
				equip.chest = null;
				this.remove(this.title+uiSlotItemChest);
				break;
			
			case Equipment.slotHand1:
			case Equipment.slotHand2:
				pickHandItem(slot);
				break;
		}
	}

	private void pickHandItem(int slot) {
		boolean H2 = false;
		
		if(slot == Equipment.slotHand1){
			if(equip.hand1.proto.type == ItemProto.typeWeapon2H){
				H2 = true;
			}
			else{
				Cursors.selectItem(equip.hand1);
				equip.hand1 = null;
				this.remove(this.title+uiSlotItemHand1);
			}
		}
		else if(slot == Equipment.slotHand2){
			if(equip.hand2.proto.type == ItemProto.typeWeapon2H){
				H2 = true;
			}
			else{
				Cursors.selectItem(equip.hand2);
				equip.hand2 = null;
				this.remove(this.title+uiSlotItemHand2);
			}
		}
		
		if(H2){
			Cursors.selectItem(equip.hand1);
			equip.hand1 = null;
			equip.hand2 = null;
			this.remove(this.title+uiSlotItemHand1);
			this.remove(this.title+uiSlotItemHand2);
		}
	}
}