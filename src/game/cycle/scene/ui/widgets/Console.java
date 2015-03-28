package game.cycle.scene.ui.widgets;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.cycle.scene.ui.Scroll;
import game.resources.Tex;
import game.script.game.event.GameEvents;
import game.tools.Const;

public class Console extends TextWidget implements Scroll {
	
	protected static final int lineHeight = 18;
	protected int maxDisplay = 10;
	protected int lineSelected = Const.invalidId;
	protected int scrollAmount = 0;
	
	private ArrayList<String> items;
	private String text;
	
	public Console(String title) {
		super(title);
		this.setTexNormal(Tex.uiBackgroundLightSelected);
		this.setSize(Gdx.graphics.getWidth(), 200);
		this.setPosition(Alignment.UPLEFT, 0, 0);
		this.maxTextLength = 100;
		this.scroll = true;
		
		this.items = new ArrayList<String>();
		this.text = "";
	}

	public void addLine(String text){
		this.items.add(text);
		
		if(items.size() > maxDisplay){
			scroll(1);
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(!visible){
			this.text = "";
		}
		super.setVisible(visible);
	}
	
	@Override
	public void scroll(int amount) {
		scrollAmount += amount;
		
		if(scrollAmount < 0 || scrollAmount >= items.size()){
			scrollAmount -= amount;
		}
	}

	@Override
	public void key(char key) {
		if(key == 13){ // enter == 13
			GameEvents.consoleCommand(text);
			text = "";
		}
		else{
			if(key == 8){ // backspace == 8
				if(this.text.length() > 0){
					this.text = this.text.substring(0, this.text.length() - 1);
				}
			}
			else{
				if(this.text.length() < maxTextLength){
					if(textFilter != null && textFilter.check(key)){
						this.text += key;
					}
				}
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch sprites) {
		sprites.draw(texNormal, x, y, sizeX, sizeY);
		
		for(int i = scrollAmount, j = 0; j < maxDisplay && i < items.size(); ++i, ++j){
			font.draw(sprites, items.get(i), x + 5, y + sizeY - j * lineHeight - 4);
		}
		font.draw(sprites, ">"+text, x + 5, y + sizeY - maxDisplay * lineHeight - 4);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		this.items.clear();
		this.items = null;
	}
}
