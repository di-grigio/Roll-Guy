package game.cycle.scene.game.world.location.go;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.cycle.scene.game.world.database.GameConst;
import game.cycle.scene.game.world.event.LocationEvent;
import game.cycle.scene.game.world.event.trigger.Trigger;
import game.cycle.scene.game.world.event.trigger.TriggersLoader;
import game.cycle.scene.game.world.location.Location;
import game.cycle.scene.game.world.location.LocationObject;
import game.cycle.scene.game.world.location.creature.Player;
import game.script.ScriptGame;

public class GO extends LocationObject {
	
	// base data
	public GOProto proto;
	
	// flags
	public boolean passable;
	public int teleportId;
	
	// script
	public ScriptGame script;
	
	// params
	public int param1;
	public int param2;
	public int param3;
	public int param4;

	// triggers
	public Trigger [] triggers;
	public int [] triggerType;
	public int [] triggerParam;
	public int [] scripts;
	public int [] params1;
	public int [] params2;
	public int [] params3;
	public int [] params4;
	
	// current
	public int durability;
	
	// los
	public boolean losBlock;
	
	public GO(int guid, GOProto proto){
		super(guid, proto.fraction);
		
		this.go = true;
		this.proto = proto;
		this.passable = proto.passable;
		this.durability = proto.durabilityMax;
		
		// triggers
		triggers = new Trigger[GameConst.GO_TRIGGERS_COUNT];
		triggerType = new int[GameConst.GO_TRIGGERS_COUNT];
		triggerParam = new int[GameConst.GO_TRIGGERS_COUNT];
		scripts = new int[GameConst.GO_TRIGGERS_COUNT];
		params1 = new int[GameConst.GO_TRIGGERS_COUNT];
		params2 = new int[GameConst.GO_TRIGGERS_COUNT];
		params3 = new int[GameConst.GO_TRIGGERS_COUNT];
		params4 = new int[GameConst.GO_TRIGGERS_COUNT];
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public void event(LocationEvent event, int param) {
		boolean linkTrigger = false;
		
		if(event.type == LocationEvent.Type.TRIGGER){
			for(int i = 0; i < GameConst.GO_TRIGGERS_COUNT; ++i){
				if(triggers[i] != null){
					if(triggers[i].getType() == Trigger.ITEM){
						if(triggers[i].execute(event, event.source.containsItemId(triggers[i].getParam())) && !linkTrigger){
							this.triggerLink(i);
						}
					}
					else{
						if(triggers[i].execute(event, param) && !linkTrigger){
							this.triggerLink(i);
						}
					}
				}
			}
		}
	}
	
	private void triggerLink(int slot){
		for(int i = 0; i < GameConst.GO_TRIGGERS_COUNT; ++i){
			if(triggers[i] != null){
				if(triggers[i].getType() == Trigger.LINK){
					triggers[i].execute(null, slot);
				}
			}
		}
	}
	
	public void loadTriggers() {
		for(int i = 0; i < GameConst.GO_TRIGGERS_COUNT; ++i){
			triggers[i] = TriggersLoader.getTrigger(this, triggerType[i], triggerParam[i], scripts[i], params1[i], params2[i], params3[i], params4[i]);
		}
	}
	
	@Override
	public boolean damage(int value) {
		if(durability != 0){
			durability -= value;
			return (durability > 0);
		}
		else{
			return true;
		}
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void update(Location loc, OrthographicCamera camera, Player player, boolean losMode) {

	}
}