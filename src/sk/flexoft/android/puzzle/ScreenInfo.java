package sk.flexoft.android.puzzle;

import sk.flexoft.android.puzzle.util.AndroidExtensions;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class ScreenInfo {
	
	int screenWidth;
	
	int screenHeight;
	
	Rect targetRect;
	
	Bitmap[][] bmpParts;
	
	Rect[][] targetParts; 
	
	public ScreenInfo(Bitmap bitmap, int scrWidth, int scrHeight) throws IllegalArgumentException 
	{
		if (bitmap == null)
		{
			throw new IllegalArgumentException();
		}
		
		screenWidth = scrWidth;
		screenHeight = scrHeight;
		
		int bmpWidth, bmpHeight;
		bmpWidth = bitmap.getWidth();
		bmpHeight = bitmap.getHeight();
		
		if (screenWidth == bmpWidth && screenHeight == bmpHeight)
		{
			targetRect = new Rect(0, 0, bmpWidth, bmpHeight);
		}
		else if (screenWidth >= bmpWidth && screenHeight >= bmpHeight)
		{
			Enlarge(bmpWidth, bmpHeight);
		}
		else
		{
			ScaleDown(bmpWidth, bmpHeight);
		}
		
		if (targetRect.bottom > scrHeight - 1)
		{
			targetRect.bottom = scrHeight - 1;
		}
		
		if (targetRect.right > scrWidth - 1)
		{
			targetRect.right = scrWidth - 1;
		}
		
		InitParts(bitmap);
	}
	
	/**
	 * Gets the rectangle index from point.
	 *
	 * @param x the x
	 * @param y the y
	 * @return The rectangle index from point if the point is inside; otherwise -1
	 */
	public int getRectIdxFromPoint(int x, int y) {

		
		if (targetRect.contains(x, y))
		{
			for(int i = 0; i < PuzzleActivity.RASTER_SIZE; i++)
			{
				for(int j = 0; j < PuzzleActivity.RASTER_SIZE; j++)
				{
					if (targetParts[i][j].contains(x, y))
					{
						return PuzzleActivity.indexes2Int(i, j);
					}
				}
			}
		}
		
		return -1;
	}

	/**
	 * Initializes raster bitmaps and targets.
	 *
	 * @param bitmap The source bitmap.
	 */
	private void InitParts(Bitmap bitmap) {
		assert bitmap != null;
		
		bmpParts = new Bitmap[PuzzleActivity.RASTER_SIZE][PuzzleActivity.RASTER_SIZE];
		targetParts = new Rect[PuzzleActivity.RASTER_SIZE][PuzzleActivity.RASTER_SIZE];
		
		int bmpHorStep = bitmap.getWidth() / PuzzleActivity.RASTER_SIZE;
		int bmpVertStep = bitmap.getHeight() / PuzzleActivity.RASTER_SIZE;
		
		int partWidth = targetRect.width() / PuzzleActivity.RASTER_SIZE;
		int partHeight = targetRect.height() / PuzzleActivity.RASTER_SIZE;
		
		for (int i = 0; i < PuzzleActivity.RASTER_SIZE; i++)
		{
			for(int j = 0; j < PuzzleActivity.RASTER_SIZE; j++)
			{
				bmpParts[i][j] = AndroidExtensions.copyBitmapPart(
						bitmap, 
						new Rect(i * bmpHorStep, j * bmpVertStep, ((i + 1) * bmpHorStep) -1, ((j + 1) * bmpVertStep) -1),
						new Rect(0, 0, partWidth, partHeight));
				
				targetParts[i][j] = new Rect(
						targetRect.left + i * partWidth, 
						targetRect.top + j * partHeight, 
						targetRect.left + (i + 1) * partWidth - 1,
						targetRect.top + (j + 1) * partHeight - 1);
			}
		}
	}

	private void ScaleDown(int bmpWidth, int bmpHeight) {
		float widthRatio = (float)bmpWidth /(float)screenWidth;
		float ratio = (float)bmpHeight / (float)screenHeight;
		
		if (ratio < widthRatio)
		{
			ratio = widthRatio;
		}
		
		targetRect = new Rect(0, 0, (int)(bmpWidth / ratio), (int)(bmpHeight / ratio));
		
		if (ratio == widthRatio)
		{
			targetRect.offset(0,  (screenHeight - targetRect.height())/2);
		}
		else
		{
			targetRect.offset((screenWidth - targetRect.width())/2, 0);
		}
	}

	/**
	 * Enlarge bitmap which is smaller than screen to fit the screen
	 *
	 * @param bmpWidth the bitmap width
	 * @param bmpHeight the bitmap height
	 */
	private void Enlarge(int bmpWidth, int bmpHeight) {
		float widthRatio = (float)screenWidth / (float)bmpWidth;
		float ratio = (float)screenHeight / (float)bmpHeight;
		
		if (ratio > widthRatio)
		{
			ratio = widthRatio;
		}
		
		targetRect = new Rect(0, 0, (int)(bmpWidth * ratio), (int)(bmpHeight * ratio));
		
		if (ratio == widthRatio)
		{
			targetRect.offset(0,  (screenHeight - targetRect.height())/2);
		}
		else
		{
			targetRect.offset((screenWidth - targetRect.width())/2, 0);
		}
	}
}
