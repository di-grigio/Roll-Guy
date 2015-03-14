package game.cycle.scene.game.world.go;

import com.badlogic.gdx.graphics.g2d.Sprite;

import game.cycle.scene.game.world.database.Database;
import game.cycle.scene.game.world.map.Location;
import game.resources.Resources;
import game.resources.Tex;
import game.script.Script;
import game.script.game.go.go_DoorTeleport;
import game.script.game.go.go_DoorUse;

public class GOFactory {

	public static GO getGo(int id, int x, int y, int param1, int param2, int param3, int param4){
		GO go = new GO(Database.getGO(id));
		
		go.sprite = new Sprite(Resources.getTex(Tex.go + go.proto.texure_1));
		go.sprite.setPosition(x*Location.tileSize, y*Location.tileSize);
		
		go.param1 = param1;
		go.param2 = param2;
		go.param3 = param3;
		go.param4 = param4;
		
		go.script1 = getScript(go);
		
		return go;
	}

	
	// GO Script base
	public static final int go_DoorUse = 1;
	public static final int go_DoorTeleport = 2;
	
	private static Script getScript(GO go) {
		int scriptId = go.proto.scriptId_1;
		
		switch(scriptId){
			case go_DoorUse:
				return new go_DoorUse(go);
				
			case go_DoorTeleport:
				return new go_DoorTeleport(go);
		}
		
		return null;
	}
}
