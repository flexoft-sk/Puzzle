package sk.flexoft.android.puzzle;

import sk.flexoft.android.puzzle.R.id;
import sk.flexoft.android.puzzle.util.AndroidExtensions;
import sk.flexoft.android.puzzle.util.SystemUiHider;
import sk.flexoft.android.puzzle.util.AndroidExtensions.LogType;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class PuzzleActivity extends Activity {

	/** The Constant TAG. */
	private static final String TAG = "PuzzleActivity";
	
	/** Whether or not the system UI should be auto-hidden after. {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds. */
	//private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	//private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	//private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	//private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	//private SystemUiHider mSystemUiHider;

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
		// setContentView(R.layout.activity_puzzle);

		/*
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);*/
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 *//*
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}*/

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 *//*
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};*/

	/** The m hide handler. *//*
	Handler mHideHandler = new Handler();*/
	
	/** The m hide runnable. *//*
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};*/

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 *
	 * @param delayMillis the delay millis
	 *//*
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}*/
}
