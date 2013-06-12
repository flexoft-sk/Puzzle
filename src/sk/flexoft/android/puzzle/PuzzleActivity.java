package sk.flexoft.android.puzzle;

import sk.flexoft.android.puzzle.util.AndroidExtensions;
import sk.flexoft.android.puzzle.util.AndroidExtensions.LogType;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * @author Vladimir Iszer
 * The main application activity
 *
 */
public class PuzzleActivity extends Activity {

	/** The Constant TAG. */
	private static final String TAG = "PuzzleActivity";
	
	/** The main view. */
	private PuzzleView view;
	
	/**
	 * Converts board indexes to an integer.
	 *
	 * @param x The x index.
	 * @param y The y index.
	 * @param rasterSize The size of the raster.
	 * @return The packed value of indexes.
	 */
	public static int indexes2Int(int x, int y, int rasterSize)
	{
		assert x < rasterSize && x >= 0;
		assert y < rasterSize && y >= 0;
		
		return x * rasterSize + y;
	}
	
	/**
	 * Converts an integer to board indexes.
	 *
	 * @param i The packed indexes value.
	 * @param rasterSize The size of the raster.
	 * @return The indexes.
	 */
	public static int[] int2Indexes(int i, int rasterSize)
	{
		assert i >= 0 && i < rasterSize * rasterSize;
		int[] result = new int[2];
		result[0] = i / rasterSize;
		result[1] = i % rasterSize;
		
		return result;
	}
	
	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public PuzzleBoard getBoard()
	{
		return PuzzleBoard.getBoard(this, getRasterSize());
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "Activity started.");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		view = new PuzzleView(this);
		
		getBoard().shuffle();
		setContentView(view);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (view != null)
		{
			view.pause();
		}
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (view != null)
		{
			view.resume();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.puzzle_menu, menu);
	    return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menu_new_game:
            getBoard().shuffle();
            return true;
        case R.id.menu_about:
        	showAboutBox();
            return true;
        case R.id.menu_level_easy:
        case R.id.menu_level_medium:
        case R.id.menu_level_hard:
        	if (item.isChecked()) {
        		item.setChecked(false);
        	}
            else {
            	item.setChecked(true);
            }
        	setLevel(item.getItemId());
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AndroidExtensions.Log(LogType.Debug, TAG, "KeyDown %d", keyCode);
		
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		AndroidExtensions.Log(LogType.Debug, TAG, "KeyDown %d", keyCode);
		
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onSearchRequested()
	 */
	@Override
	public boolean onSearchRequested() {
		return false;
	}
	
	/**
	 * @return Raster size based on selected level.
	 */
	public int getRasterSize()
	{
		if (PuzzleConfiguration.level == R.id.menu_level_easy)
		{
			return 3;
		}
		
		if (PuzzleConfiguration.level == R.id.menu_level_hard)
		{
			return 5;
		}
		
		// use default value for the rest
		return 4;
	}

	/**
	 * Sets the level.
	 *
	 * @param itemId the new level
	 */
	private void setLevel(int level) {
		if (PuzzleConfiguration.level != level)
		{
			PuzzleConfiguration.level = level;
			view.RefreshActiveScreenInfo();
			getBoard().shuffle();
		}
	}
	
	/**
	 * SHows the about dialog box.
	 */
	private void showAboutBox()
	{
		final Dialog aboutDlg = new Dialog(this);
		aboutDlg.setContentView(R.layout.about);
		aboutDlg.setTitle(R.string.menu_about);
		
		Button aboutDlgOkBtn = (Button)(aboutDlg.findViewById(R.id.btnAboutOK)); 
		aboutDlgOkBtn.setOnClickListener(new View.OnClickListener()
				 {

					/* (non-Javadoc)
					 * @see android.view.View.OnClickListener#onClick(android.view.View)
					 */
					@Override
					public void onClick(View v) {
						aboutDlg.dismiss();
					}
				 });
		
		aboutDlg.show();
	}
}
