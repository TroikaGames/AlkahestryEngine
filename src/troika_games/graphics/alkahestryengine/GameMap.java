package troika_games.graphics.alkahestryengine;

import troika_games.graphics.alkahestryengine.R;
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
	
	// TODO: make this a tile mapping rather than just one MapTile (array of MapTiles).
	private MapTile tileMapping = null;
	private MapTile tileMappingBlank = null;
	// TODO: Make this a tile mapping rather than a straight bitmap (array of Bitmaps).
	private Bitmap currentMapTileSet = null;
	
	public GameMap()
	{
	}
	
	// TODO: Make this actually load a dynamic map.
	public void loadMap(String mapFile, Resources resources)
	{
		// TODO: Probably make this dynamically sized based on the map file we load.
		this.mapLayout = new int[MAX_WIDTH][MAX_HEIGHT][MAX_LAYERS];
		this.currentMapTileSet = BitmapFactory.decodeResource(resources, R.drawable.grass);
		this.tileMapping = new MapTile(true, 1, 0, 0, new Rect(0, 0, 32, 32), -1);
		this.tileMappingBlank = new MapTile(true, 0, 0, 0, new Rect(0, 0, 32, 32), -1);
		
		for (int k = 0; k < MAX_LAYERS; ++k)
		{
			for (int i = 0; i < MAX_WIDTH; ++i)
			{
				for (int j = 0; j < MAX_HEIGHT; ++j)
				{
					if (k == 0)
					{
						mapLayout[i][j][k] = 1;
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
			return this.tileMappingBlank;
		}
		
		return this.tileMapping;
	}
	
	public Bitmap getTileBitmap(int tileNumber)
	{
		return this.currentMapTileSet;
	}
}
