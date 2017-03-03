/**
 * Concrete strategy implementation for the Langton's Ant game.
 * 
 * Displays ant location as icon oriented according the actual move direction.
 * 
 *  @author Daniel
 */
package edu.mqu.assign2.gol;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AntAgent implements Agent {

	private CellularAutomaton 	mEnvironment;
	private int 				mAntXCoordinate;
	private int 				mAntYCoordinate;
	private enum				Direction {NORTH,EAST,SOUTH,WEST};
	private int					mDirectionIdx = 0;
	
	private int 				mGameBoardWidth;
	private int 				mGameBoardHeight;
	
	// Organize directions into array that allows easy determination of direction
	// based on need to turn left/right. Right turn means increasing array index, wrapping when reaching end,
	// left turn means decreasing index.
	private static final Direction[] mDirections = new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	// Array of icons with orientation corrersponding the direction.
	private static BufferedImage[]   mAntIcons;

	static {
		mAntIcons = new BufferedImage[4];
		mAntIcons[0] = readImage("ant_north.jpg");
		mAntIcons[1] = readImage("ant_east.jpg");
		mAntIcons[2] = readImage("ant_south.jpg");
		mAntIcons[3] = readImage("ant_west.jpg");
	}
	public AntAgent() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Set the context the Agents operates with.
	 */
	@Override
	public void setEnvironment(CellularAutomaton pEnvironment) {
		mEnvironment = pEnvironment;
		mGameBoardWidth  = pEnvironment.getGameBoard().length;
		mGameBoardHeight = pEnvironment.getGameBoard()[0].length;
		// places Ant to the middle of the board.
		mAntXCoordinate = mGameBoardWidth / 2;
		mAntYCoordinate = mGameBoardHeight / 2;
	}
	/**
	 * Calculate the state of the cell identified by coordinates in the next generation.
	 */
	@Override
	public int step(int pXCoordinate, int pYCoordinate) {
		int vCellValue = mEnvironment.getGameBoard()[pXCoordinate][pYCoordinate];
		// Ant movement logic, apply when reached cell with ant.
		if (pXCoordinate == mAntXCoordinate && pYCoordinate == mAntYCoordinate) {
			// cell has ant on it
			if (vCellValue == GameOfLife.ALIVE) { // black cell, turn right
				mDirectionIdx = (mDirectionIdx + 1) % mDirections.length;
			} else { // white cell, turn left 
				mDirectionIdx = (mDirectionIdx + mDirections.length - 1) % mDirections.length;
			}
			vCellValue = vCellValue == GameOfLife.ALIVE ? GameOfLife.DEAD : GameOfLife.ALIVE;
		}
		// move ant when evaluation has reached last cell on the board, to ensure that
		// there's no double ant move per one step if the move will be in forward direction to
		// cell which next state has not yet been calculated.
		if (pXCoordinate == mGameBoardWidth-1 && pYCoordinate == mGameBoardHeight-1) {
			switch (mDirections[mDirectionIdx]) {
				case NORTH:
					mAntYCoordinate = (mAntYCoordinate + mGameBoardHeight - 1) % mGameBoardHeight;
					break;
				case EAST:
					mAntXCoordinate = (mAntXCoordinate + 1) % mGameBoardWidth;
					break;
				case SOUTH:
					mAntYCoordinate = (mAntYCoordinate + 1) % mGameBoardHeight;
					break;
				case WEST:
					mAntXCoordinate = (mAntXCoordinate + mGameBoardWidth - 1) % mGameBoardWidth;
					break;
				default:
			}
		}
		return vCellValue;
	}

	/**
	 * Paint the current cell, either as square or as ant icon
	 */
	@Override
	public void paint(Graphics pGraphics, int pXCoordinate, int pYCoordinate) {
		int vCellWidth  = mEnvironment.getCellWidth();
		int vCellHeight = mEnvironment.getCellHeight();
		int vOffset     = mEnvironment.getOffset();
		if (pXCoordinate == mAntXCoordinate && pYCoordinate == mAntYCoordinate) {
			// Paint Ant icon, scale it to cell size.
			BufferedImage vAntIcon = mAntIcons[mDirectionIdx];
			Graphics2D vGraphics2D = (Graphics2D)pGraphics; 
			int vIconWidth  = vAntIcon.getWidth();
			int vIconHeight = vAntIcon.getHeight();
			int vAntX = pXCoordinate*vCellWidth+vOffset;
			int vAntY = pYCoordinate*vCellHeight+vOffset;
			vGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias
										 RenderingHints.VALUE_ANTIALIAS_ON);
			vGraphics2D.drawImage(vAntIcon, vAntX, vAntY, vAntX+vCellWidth, vAntY+vCellHeight, 0, 0, vIconWidth, vIconHeight, null);
		} else {
			// paint black/white square depending on cell state.
			pGraphics.setColor(mEnvironment.getGameBoard()[pXCoordinate][pYCoordinate] == GameOfLife.ALIVE ? Color.BLACK : Color.WHITE);
			pGraphics.fillRect(pXCoordinate*vCellWidth+vOffset,
					   pYCoordinate*vCellHeight+vOffset,
					   vCellWidth,vCellHeight);		
		}
	}
	
	/**
	 * Loads an image from a file.
	 * 
	 * @param fileLocation  image file location
	 * @return  BufferedImage ready to be used by Graphic 
	 */
	public static BufferedImage readImage(String fileLocation) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
