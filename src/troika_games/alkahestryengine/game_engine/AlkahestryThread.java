package troika_games.alkahestryengine.game_engine;

import java.text.DecimalFormat;

import troika_games.alkahestryengine.graphics.AlkahestrySurface;
import android.graphics.Canvas;
import android.view.SurfaceHolder;


public class AlkahestryThread extends Thread
{
	private final static int MAX_FPS = 50;
	private final static int MAX_FRAME_SKIPS = 5;
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;
	
	// we'll be reading the stats every second
	private final static int STAT_INTERVAL = 1000; //ms
	
	// the average will be calculated by storing 
	// the last n FPSs
	private final static int FPS_HISTORY_NR = 10;
	
	private DecimalFormat df = new DecimalFormat("0.##");
	
	
	// the status time counter
	private long statusIntervalTimer = 0l;
	
	// number of frames skipped since the game started
	private long totalFramesSkipped = 0l;
	
	// number of frames skipped in a store cycle (1 sec)
	private long framesSkippedPerStatCycle = 0l;

	// number of rendered frames in an interval
	private int frameCountPerStatCycle = 0;
	private long totalFrameCount = 0l;
	
	// the last FPS values
	private double fpsStore[];
	
	// the number of times the stat has been read
	private long statsCount = 0;
	
	// the average FPS since the game started
	private double averageFPS = 0.0;
	
	boolean isRunning;
	
	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	
	// The actual view that handles inputs
	// and draws to the surface
	private AlkahestrySurface gameSurface;
		
	public AlkahestryThread(SurfaceHolder surfaceHolder, AlkahestrySurface surface)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.gameSurface = surface;
	}
	
	public void setIsRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
	
	@Override
	public void run()
	{
		Canvas canvas;
		
		// Initialize timing elements for stat gathering
		initTimingElements();
		
		long beginTime;		// the time when the cycle begun
		long timeDiff;		// the time it took for the cycle to execute
		int sleepTime;		// ms to sleep (<0 if we're behind)
		int framesSkipped;	// number of frames being skipped 

		sleepTime = 0;
		
		while (this.isRunning)
		{
			canvas = null;
			
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try
			{
				canvas = this.surfaceHolder.lockCanvas();
				
				synchronized (this.surfaceHolder)
				{
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;	// resetting the frames skipped
					
					// update game state 
					//this.gamePanel.update();
					
					// render state to the screen
					// draws the canvas on the panel
					this.gameSurface.render(canvas);
					
					// calculate how long did the cycle take
					timeDiff = System.currentTimeMillis() - beginTime;
					
					// calculate sleep time
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					
					if (sleepTime > 0)
					{
						// if sleepTime > 0 we're OK
						try
						{
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(sleepTime);	
						}
						catch (InterruptedException e)
						{
						}
					}
					
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS)
					{
						// we need to catch up
						this.gameSurface.update(); // update without rendering
						sleepTime += FRAME_PERIOD;	// add frame period to check if in next frame
						++framesSkipped;
					}
					
					// for statistics
					this.framesSkippedPerStatCycle += framesSkipped;
					
					// calling the routine to store the gathered statistics
					storeStats();
				}
			}
			finally
			{	
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null)
				{
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	
	private void storeStats()
	{
		++this.frameCountPerStatCycle;
		++this.totalFrameCount;
		
		this.statusIntervalTimer += FRAME_PERIOD;
		
		if (this.statusIntervalTimer >= STAT_INTERVAL)
		{
			// calculate the actual frames per status check interval
			double actualFPS = (double)(this.frameCountPerStatCycle / (STAT_INTERVAL / 1000));
			
			//stores the latest FPS in the array
			this.fpsStore[(int)this.statsCount % FPS_HISTORY_NR] = actualFPS;
			
			// increase the number of times statistics was calculated
			++this.statsCount;
			
			double totalFPS = 0.0;
			
			// sum up the stored FPS values
			for (int i = 0; i < FPS_HISTORY_NR; ++i)
			{
				totalFPS += this.fpsStore[i];
			}
			
			// obtain the average
			if (statsCount < FPS_HISTORY_NR)
			{
				this.averageFPS = totalFPS / this.statsCount;
			}
			else
			{
				this.averageFPS = totalFPS / FPS_HISTORY_NR;
			}
			
			// saving the number of total frames skipped
			this.totalFramesSkipped += this.framesSkippedPerStatCycle;
			
			// resetting the counters after a status record (1 sec)
			this.framesSkippedPerStatCycle = 0;
			this.statusIntervalTimer = 0;
			this.frameCountPerStatCycle = 0;
			
			//Log.d(TAG, "Average FPS:" + df.format(averageFps));
			gameSurface.averageFPS = this.df.format(this.averageFPS);
		}
	}
	
	private void initTimingElements()
	{
		// Initialize timing elements
		this.fpsStore = new double[FPS_HISTORY_NR];
		
		for (int i = 0; i < FPS_HISTORY_NR; ++i)
		{
			this.fpsStore[i] = 0.0;
		}
		
		//Log.d(TAG + ".initTimingElements()", "Timing elements for stats initialized");
	}
}
