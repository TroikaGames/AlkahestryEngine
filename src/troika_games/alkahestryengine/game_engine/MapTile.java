package troika_games.alkahestryengine.game_engine;

import android.graphics.Rect;

public class MapTile
{
	private boolean canWalk = true;
	private boolean isStairs = false;
	private boolean stairsUp = false;
	private int tileNum = 0;
	private int animationSpeed = 0;

	private int animationFrames = 0;
	private int currentAnimationFrame = 0;
	private int animationTime = 0;

	private Rect tileRectangle = null;

	private int battleBackGroundImageResource = 0;
	
	public MapTile(boolean canWalk, int tileNum, int animationSpeed, int animationFrames, 
					Rect tileRectangle, int battleBgResource)
	{
		this.canWalk = canWalk;
		this.tileNum = tileNum;
		this.animationSpeed = animationSpeed;
		this.animationFrames = animationFrames;
		this.tileRectangle = new Rect(tileRectangle);
		this.battleBackGroundImageResource = battleBgResource;
	}
	
	public boolean canWalkOnTile()
	{
		return this.canWalk;
	}
	
	public int getTileNumber()
	{
		return this.tileNum;
	}
	
	public int getAnimationSpeed()
	{
		return this.animationSpeed;
	}
	
	public Rect getTileRectangle()
	{
		return this.tileRectangle;
	}
}
