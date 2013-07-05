package troika_games.alkahestryengine.game_engine;

import android.content.res.Resources;
import android.graphics.Point;
import android.view.MotionEvent;

public class GameMaster
{
	public static final int DO_WALK_ABOUT = 0;
	public int currentState = DO_WALK_ABOUT;
	public int currentDirection = 0;
	public int xCamera = 0;
	public int yCamera = 0;
	public int xMaxCamera = 1000;
	public int yMaxCamera = 0;
	public Point centerPosition = new Point();
	public boolean isMoving = false;
	public Point currentPosition = new Point();
	public int[] spriteDirectionMappings = new int[8];
	
	public GameMap currentMap = new GameMap();
	
	private double[] directionAnglesCenter = new double[8];
	private Point screenDims = null;
	private int xArrayCoord = -1;
	private int yArrayCoord = -1;
	// TODO: Get these from the sprite or pass them or something.
	private int characterSpriteWidth = 93;
	private int characterSpriteHeight = 68;
	
	public GameMaster(int dimsX, int dimsY, int centerX, int centerY)
	{
		this.screenDims = new Point(dimsX, dimsY);
		this.centerPosition.x = centerX;
		this.centerPosition.y = centerY;
	}
	
	public void setupDirectionAngles(float radius)
	{
		double angle = Math.PI / 4;
		double curAngle = angle / 2;
		
		for (int i = 0; i <= 7; ++i)
		{
			this.directionAnglesCenter[i] = curAngle;
			//double xPoint = this.currentPosition.x + radius * (Math.cos(curAngle));
			//double yPoint = this.currentPosition.y + radius * (Math.sin(curAngle));
					
			curAngle += angle;
		}
	}
	
	public void loadMap(String mapFile, Resources resources)
	{
		this.currentMap.loadMap(mapFile, resources);
		
		int xTilesOnScreen = this.screenDims.x / this.currentMap.tileWidth;
		
		this.xMaxCamera = (this.currentMap.maxWidth - xTilesOnScreen) * this.currentMap.tileWidth;
		
		if (this.xMaxCamera < 0)
		{
			this.xMaxCamera = 0;
		}

		int yTilesOnScreen = this.screenDims.y / this.currentMap.tileHeight;

		this.yMaxCamera = (this.currentMap.maxHeight - yTilesOnScreen) * this.currentMap.tileHeight;

		if (this.yMaxCamera < 0)
		{
			this.yMaxCamera = 0;
		}
	}
	
