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
import game.cycle.scene.game.world.creature.items.Item;
import game.cycle.scene.game.world.database.Database;
import game.cycle.scene.ui.list.UIGame;
import game.resources.Cursors;
import game.resources.Fonts;
import game.resources.Resources;
import game.script.game.event.GameEvents;

public class SceneGame extends Scene {

	// mode
	private boolean freeCameraMode;
	
	// ui
	private UIGame uimenu;
	private BitmapFont font;
	
	// data
	private World world;
	private Database database;
	
	public SceneGame() {
		this.database = new Database();
		this.ui = uimenu = new UIGame(this);
		font = Resources.getFont(Fonts.fontDefault);
		world = new World(uimenu);
		new GameEvents(this, uimenu);
		
		// test
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
			world.updateFreeCamera(camera);
		}
		
		camera.update();
		world.update(camera);
	}
	
	@Override
	public void sceneClick(int button) {
		if(!uimenu.isDialog()){
			if(button == Input.Buttons.LEFT){
				world.actionFirst(uimenu);
			}
			else if(button == Input.Buttons.RIGHT){
				world.actionSecond(uimenu);
			}
		}
	}

	@Override
	public void sceneKey(int key) {
		
	}
	
	@Override
	public void draw(SpriteBatch batch, OrthographicCamera camera) {
		world.draw(batch, camera, uimenu);
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
		drawTextLine(batch, font, "Selected x: " + world.getSelectedNode().x + " y: " + world.getSelectedNode().y, 6);
		drawTextLine(batch, font, "Selected Creature GUID: " + world.getSelectedCreature(), 7);
		
		updateSelectedItem(batch);
	}
	
	private void updateSelectedItem(SpriteBatch batch) {
		Item item = Cursors.getSelectedItem();
		
		if(item != null){
			int texX = item.proto.sizeX*32;
			int texY = item.proto.sizeY*32;
			int x = UserInput.mouseX;
			int y = Gdx.graphics.getHeight() - UserInput.mouseY;
			batch.draw(item.tex, x, y - texY, texX, texY);
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
		database.dispose();
	}
}
