package game.cycle.scene.game.world.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import game.cycle.scene.game.world.creature.Creature;
import game.cycle.scene.game.world.creature.NPC;
import game.cycle.scene.game.world.creature.Player;
import game.cycle.scene.game.world.creature.ai.AI;
import game.cycle.scene.game.world.database.Database;
import game.cycle.scene.game.world.database.GameConst;
import game.cycle.scene.game.world.go.GO;
import game.cycle.scene.game.world.go.GOFactory;
import game.cycle.scene.ui.list.UIGame;
import game.script.game.event.GameEvents;
import game.script.ui.app.ui_ExitGame;
import game.tools.Const;
import game.tools.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Location implements Disposable {
	
	public LocationProto proto;
	public static final int tileSize = 32;
	
	public int sizeX;
	public int sizeY;
	public Terrain [][] map;
	public Sprite [] sprites;
	
	public boolean isTurnBased;
	public boolean playerTurn;
	
	public HashMap<Integer, Creature> creatures;
	public HashMap<Integer, NPC> npcs;
	
	private Player player;
	
	public Location(Player player) {
		this.player = player;
		isTurnBased = false;
		
		creatures = new HashMap<Integer, Creature>();
		npcs = new HashMap<Integer, NPC>();
	}
	
	// add
	public void addCreature(NPC npc, int x, int y){
		map[x][y].creature = npc;
		creatures.put(npc.id, npc);
		npcs.put(npc.id, npc);
	}
	
	public void addCreature(Player player, int x, int y){
		map[x][y].creature = player;
		creatures.put(player.id, player);
	}

	// remove
	public void removeCreature(Creature creature){
		if(creature.id == player.id){
			new ui_ExitGame().execute();
		}
		else{
			Point pos = creature.getPosition();
			int x = pos.x;
			int y = pos.y;
		
			if(inBound(x, y)){
				creatures.remove(creature.id);
				map[x][y].creature = null;
			}
		}
	}
	
	public void removeCreature(int x, int y){
		if(inBound(x, y)){
			if(map[x][y].creature != null){
				if(map[x][y].creature.id == player.id){
					new ui_ExitGame().execute();
				}
				else{
					creatures.remove(map[x][y].creature.id);
				}
			}
		}
	}
	
	// Update
	public void update(Player player) {
		for(Creature creature: creatures.values()){
			creature.animationUpdate();
		}
		
		if(isTurnBased){
			if(playerTurn){
				player.update(this);
			}
			else{
				npcUpdate(player);
			}
		}
		else{
			player.update(this);
			npcUpdate(player);
		}
	}
	
	private void npcUpdate(Player player){
		if(isTurnBased){
			for(NPC npc: npcs.values()){
				npc.resetAp();
				AI.execute(this, npc);
			}
			
			this.playerTurn(player);
		}
	}
	
	public void playerTurn(Player player){
		player.resetAp();
		this.playerTurn = true;
		GameEvents.nextTurn();
		Log.debug("Player turn");
	}
	
	public void npcTurn(Player player){
		player.path = null;
		this.playerTurn = false;
		Log.debug("NPC turn");
	}
	
	public void gameModeTurnBased(boolean playerTurn) {
		this.isTurnBased = true;
	}

	public void gameModeRealTime() {
		this.isTurnBased = false;
	}
	
	// Draw
	public int counter;
	public void draw(OrthographicCamera camera, SpriteBatch batch) {
		ArrayList<Creature> creatures = new ArrayList<Creature>();
		Terrain node = null;
		counter = 0;
		
		int x = (int)(camera.position.x / tileSize);
		int y = (int)(camera.position.y / tileSize);
		int w = (Gdx.graphics.getWidth()/tileSize + 4)/2;
		int h = (Gdx.graphics.getHeight()/tileSize + 4)/2;
		
		int xmin = Math.max(0, x - w);
		int ymin = Math.max(0, y - h);
		int xmax = Math.min(sizeX, x + w);
		int ymax = Math.min(sizeY, y + h);
		
		for(int i = xmin; i < xmax; ++i){
			for(int j = ymin; j < ymax; ++j){
				counter++;
				node = map[i][j];
				
				sprites[node.proto.texture].setPosition(i*tileSize, j*tileSize);
				sprites[node.proto.texture].draw(batch);
				
				if(node.go != null){
					node.go.sprite.draw(batch);
				}

				if(node.creature != null){
					creatures.add(node.creature);
				}
			}
		}
		
		for(Creature creature: creatures){
			creature.draw(batch);
		}
	}

	@Override
	public void dispose() {
		for(int i = 0; i < sprites.length; ++i){
			sprites[i] = null;
		}
	}
	
	public boolean inBound(int x, int y){
		return (x >= 0 && x < sizeX && y >= 0 && y < sizeY);
	}

	// EDITOR
	public void editorTerrain(int x, int y, UIGame ui, int brush) {
		if(inBound(x, y)){
			int terrainid = ui.getSelectedListTerrain();
			
			if(terrainid != Const.invalidId){
				int size = 1;
				switch (brush) {
					case UIGame.modeTerrainBrush1: size = 1; break;
					case UIGame.modeTerrainBrush2: size = 2; break;
					case UIGame.modeTerrainBrush3: size = 3; break;
				}
				
				for(int i = 0; i < size; ++i){
					for(int j = 0; j < size; ++j){
						if(inBound(i + x, j + y)){
							map[i + x][j + y].proto = Database.getTerrainProto(terrainid);
						}
					}
				}
			}
		}
	}

	public void npcAdd(int x, int y, UIGame ui) {
		int id = ui.getSelectedListNpc();
		if(id != Const.invalidId){
			if(map[x][y].creature == null){
				NPC npc = new NPC(Database.getCreature(id));
				addCreature(npc, x, y);
				map[x][y].creature.sprite.setPosition(x*tileSize, y*tileSize);
			}
			else{
				removeCreature(x, y);
			}
		}
		else{
			removeCreature(x, y);
		}
	}
	
	public void npcEdit(int x, int y, UIGame ui){
		ui.npcEdit.setCreature(map[x][y].creature);
	}
	
	public void goAdd(int x, int y, UIGame ui) {
		int id = ui.getSelectedListGO();
		if(id != Const.invalidId){
			if(map[x][y].go == null){
				map[x][y].go = GOFactory.getGo(id, x, y, 0, 0, 0, 0);
			}
			else{
				map[x][y].go = null;
			}
		}
	}

	public void goEdit(int x, int y, UIGame ui) {
		ui.goEdit.setGO(map[x][y].go);
	}
	
	// PLAYER ACTIONS
	public static final float interactRange = 2*Location.tileSize * 1.45f;
	
	public void talkWithNpc(Player player, UIGame ui, int x, int y) {
		Creature npc = map[x][y].creature;
		
		if(npc != null && npc.id != player.id){
			float delta = getRange(player.sprite, npc.sprite);
			
			if(delta < interactRange){
				ui.npcTalk(ui, player, npc);
			}
		}
	}
	
	public void useGO(Player player, int x, int y){
		GO go = map[x][y].go;
		
		if(go != null && go.script1 != null){
			float delta = getRange(player.sprite, go.sprite);
			
			if(delta < interactRange){
				go.script1.execute();
			}
		}
	}

	public void attack(int x, int y, Creature damager) {
		int damage = damager.proto.stats.strength;
		
		if(inBound(x, y)){
			// attack the Creature
			Creature creature = map[x][y].creature;
			if(creature != null && creature.id != damager.id){
				float delta = getRange(damager.sprite, creature.sprite);
				
				if(delta < interactRange){
					if(!isTurnBased){
						GameEvents.gameModeTurnBased(true);
					}
					
					int apPrice = GameConst.getAttackAp(damager);
					
					if(damager.ap - apPrice >= 0){
						damager.ap -= GameConst.getAttackAp(damager);
						
						boolean targetIsAlive = creature.damage(damage);
						creature.aidata.addEnemy(damager);
						
						if(!targetIsAlive){
							GameEvents.gameModeRealTime();
							removeCreature(creature);
							Log.debug("Creature id: " + creature.id + " died");
						}
					}
				}
				return;
			}
			
			// attack the GO
			GO go = map[x][y].go;
			if(go != null && go.proto.durabilityMax > 0){
				if(isTurnBased){
					damager.ap -= GameConst.getAttackAp(damager);
				}
				
				go.durability -= damage;
				 
				Log.debug("GO id: " + go.id + " get " + damage + " hp: " + go.durability + "/" + go.proto.durabilityMax);
				 
				if(go.durability <= 0){
					map[x][y].go = null;
					Log.debug("GO id: " + go.id + " destroyed");
				}
				 
				return;
			}
		}
	}
	
	public float getRange(Sprite a, Sprite b){
		return new Vector2(a.getX() - b.getX(), a.getY() - b.getY()).len();
	}
}
