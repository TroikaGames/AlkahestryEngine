package troika_games.alkahestryengine.game_engine;

import troika_games.alkahestryengine.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class GameMap
{
	// TODO: We probably want map maximums but maybe not?
	public static int MAX_LAYERS = 3;
	public static int MAX_WIDTH = 50;
	public static int MAX_HEIGHT = 50;
	
	public int[][][] mapLayout = null;
	
	public int bitshift = 0;
	public int divisor = 0;
	public int tileHeight = 0;
	public int tileWidth = 0;
	public int maxWidth = 0;
	public int maxHeight = 0;
	
	private Bitmap currentMapTileSet = null;
	private MapTile[] tileMapping = null;
		
	public GameMap()
	{
	}
	
	// TODO: Make this actually load a dynamic map.
	public void loadMap(String mapFile, Resources resources)
	{
		this.bitshift = 5;
		this.divisor = 31;
		this.tileHeight = 32;
		this.tileWidth = 32;
		this.maxWidth = MAX_WIDTH;
		this.maxHeight = MAX_HEIGHT;
		
		// TODO: Probably make this dynamically sized based on the map file we load.
		this.mapLayout = new int[MAX_WIDTH][MAX_HEIGHT][MAX_LAYERS];
		this.currentMapTileSet = BitmapFactory.decodeResource(resources, R.drawable.tileset);
		this.tileMapping = new MapTile[3];
		
		this.tileMapping[0] = new MapTile(true, 0, 0, 0, new Rect(0, 0, 32, 32), -1);
		this.tileMapping[1] = new MapTile(true, 1, 0, 0, new Rect(0, 0, 32, 32), -1);
		this.tileMapping[2] = new MapTile(true, 2, 0, 0, new Rect(0, 33, 32, 64), -1);
		
		java.util.Random generator = new java.util.Random();
		
		for (int k = 0; k < MAX_LAYERS; ++k)
		{
			for (int i = 0; i < MAX_WIDTH; ++i)
			{
				for (int j = 0; j < MAX_HEIGHT; ++j)
				{
					if (k == 0)
					{
						int randomInt = generator.nextInt(100);
						
						if (randomInt < 75)
						{
							mapLayout[i][j][k] = 1;
						}
						else
						{
							mapLayout[i][j][k] = 2;
						}
					}
					else
					{
						mapLayout[i][j][k] = 0;
					}
				}
			}
		}
	}
	
	public MapTile getTile(int x, int y, int z)
	{
		if (z != 0)
		{
			return this.tileMapping[0];
		}
		
		return this.tileMapping[this.mapLayout[x][y][z]];
	}
	
	public Bitmap getTileBitmap(int tileNumber)
	{
		return this.currentMapTileSet;
	}
}
