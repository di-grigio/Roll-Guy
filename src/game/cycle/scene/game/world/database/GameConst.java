package game.cycle.scene.game.world.database;

import game.cycle.scene.game.world.creature.Creature;

public class GameConst {

	public static final int apMax = 10;
	
	public static final int uiActionPanelSlots = 12;
	
	public static final int inventorySizeX = 5;
	public static final int inventorySizeY = 6;
	
	public static final int tileSize = 32;
	
	// PLAYER ACTIONS
	public static final float interactRange = 1.45f;

	// AI
	public static final float aiReactionRadius = 100;

	// GO
	public static final int goTriggersCount = 4;
	
	public static int getMovementAP(Creature creature) {
		return 1; // test value
	}

	public static int getAttackAp(Creature creature) {
		return 3; // test value
	}
}
