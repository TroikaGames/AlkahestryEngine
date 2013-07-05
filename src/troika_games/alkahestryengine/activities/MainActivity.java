package troika_games.alkahestryengine.activities;

import troika_games.alkahestryengine.game_engine.GameMaster;
import troika_games.alkahestryengine.graphics.AlkahestrySurface;
import android.app.Activity;
//import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Display display = getWindowManager().getDefaultDisplay();
        //Point size = new Point();
        //display.getSize(size);
        
        int width = display.getWidth();
        int height = display.getHeight();
      
        // set our MainGamePanel as the View
        setContentView(new AlkahestrySurface(this, new GameMaster(width, height, width / 2, height / 2), width, height, true));
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
}
