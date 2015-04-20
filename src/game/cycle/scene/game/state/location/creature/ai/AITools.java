package game.cycle.scene.game.state.location.creature.ai;

import game.cycle.scene.game.state.location.Location;
import game.cycle.scene.game.state.location.LocationObject;
import game.cycle.scene.game.state.location.creature.Creature;

public class AITools {

	public static boolean isVisible(Location loc, Creature agent, LocationObject target){
		return (loc.checkVisiblity(agent, target) == null && Perception.isVisible(agent, loc, target));
	}
}
