package game.cycle.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Version;
import game.cycle.input.UserInput;
import game.cycle.scene.Scene;
import game.cycle.scene.game.world.World;
import game.cycle.scene.game.world.map.Location;
import game.cycle.scene.ui.list.UIGame;
import game.resources.Fonts;
import game.resources.Resources;

public class SceneGame extends Scene {

	// click modes
	public int currentClickMode = clickNone;
	public static final int clickNone = 0;
	public static final int clickPlayerAttack = 1;
	public static final int clickPlayerUse = 2;
	
	public static final int clickTerrain = 100;
	public static final int clickEditorNpc = 101;
	public static final int clickEditorGO = 102;
	public static final int clickEditorGOAdd = 103;
	public static final int clickEditorGOEdit = 104;
	public static final int clickEditorLocation = 105;
	
	// mode
	private boolean freeCameraMode;
	
	// ui
	private UIGame uimenu;
	private BitmapFont font;
	
	// data
	private World world;
	
	public SceneGame() {
		font = Resources.getFont(Fonts.fontDefault);
		this.ui = uimenu = new UIGame(this);
		
		world = new World();
		loadLocation(0, 0, 0);
	}

	public void loadLocation(int id, int playerPosX, int playerPosY) {
		world.loadLocation(id, playerPosX, playerPosY);
	}

	public void saveLocation() {
		world.saveLocation();
	}

	public float speed = 5.0f;
	@Override
	public void update(OrthographicCamera camera) {
		if(freeCameraMode){
			if(UserInput.key(Keys.W)){
				camera.translate(0.0f, speed);
			}
			if(UserInput.key(Keys.S)){
				camera.translate(0.0f, -speed);
			}
			if(UserInput.key(Keys.A)){
				camera.translate(-speed, 0.0f);
			}
			if(UserInput.key(Keys.D)){
				camera.translate(speed, 0.0f);
			}
		}
		else{
			camera.translate(-camera.position.x, -camera.position.y);
			camera.translate(world.getPlayer().sprite.getX() + Location.tileSize/2, world.getPlayer().sprite.getY());
		}
		
		camera.update();
		world.update(camera);
	}
	
	@Override
	public void sceneClick(int button) {
		if(!uimenu.isDialog()){
			if(button == Input.Buttons.LEFT){
				switch (currentClickMode) {
					case clickPlayerAttack:
						world.playerAttack();
						break;
				
					case clickPlayerUse:
						world.playerUse();
						break;
					
					case clickTerrain:
						world.editorWall(uimenu);
						break;
			
					case clickEditorNpc:
						world.editorNpc();
						break;
					
					case clickEditorGO:
					case clickEditorGOAdd:
					case clickEditorGOEdit:
						world.editorGO(uimenu, currentClickMode);
						break;
					
					default:
						world.playerMove(uimenu);
						break;
				}
			}
			
			if(button == Input.Buttons.RIGHT){
				world.playerAction();
			}
		}
	}

	@Override
	public void sceneKey(int key) {
		
	}
	
	@Override
	public void draw(SpriteBatch batch, OrthographicCamera camera) {
		world.draw(batch, camera, ui);
	}

	@Override
	public void drawGui(SpriteBatch batch) {
		String selected = "ui selected: ";
		if(uimenu.selected ){
			selected = "UI: " + uimenu.widgetSelected.title;
		}
		
		drawTextLine(batch, font, "Tile RollBoy v" + Version.version + "." + Version.subversion, 0);
		drawTextLine(batch, font, "Game scene", 1);
		drawTextLine(batch, font, selected, 2);
		drawTextLine(batch, font, "FPS: " + Gdx.graphics.getFramesPerSecond(), 3);
		drawTextLine(batch, font, "Node selected x: " + world.selectedNodeX + " y: " + world.selectedNodeY, 4);
		drawTextLine(batch, font, "Tiles: " + world.getLocation().counter, 5);
	}

	public void clickMode(int mode) {
		if(currentClickMode == mode){
			uimenu.setClickMode(this.currentClickMode, clickNone);
			this.currentClickMode = clickNone;
		}
		else{
			uimenu.setClickMode(this.currentClickMode, mode);
			this.currentClickMode = mode;
		}
	}

	public World getWorld() {
		return world;
	}
	
	public void freeCameraMode(){
		freeCameraMode = !freeCameraMode;
	}
	
	@Override
	public void dispose() {

	}
}