	public boolean handleWalkAboutInput(MotionEvent event)
	{
		int eventAction = event.getAction();
		
		if (eventAction == MotionEvent.ACTION_DOWN || eventAction == MotionEvent.ACTION_MOVE)
		{
			this.currentDirection = getDirection(event.getX(), event.getY());
			Point suggestedPosition = new Point(this.currentPosition);
			
			 // 0 = South, 1 = West, 2 = North, 3 = East, 4 = North West, 5 = North East, 6 = South East, 7 = South West
			
			// South
			if (this.currentDirection == 0)
			{
				boolean moveCam = false;
				MapTile tile = null;
				
				if (this.yCamera < (this.yMaxCamera - 4) && this.currentPosition.y == this.centerPosition.y)
				{
					moveCam = true;
					Point collisionPosition = new Point(this.currentPosition.x, 
															this.currentPosition.y + this.characterSpriteHeight / 2);
					tile = getTile(this.xCamera, this.yCamera + 5, collisionPosition, 0); 
				}
				else
				{
					suggestedPosition.y += 5;
					Point collisionPosition = new Point(suggestedPosition.x, suggestedPosition.y + (this.characterSpriteHeight / 2));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCam)
						{
							this.yCamera += 5;
						}
						else
						{
							this.currentPosition.y += 5;
						}
					}
				}
			}
			// West
			else if (this.currentDirection == 1)
			{
				boolean moveCam = false;
				MapTile tile = null;
				
				if (this.xCamera > 4 && this.currentPosition.x == this.centerPosition.x)
				{
					moveCam = true;
					Point collisionPosition = new Point(this.currentPosition.x - this.characterSpriteWidth / 3, 
															this.currentPosition.y + this.characterSpriteHeight / 3);
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0); 
				}
				else
				{
					suggestedPosition.x = this.currentPosition.x - 5;
					suggestedPosition.y = this.currentPosition.y;
					Point collisionPosition = new Point(suggestedPosition.x - (this.characterSpriteWidth / 3), suggestedPosition.y);
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0); 
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCam)
						{
							this.xCamera -= 5;
						}
						else
						{
							this.currentPosition.x -= 5;
						}
					}
				}
			}
			// North
			else if (this.currentDirection == 2)
			{
				boolean moveCam = false;
				MapTile tile = null;
				suggestedPosition.x = this.currentPosition.x;
				suggestedPosition.y = this.currentPosition.y;

				if (this.yCamera > 4 && this.currentPosition.y == this.centerPosition.y)
				{
					moveCam = true;
					tile = getTile(this.xCamera, this.yCamera - 5, suggestedPosition, 0);
				}
				else
				{
					suggestedPosition.y -= 5;
					Point collisionPosition = new Point(suggestedPosition.x, suggestedPosition.y - (this.characterSpriteHeight / 4));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCam)
						{
							this.yCamera -= 5;
						}
						else
						{
							this.currentPosition.y -= 5;
						}
					}
				}
			}
			// East
			else if (this.currentDirection == 3)
			{
				boolean moveCam = false;
				MapTile tile = null;
				
				if (this.xCamera < (this.xMaxCamera - 4) && this.currentPosition.x == this.centerPosition.x)
				{
					moveCam = true;
					Point collisionPosition = new Point(this.currentPosition.x + this.characterSpriteWidth / 2, 
															this.currentPosition.y + this.characterSpriteHeight / 3);
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0); 
				}
				else
				{
					suggestedPosition.x += 5;
					Point collisionPosition = new Point(suggestedPosition.x, suggestedPosition.y - (this.characterSpriteHeight / 4));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0); 
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCam)
						{
							this.xCamera += 5;
						}
						else
						{
							this.currentPosition.x += 5;
						}
					}
				}
			}
			// NorthWest
			else if (this.currentDirection == 4)
			{
				boolean moveCamNorth = (this.yCamera > 4 && this.currentPosition.y == this.centerPosition.y);
				boolean moveCamWest = (this.xCamera > 4 && this.currentPosition.x == this.centerPosition.x);
				MapTile tile = null;
				
				if (moveCamNorth && moveCamWest)
				{
					Point collisionPosition = new Point(this.currentPosition.x - this.characterSpriteWidth / 3, 
															this.currentPosition.y + this.characterSpriteHeight / 3);
					tile = getTile(this.xCamera, this.yCamera - 5, collisionPosition, 0);
				}
				else if (moveCamNorth && !moveCamWest)
				{
					suggestedPosition.x = this.currentPosition.x - 5;
					suggestedPosition.y = this.currentPosition.y;
					Point collisionPosition = new Point(suggestedPosition.x - (this.characterSpriteWidth / 3), suggestedPosition.y);
					tile = getTile(this.xCamera, this.yCamera - 5, collisionPosition, 0); 
				}
				else if (!moveCamNorth && moveCamWest)
				{
					suggestedPosition.y -= 5;
					Point collisionPosition = new Point(this.currentPosition.x - this.characterSpriteWidth / 3, 
															this.currentPosition.y - this.characterSpriteHeight);
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0); 
				}
				else if (!moveCamNorth && !moveCamWest)
				{
					suggestedPosition.x = this.currentPosition.x - 5;
					suggestedPosition.y -= 5;
					Point collisionPosition = new Point(suggestedPosition.x - (this.characterSpriteWidth / 3), 
															suggestedPosition.y - (this.characterSpriteHeight / 4));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCamNorth)
						{
							this.yCamera -= 5;
						}
						else
						{
							this.currentPosition.y -= 5;
						}
						
						if (moveCamWest)
						{
							this.xCamera -= 5;
						}
						else
						{
							this.currentPosition.x -= 5;
						}
					}
				}
			}
			// NorthEast
			else if (this.currentDirection == 5)
			{
				MapTile tile = null;
				suggestedPosition.x = this.currentPosition.x;
				suggestedPosition.y = this.currentPosition.y;

				boolean moveCamNorth = (this.yCamera > 4 && this.currentPosition.y == this.centerPosition.y);
				boolean moveCamEast = (this.xCamera < (this.xMaxCamera - 4) && this.currentPosition.x == this.centerPosition.x);
					
				if (moveCamNorth && moveCamEast)
				{
					Point collisionPosition = new Point(this.currentPosition.x + this.characterSpriteWidth / 2, 
															this.currentPosition.y + this.characterSpriteHeight / 3);
					tile = getTile(this.xCamera, this.yCamera - 5, collisionPosition, 0);
				}
				else if (moveCamNorth && !moveCamEast)
				{
					suggestedPosition.x += 5;
					Point collisionPosition = new Point(suggestedPosition.x, suggestedPosition.y - (this.characterSpriteHeight / 4));
					tile = getTile(this.xCamera, this.yCamera - 5, collisionPosition, 0);
				}
				else if (!moveCamNorth && moveCamEast)
				{
					suggestedPosition.y -= 5;
					
					Point collisionPosition = new Point(this.currentPosition.x + this.characterSpriteWidth / 2, 
															this.currentPosition.y + this.characterSpriteHeight / 4);
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				else if (!moveCamNorth && !moveCamEast)
				{
					suggestedPosition.x += 5;
					suggestedPosition.y -= 5;
					Point collisionPosition = new Point(suggestedPosition.x, suggestedPosition.y - (this.characterSpriteHeight / 4));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCamNorth)
						{
							this.yCamera -= 5;
						}
						else
						{
							this.currentPosition.y -= 5;
						}
						
						if (moveCamEast)
						{
							this.xCamera += 5;
						}
						else
						{
							this.currentPosition.x += 5;
						}
					}
				}
			}
			// SouthEast
			else if (this.currentDirection == 6)
			{
				boolean moveCamSouth = (this.yCamera < (this.yMaxCamera - 4) && this.currentPosition.y == this.centerPosition.y);
				boolean moveCamEast = (this.xCamera < (this.xMaxCamera - 4) && this.currentPosition.x == this.centerPosition.x);
				MapTile tile = null;
				
				if (moveCamSouth && moveCamEast)
				{
					Point collisionPosition = new Point(this.currentPosition.x + this.characterSpriteWidth / 2, 
															this.currentPosition.y + this.characterSpriteHeight / 3);
					tile = getTile(this.xCamera, this.yCamera + 5, collisionPosition, 0);					
				}
				else if (moveCamSouth && !moveCamEast)
				{
					Point collisionPosition = new Point(this.currentPosition.x, 
															this.currentPosition.y + this.characterSpriteHeight);
					tile = getTile(this.xCamera, this.yCamera + 5, collisionPosition, 0);
				}
				else if (!moveCamSouth && moveCamEast)
				{
					suggestedPosition.y += 5;
					Point collisionPosition = new Point(suggestedPosition.x + this.characterSpriteWidth / 2,
															suggestedPosition.y + (this.characterSpriteHeight / 2));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);					
				}
				else if (!moveCamSouth && !moveCamEast)
				{
					suggestedPosition.y += 5;
					suggestedPosition.x += 5;
					Point collisionPosition = new Point(suggestedPosition.x, suggestedPosition.y + this.characterSpriteHeight);
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCamSouth)
						{
							this.yCamera += 5;
						}
						else
						{
							this.currentPosition.y += 5;
						}
						
						if (moveCamEast)
						{
							this.xCamera += 5;
						}
						else
						{
							this.currentPosition.x += 5;
						}
					}
				}
			}
			// SouthWest
			else if (this.currentDirection == 7)
			{
				boolean moveCamSouth = (this.yCamera < (this.yMaxCamera - 4) && this.currentPosition.y == this.centerPosition.y);
				boolean moveCamWest = (this.xCamera > 4 && this.currentPosition.x == this.centerPosition.x);
				MapTile tile = null;
				
				if (moveCamSouth && moveCamWest)
				{
					Point collisionPosition = new Point(this.currentPosition.x - this.characterSpriteWidth / 3, 
															this.currentPosition.y + this.characterSpriteHeight / 3);
					tile = getTile(this.xCamera, this.yCamera + 5, collisionPosition, 0);
				}
				else if (moveCamSouth && !moveCamWest)
				{
					suggestedPosition.x = this.currentPosition.x - 5;
					Point collisionPosition = new Point(this.currentPosition.x - (this.characterSpriteWidth / 3),
															this.currentPosition.y + this.characterSpriteHeight / 2);
					tile = getTile(this.xCamera, this.yCamera + 5, collisionPosition, 0);
				}
				else if (!moveCamSouth && moveCamWest)
				{
					suggestedPosition.y += 5;
					Point collisionPosition = new Point(suggestedPosition.x - this.characterSpriteWidth / 3, 
															suggestedPosition.y + (this.characterSpriteHeight / 2));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				else if (!moveCamSouth && !moveCamWest)
				{
					suggestedPosition.x -= 5;
					suggestedPosition.y += 5;
					Point collisionPosition = new Point(suggestedPosition.x - (this.characterSpriteWidth / 3), 
															suggestedPosition.y + (this.characterSpriteHeight / 2));
					tile = getTile(this.xCamera, this.yCamera, collisionPosition, 0);
				}
				
				if (tile != null)
				{
					if (tile.canWalkOnTile())
					{
						if (moveCamSouth)
						{
							this.yCamera += 5;
						}
						else
						{
							this.currentPosition.y += 5;
						}
						
						if (moveCamWest)
						{
							this.xCamera -= 5;
						}
						else
						{
							this.currentPosition.x -= 5;
						}
					}
				}
			}
			
			this.isMoving = true;
			return true;
		}
		else if (eventAction == MotionEvent.ACTION_UP)
		{
			this.isMoving = false;
			return true;
		}
		
		return false;
	}
	
	MapTile getTile(int cameraX, int cameraY, Point position, int layer)
	{
		int xStart = cameraX >> this.currentMap.bitshift;
		int yStart = cameraY >> this.currentMap.bitshift;
		int xEnd = xStart + (this.screenDims.x / this.currentMap.tileWidth);
		int yEnd = yStart + (this.screenDims.y / this.currentMap.tileWidth);

		if (xEnd >= GameMap.MAX_WIDTH - 1) {
			xEnd = GameMap.MAX_WIDTH - 1;
		}
		
		if (yEnd >= GameMap.MAX_HEIGHT - 1) {
			yEnd = GameMap.MAX_HEIGHT - 1;
		}

		// We actually need to use floating point values of this because if we just truncate, we can (and do) end up off by one.  Then we do rounding to get the answer we want.
		int absoluteTileX = (int)((float)position.x / (float)this.currentMap.tileWidth + .5f);
		int absoluteTileY = (int)((float)position.y / (float)this.currentMap.tileHeight + .5f);

		int x = absoluteTileX + xStart;
		int y = absoluteTileY + yStart;
		
		// Set these here so we have them.
		// Note: Java can suck it.
		this.xArrayCoord = x;
		this.yArrayCoord = y;

		// If we're off the map, we'll return null.
		if ((x < 0 || y < 0) || (x >= GameMap.MAX_WIDTH || y >= GameMap.MAX_HEIGHT))
		{
			return null;
		}
		
		return this.currentMap.getTile(x, y, layer);
	}
	
	/*
	 * Returns the sprite direction that should be drawn for the player sprite.
	 * 
	 * @param float x - x coordinate of the touch event
	 * @param float y - y coordinate of the touch even
	 * 
	 * @returns 0 = South, 1 = West, 2 = North, 3 = East, 4 = North West, 5 = North East, 6 = South East, 7 = South West
	 */
	private int getDirection(float x, float y)
	{
		int direction = 0;
		
		double angleSlice = Math.PI / 4;
		double pathX = x - this.currentPosition.x;
		double pathY = y - this.currentPosition.y;
		double distance = Math.sqrt(pathX * pathX + pathY * pathY);
		double touchAngle = Math.acos((x - this.currentPosition.x) / distance);
			
		if (y < this.centerPosition.y + 50) // Add a fudge factor so we can get the east direction correct.
		{
			touchAngle = 2 * Math.PI - touchAngle;
		}
		
		/*double angle = Math.PI / 4;
		double curAngle = angle / 2;
		
		for (int i = 0; i <= 7; ++i)
		{
			this.directionAnglesCenter[i] = curAngle;
			//double xPoint = this.currentPosition.x + radius * (Math.cos(curAngle));
			//double yPoint = this.currentPosition.y + radius * (Math.sin(curAngle));
					
			curAngle += angle;*/
			
			
		
		for (int i = 0; i <= 7; ++i) 
		{
			double angleDiff = 0;
			
			//if (this.currentPosition.x = this.centerPosition.x && this.currentPosition.y == this.centerPosition.y)
			//{
				angleDiff = Math.abs(touchAngle - this.directionAnglesCenter[i]);
			//}
			//else
			//{
			//	angleDiff = Math.abs(touchAngle - curAngle;
			//}
			
			if (angleDiff <= angleSlice)
			{
				return this.spriteDirectionMappings[i];
			}
		}
		
		return direction;
	}
}
