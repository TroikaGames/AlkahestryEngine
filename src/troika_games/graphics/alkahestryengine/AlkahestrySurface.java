package troika_games.graphics.alkahestryengine;

import java.util.ArrayList;

import troika_games.graphics.alkahestryengine.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class AlkahestrySurface extends SurfaceView implements Callback
{
	private GameMaster gm = null;
	
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private Sprite playerSprite = null;
	private AlkahestryThread thread = null;
	private boolean isDebugMode = false;
	private Point[] directionPoints = new Point[8];
	private double[] directionAngles = new double[8];
	private int[] spriteDirectionMappings = new int[8];
	
	private int screenX = 0;
	private int screenY = 0;
	private int centerX = 0;
	private int centerY = 0;
	
	public String averageFPS = null;
	
	private void init(GameMaster gm, int x, int y, boolean isDebugMode) 
	{
		Resources resources = getResources();
		
		this.gm = gm;
		this.gm.loadMap("somemap.map", resources);
		this.screenX = x;
		this.screenY = y;
		
		this.centerX = this.screenX / 2;
		this.centerY = this.screenY / 2;
		
		this.averageFPS = new String();
		
		this.isDebugMode = isDebugMode;
		
		getHolder().addCallback(this);
		
		loadCharacterSprite(resources);
		
		float radius = this.centerY;
		double angle = Math.PI / 4;
		
		double curAngle = angle / 2;
		
		for (int i = 0; i <= 7; ++i)
		{
			this.directionAngles[i] = curAngle;
			double xPoint = this.centerX + radius * (Math.cos(curAngle));
			double yPoint = this.centerY + radius * (Math.sin(curAngle));
			
			this.directionPoints[i] = new Point();
			this.directionPoints[i].x = (int)xPoint;
			this.directionPoints[i].y = (int)yPoint;
			
			curAngle += angle;
		}
		
		this.thread = new AlkahestryThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}
	
	public AlkahestrySurface(Context context)
	{
		super(context);
		init(null, 0, 0, false);
	}
	
	public AlkahestrySurface(Context context, GameMaster gm, int x, int y, boolean isDebugMode)
	{
		super(context);
		init(gm, x, y, isDebugMode);
	}
	
	private void loadCharacterSprite(Resources resources)
	{
		this.playerSprite = new Sprite(BitmapFactory.decodeResource(resources, R.drawable.knight_m_character));
		
		// South
		this.playerSprite.addRectangle(new Rect(0, 0, 68, 93), 0);
		this.playerSprite.addRectangle(new Rect(72, 0, 136, 93), 0);
		this.playerSprite.addRectangle(new Rect(144, 0, 204, 93), 0);
		this.playerSprite.setAnimationSpeed(100, 0);
		
		// West
		this.playerSprite.addRectangle(new Rect(216, 0, 272, 93), 1);
		this.playerSprite.addRectangle(new Rect(288, 0, 340, 93), 1);
		this.playerSprite.addRectangle(new Rect(360, 0, 408, 93), 1);
		this.playerSprite.setAnimationSpeed(100, 1);
		
		// North
		this.playerSprite.addRectangle(new Rect(417, 0, 476, 93), 2);
		this.playerSprite.addRectangle(new Rect(488, 0, 544, 93), 2);
		this.playerSprite.addRectangle(new Rect(556, 0, 612, 93), 2);
		this.playerSprite.setAnimationSpeed(100, 2);
		
		// East
		this.playerSprite.addRectangle(new Rect(625, 0, 680, 93), 3);
		this.playerSprite.addRectangle(new Rect(694, 0, 748, 93), 3);
		this.playerSprite.addRectangle(new Rect(762, 0, 816, 93), 3);
		this.playerSprite.setAnimationSpeed(100, 3);
		
		// North West
		this.playerSprite.addRectangle(new Rect(834, 0, 894, 93), 4);
		this.playerSprite.addRectangle(new Rect(894, 0, 953, 93), 4);
		this.playerSprite.addRectangle(new Rect(1015, 0, 816, 93), 4);
		this.playerSprite.setAnimationSpeed(100, 4);
		
		// North East
		this.playerSprite.addRectangle(new Rect(1018, 0, 1083, 93), 5);
		this.playerSprite.addRectangle(new Rect(1083, 0, 1145, 93), 5);
		this.playerSprite.addRectangle(new Rect(1145, 0, 1215, 93), 5);
		this.playerSprite.setAnimationSpeed(100, 5);
		
		// South East
		this.playerSprite.addRectangle(new Rect(1216, 0, 1267, 93), 6);
		this.playerSprite.addRectangle(new Rect(1270, 0, 1333, 93), 6);
		this.playerSprite.addRectangle(new Rect(1340, 0, 1400, 93), 6);
		this.playerSprite.setAnimationSpeed(100, 6);
		
		// South West
		this.playerSprite.addRectangle(new Rect(1407, 0, 1474, 93), 7);
		this.playerSprite.addRectangle(new Rect(1474, 0, 1535, 93), 7);
		this.playerSprite.addRectangle(new Rect(1535, 0, 1608, 93), 7);
		this.playerSprite.setAnimationSpeed(100, 7);
		
		
		this.spriteDirectionMappings[0] = 6;
		this.spriteDirectionMappings[1] = 0;
		this.spriteDirectionMappings[2] = 7;
		this.spriteDirectionMappings[3] = 1;
		this.spriteDirectionMappings[4] = 4;
		this.spriteDirectionMappings[5] = 2;
		this.spriteDirectionMappings[6] = 5;
		this.spriteDirectionMappings[7] = 3;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		this.thread.setIsRunning(true);
		this.thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		boolean retry = true;
		
		while (retry)
		{
			try
			{
				this.thread.join();
				retry = false;
			}
			catch (InterruptedException e)
			{
				// try again shutting down the thread
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (this.gm.currentState == GameMaster.DO_WALK_ABOUT)
		{
			int eventAction = event.getAction();
			
			if (eventAction == MotionEvent.ACTION_DOWN || eventAction == MotionEvent.ACTION_MOVE)
			{
				this.gm.currentDirection = getDirection(event.getX(), event.getY());
				this.gm.isMoving = true;
			}
			else if (eventAction == MotionEvent.ACTION_UP)
			{
				this.gm.isMoving = false;
			}
		}
		
		return true;
	}
	
	public void render(Canvas canvas)
	{
		canvas.drawColor(Color.BLACK);
		
		if (this.gm.currentState == GameMaster.DO_WALK_ABOUT)
		{
			doWalkAround(canvas);
			
			/*Paint[] tempPaint = new Paint[8];
			
			tempPaint[0] = new Paint();
			tempPaint[0].setColor(Color.WHITE);
			
			tempPaint[1] = new Paint();
			tempPaint[1].setColor(Color.BLACK);
			
			tempPaint[2] = new Paint();
			tempPaint[2].setColor(Color.RED);
			
			tempPaint[3] = new Paint();
			tempPaint[3].setColor(Color.GREEN);
			
			tempPaint[4] = new Paint();
			tempPaint[4].setColor(Color.BLUE);
			
			tempPaint[5] = new Paint();
			tempPaint[5].setColor(Color.YELLOW);
			
			tempPaint[6] = new Paint();
			tempPaint[6].setColor(Color.CYAN);
			
			tempPaint[7] = new Paint();
			tempPaint[7].setColor(Color.MAGENTA);

			for (int i = 0; i <= 7; ++i)
			{
				canvas.drawLine((float)centerX, (float)centerY, (float)this.directionPoints[i].x, 
									(float)this.directionPoints[i].y, tempPaint[i]);
			}*/
		}
		
		if (this.isDebugMode)
		{
			displayDebugInfo(canvas);
		}
	}

	// Updates animations.  Only used when we are sleeping or otherwise not animating.
	public void update()
	{
		for (int i = 0; i < this.sprites.size(); ++i)
		{
			this.sprites.get(i).update(System.currentTimeMillis(), 0);
		}
	}
	
	private void doWalkAround(Canvas canvas)
	{
		drawMap(canvas);
				
		for (int i = 0; i < this.sprites.size(); ++i)
		{
			Sprite currentSprite = this.sprites.get(i);
			
			Rect drawRect = new Rect(0, 0, 128, 128);
			
			currentSprite.draw(canvas, 0, drawRect);			
		}
	}
	
	private void drawMap(Canvas canvas)
	{
		Point drawPoint = new Point(0, 0);
		int xStart = gm.xCamera >> 5;
		int yStart = gm.yCamera >> 5;
		int xEnd = xStart + (this.screenX / 32);
		int yEnd = yStart + (this.screenY / 32);
		
		int x = gm.xCamera & 32;
		int y = gm.yCamera & 32;

		if (x == 0)
		{
			// If xCamera is divisible by 32, use only 20 tiles across
			--xEnd;
		}
		else
		{
			// Otherwise move destination RECT to the left to clip the first column of tiles
			drawPoint.x -= x;
		}

		if (y == 0)
		{
			// If yCamera is divisible by 32, use only 15 tiles down
			--yEnd;
		}
		else
		{
			// Otherwise move destination RECT up to clip the first row of tiles
			drawPoint.y -= y;
		}
		
		// Finally make sure we're not exceeding map limits
		if (xEnd >= GameMap.MAX_WIDTH - 1) {
			xEnd = GameMap.MAX_WIDTH - 1;
		}
		
		if (yEnd >= GameMap.MAX_HEIGHT - 1) {
			yEnd = GameMap.MAX_HEIGHT - 1;
		}
		
		Point tempPoint = new Point(drawPoint.x, drawPoint.y);
		
		for (int layer = 0; layer < 3; ++layer)
		{
			drawPoint.x = tempPoint.x;
			drawPoint.y = tempPoint.y;

			// This layer has the player and all the objects.
			if (layer == 2)
			{
				drawPlayer(canvas);
			}
			
			for (x = xStart; x <= xEnd; ++x)
			{
				for (y = yStart; y <= yEnd; ++y)
				{
					MapTile tileToUse = gm.currentMap.getTile(x, y, layer);
					int tileNumber = tileToUse.getTileNumber();
					
					if (tileNumber != 0)
					{
						int animationSpeed = tileToUse.getAnimationSpeed();
						
						if (animationSpeed > 0)
						{
							// we'll animate here.
						}
					
						Rect destRect = new Rect(drawPoint.x, drawPoint.y, drawPoint.x + 32, drawPoint.y + 32);
						canvas.drawBitmap(gm.currentMap.getTileBitmap(tileNumber), tileToUse.getTileRectangle(), destRect, null);
						
						// TODO: Figure out the formula for hex tiles and move down the correct 
						//	amount based on that.
						// Also, the tile height should be used here, not a hard coded number.
						drawPoint.y += 32;
					}
				}
				
				// TODO: Figure out the formula for hex tiles and move over the correct 
				//	amount based on that.
				// Also, the tile height should be used here, not a hard coded number.
				drawPoint.x += 32;
				drawPoint.y = tempPoint.y;
			}
		}
	}
	
	private void drawPlayer(Canvas canvas)
	{
		//this.sprites.get(i).draw(canvas, 0, new Rect(0, 0, 128, 128));
		int width = this.playerSprite.getFrameWidth(this.gm.currentDirection, 0);
		int height = this.playerSprite.getFrameHeight(this.gm.currentDirection, 0);
		
		int startX = this.centerX - (width / 2);
		int startY = this.centerY - (height / 2);
		
		Rect playerDrawRect = new Rect(startX, startY, startX + width, startY + height);
		
		if (this.gm.isMoving)
		{
			this.playerSprite.animate(canvas, playerDrawRect, System.currentTimeMillis(), gm.currentDirection);
		}
		else
		{
			this.playerSprite.draw(canvas, gm.currentDirection, playerDrawRect);
		}
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
		double pathX = x - this.centerX;
		double pathY = y - this.centerY;
		double distance = Math.sqrt(pathX * pathX + pathY * pathY);
		double touchAngle = Math.acos((x - this.centerX) / distance);
			
		if (y < this.centerY + 50) // Add a fudge factor so we can get the east direction correct.
		{
			touchAngle = 2 * Math.PI - touchAngle;
		}
		
		for (int i = 0; i <= 7; ++i) 
		{
			double angleDiff = Math.abs(touchAngle - this.directionAngles[i]);
			
			if (angleDiff <= angleSlice)
			{
				return this.spriteDirectionMappings[i];
			}
		}
		
		return direction;
	}
	
	private void displayDebugInfo(Canvas canvas)
	{
		if (canvas != null)
		{
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			String debugText = "FPS " + this.averageFPS + " - direction: " + this.gm.currentDirection;
			canvas.drawText(debugText, this.getWidth() - 150, 20, paint);
		}
	}
}
