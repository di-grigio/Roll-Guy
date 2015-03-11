package game.cycle.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Version;
import game.cycle.scene.Scene;
import game.cycle.scene.game.world.World;
import game.cycle.scene.ui.list.UIGame;
import game.resources.Fonts;
import game.resources.Resources;

public class SceneGame extends Scene {

	// click modes
	public int currentClickMode = clickNone;
	public static final int clickNone = 0;
	public static final int clickPlayerAttack = 1;
	public static final int clickEditor = 2;
	public static final int clickEditorNpc = 3;
	public static final int clickEditorGO = 4;
	
	// ui
	private UIGame uimenu;
	private BitmapFont font;
	
	// data
	private World world;
	
	public SceneGame() {
		font = Resources.getFont(Fonts.fontDefault);
		this.ui = uimenu = new UIGame(this);
		
		world = new World();
	}
	
	@Override
	public void update(OrthographicCamera camera) {
		world.update(camera);
	}
	
	@Override
	public void sceneClick(int button) {
		if(!uimenu.isDialog()){
			switch (currentClickMode) {
				case clickPlayerAttack:
					world.playerAttack();
					break;
			
				case clickEditor:
					world.editorWall();
					break;
			
				case clickEditorNpc:
					world.editorNpc();
					break;
					
				case clickEditorGO:
					world.editorGO(uimenu);
					break;
					
				default:
					world.playerMove(uimenu);
					break;
			}
		}
	}

	@Override
	public void sceneKey(int key) {
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		world.draw(batch, ui);
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
	
	@Override
	public void dispose() {

	}
}
