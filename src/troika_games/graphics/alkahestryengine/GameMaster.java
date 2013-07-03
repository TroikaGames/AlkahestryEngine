package troika_games.graphics.alkahestryengine;

import android.content.res.Resources;

public class GameMaster
{
	public static final int DO_WALK_ABOUT = 0;
	public int currentState = DO_WALK_ABOUT;
	public int currentDirection = 0;
	public int xCamera = 0;
	public int yCamera = 0;
	public boolean isMoving = false;
	
	public GameMap currentMap = new GameMap();
	
	public GameMaster()
	{
	}
	
	public void loadMap(String mapFile, Resources resources)
	{
		this.currentMap.loadMap(mapFile, resources);
	}
}
