package troika_games.alkahestryengine.graphics;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Sprite
{
	private long spriteTime = -1;
	private Bitmap texture = null;
	private ArrayList<Integer> animationIndex = null;
	private ArrayList<Integer> animationSpeeds = null;
	private ArrayList<ArrayList<Rect>> frames = null;
	private ArrayList<Integer> rectangleCounts = null;
	private int height = -1;
	private int width = -1;
	
	public Sprite()
	{
		this.texture = null;
		this.spriteTime = 0;
		
		this.animationIndex = new ArrayList<Integer>(); 
		this.animationSpeeds = new ArrayList<Integer>();
		this.frames = new ArrayList<ArrayList<Rect>>();
		this.rectangleCounts = new ArrayList<Integer>();
	}
	
	public Sprite(Bitmap texture)
	{
		this.texture = Bitmap.createBitmap(texture);
		this.spriteTime = 0;
		
		this.animationIndex = new ArrayList<Integer>(); 
		this.animationSpeeds = new ArrayList<Integer>();
		this.frames = new ArrayList<ArrayList<Rect>>();
		this.rectangleCounts = new ArrayList<Integer>();
		
		this.height = this.texture.getHeight();
		this.width = this.texture.getWidth();
	}
	
	public void setTexture(Bitmap texture)
	{
		this.texture = Bitmap.createBitmap(texture);
		this.height = this.texture.getHeight();
		this.width = this.texture.getWidth();
	}
	
	public void addRectangle(Rect rectangle, int spriteIndex)
	{
		while (spriteIndex >= this.frames.size())
		{
			this.frames.add(new ArrayList<Rect>());
			this.animationIndex.add(0);
			this.rectangleCounts.add(0);
		}
		
		this.frames.get(spriteIndex).add(rectangle);
		
		this.rectangleCounts.set(spriteIndex, this.frames.get(spriteIndex).size());
		
		this.animationIndex.add(0);
	}
	
	public void setAnimationSpeed(int speed, int spriteIndex)
	{
		while (spriteIndex >= this.animationSpeeds.size())
		{
			this.animationSpeeds.add(0);
		}

		this.animationSpeeds.set(spriteIndex, speed);
	}
	
	public void update(long currentTime, int spriteIndex)
	{
		if (this.spriteTime + this.animationSpeeds.get(spriteIndex) <= currentTime)
		{
			this.animationIndex.set(spriteIndex, this.animationIndex.get(spriteIndex) + 1);
			
			if (this.animationIndex.get(spriteIndex) > this.rectangleCounts.get(spriteIndex) - 1)
			{
				this.animationIndex.set(spriteIndex, 0);
			}

			this.spriteTime = currentTime;
		}
	}
	
	public void draw(Canvas canvas, int spriteIndex, Rect locationToDraw)
	{
		Rect sourceRect = this.frames.get(spriteIndex).get(this.animationIndex.get(spriteIndex));
		canvas.drawBitmap(this.texture, sourceRect, locationToDraw, null);
	}
	
	public void animate(Canvas canvas, Rect locationToDraw, long currentTime, int spriteIndex)
	{
		Rect sourceRect = this.frames.get(spriteIndex).get(this.animationIndex.get(spriteIndex));
		canvas.drawBitmap(this.texture, sourceRect, locationToDraw, null);
		
		if (this.spriteTime + this.animationSpeeds.get(spriteIndex) <= currentTime)
		{
			this.animationIndex.set(spriteIndex, this.animationIndex.get(spriteIndex) + 1);
			
			if (this.animationIndex.get(spriteIndex) > this.rectangleCounts.get(spriteIndex) - 1)
			{
				this.animationIndex.set(spriteIndex, 0);
			}

			this.spriteTime = currentTime;
		}
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getFrameHeight(int spriteIndex, int frame)
	{
		return this.frames.get(spriteIndex).get(frame).height();
	}
	
	public int getFrameWidth(int spriteIndex, int frame)
	{
		return this.frames.get(spriteIndex).get(frame).width();
	}
}
