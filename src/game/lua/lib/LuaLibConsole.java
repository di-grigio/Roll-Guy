package game.lua.lib;

import game.cycle.scene.ui.UI;
import game.tools.Log;

public class LuaLibConsole {

	public void print(String text){
		Log.lua(text);
	}
	
	public void splash(String text){
		UI.setInformation(text);
	}
}
