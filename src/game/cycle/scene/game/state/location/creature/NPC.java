package game.cycle.scene.game.state.location.creature;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.cycle.scene.game.state.database.proto.CreatureProto;
import game.cycle.scene.game.state.event.Event;
import game.cycle.scene.game.state.location.Location;
import game.cycle.scene.game.state.location.creature.ai.AI;
import game.cycle.scene.game.state.location.creature.ai.AIData;
import game.cycle.scene.game.state.location.go.GO;
import game.resources.Resources;
import game.resources.tex.Tex;

public class NPC extends Creature {

	// AI
	public AIData aidata;
	private static Texture warningTex;

	public NPC(int guid, CreatureProto proto) {
		super(guid, proto);
		NPC.warningTex = Resources.getTex(Tex.NPC_WARNING);
		this.npc = true;
		aidata = new AIData();
	}
	
	@Override
	public void update(Location location, OrthographicCamera camera, Player player, boolean losMode) {
		if(!aidata.fullUpdate){
			AI.fullUpdate(location, this);
		}
		if(!isMoved){
			AI.softUpdate(location, this);
		}
		
		super.update(location, camera, player, losMode);
	}

	public void resetAI() {
		this.resetPath();
		aidata.reset();
	}

	public void aiEvent(Location location, Event event) {
		AI.event(location, event, this);
	}
	
	public void addWayPoint(GO go, int number, int pause) {
		aidata.addWayPoint(go, number, pause);
	}

	public void printWayPoints() {
		aidata.printWayPoints();
	}
	
	public void addDialogTopic(int id){
		proto.addDialogTopic(id);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		if(isAlive()){
			if(aidata.viewedEnemy.size() > 0){
				batch.draw(warningTex, sprite.getX(), sprite.getY());
			}
		}
	}
}