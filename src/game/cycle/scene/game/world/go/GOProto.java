package game.cycle.scene.game.world.go;

public class GOProto {
	
	// key data
	public int id;
	public String title;
	
	// sprite
	public int texure_1;
	public int texure_2;
	
	// flags
	public boolean visible;
	public boolean trigger;
	public boolean teleport;
	public boolean usable;
	public boolean container;
	public boolean passable;
	public boolean losBlock;
	public boolean waypoint;
	public boolean dragble;
	
	// params
	public int durabilityMax;
	
	// scripts
	public int scriptId;
	public int fraction;
	
	// container
	public int containerSizeX;
	public int containerSizeY;
}
