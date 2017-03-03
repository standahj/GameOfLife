package edu.mqu.assign2.gol;

import java.awt.Graphics;

@SuppressWarnings("serial")
public class CellularAutomaton extends Animator implements Animatable {

	public static final int BOARD_WIDTH 	= 200;
	public static final int BOARD_HEIGHT 	= 200;
	public static final int CELL_SIZE 		= 4;
	
	private int[][] mGameBoard 		= null;
	private int[][] mInitialPattern = null;
	private int     mGameWidth 		= BOARD_WIDTH;
	private int     mGameHeight		= BOARD_HEIGHT;
	private int		mCellWidth		= CELL_SIZE;
	private int		mCellHeight		= CELL_SIZE;
	private int 	mOffset 		= 10;
	
	private Agent 	mAgent			= null;
	
	public CellularAutomaton(float pAliveThreshold) {
		super();
		mGameBoard = new int[mGameWidth][mGameHeight];
		// initialize (as per instructions in assignment) to a default state
		initialise(pAliveThreshold);
	}

	public CellularAutomaton(int pGameWidth, int pGameHeight, float pAliveThreshold, int[][] pInitialPattern) {
		mGameWidth  	= pGameWidth;
		mGameHeight 	= pGameHeight;
		mInitialPattern = pInitialPattern;
		mGameBoard  	= new int[mGameWidth][mGameHeight];
		mCellWidth  	= BOARD_WIDTH / mGameWidth * CELL_SIZE;
		mCellHeight 	= BOARD_HEIGHT / mGameHeight * CELL_SIZE;
		// initialize (as per instructions in assignment) to a default state
		initialise(pAliveThreshold);
	}

	/**
	 * Initializes the initial state of the automaton
	 * 
	 */
	public synchronized void initialise(float pAliveThreshold) {
		if (mInitialPattern == null) {
			// defaults to random population
			for (int x = 0; x < mGameWidth; x++) {
				for (int y = 0; y < mGameHeight; y++) {
					mGameBoard[x][y] = Math.random() > pAliveThreshold ? GameOfLife.ALIVE :GameOfLife.DEAD; 
				}
			}
		} else {
			// use initialization pattern at offsetX, offsetY from top-left corner.
			int vPatternXDim = mInitialPattern.length;
			int vPatternYDim = mInitialPattern[0].length;
			int vPatternOffsetX = (mGameWidth-mInitialPattern.length)/2;
			int vPatternOffsetY = (mGameHeight-mInitialPattern[0].length)/2;
			for (int x = 0; x < mGameWidth; x++) {
				for (int y = 0; y < mGameHeight; y++) {
					mGameBoard[x][y] = x >= vPatternOffsetX &&
									  x < vPatternOffsetX+vPatternXDim &&
									  y >= vPatternOffsetY &&
									  y < vPatternOffsetY+vPatternYDim ? 
									mInitialPattern[x-vPatternOffsetX][y-vPatternOffsetY] :GameOfLife.DEAD; 
				}
			}
		}
	}

	/**
	 * Calculates the next generation state based on current generation game state.
	 * Refer to the Game of Life rules for details.
	 */
	@Override
	public synchronized void step() {
		int[][] vNextGenBoard = new int[mGameWidth][mGameHeight]; // stores next generation state 
		for (int x = 0; x < mGameWidth; x++) {
			for (int y = 0; y < mGameHeight; y++) {
				vNextGenBoard[x][y] = mAgent.step(x, y);
			}
		}
		mGameBoard = vNextGenBoard;
	}

	@Override
	public synchronized void paint(Graphics pGraphics) {
		if (mAgent != null) {
			for (int x = 0; x < mGameWidth; x++) {
				for (int y = 0; y < mGameHeight; y++) {
					mAgent.paint(pGraphics, x, y);
				}
			}
		}
	}

	@Override
	public int getWidth() {
		return mGameWidth*mCellWidth+2*mOffset;
	}

	@Override
	public int getHeight() {
		return mGameHeight*mCellHeight+6*mOffset;
	}

	/**
	 * @return the Cell Width
	 */
	public int getCellWidth() {
		return mCellWidth;
	}

	/**
	 * @return the Cell Height
	 */
	public int getCellHeight() {
		return mCellHeight;
	}

	/**
	 * Required for JUnit to access the game board.
	 * @return
	 */
	public int[][] getGameBoard() {
		return mGameBoard;
	}
	/**
	 * Required for JUnit to access the initialization data.
	 * @return
	 */
	public int[][] getInitialPattern() {
		return mInitialPattern;
	}
	
	/**
	 * If initial pattern is set, the game board will get initialized to this pattern.
	 * 
	 * @param pInitialPattern
	 */
	public void setInitialPattern(int[][] pInitialPattern) {
		mInitialPattern = pInitialPattern;
	}
	
	/**
	 * offset defines the space between the screen edge and the game board edge, in pixels
	 * @return offset size in pixels
	 */
	public int getOffset() {
		return mOffset;
	}

	/**
	 * @return the mAgent
	 */
	public Agent getAgent() {
		return mAgent;
	}

	/**
	 * @param pAgent the concrete Agent to use with this automaton
	 */
	public void setAgent(Agent pAgent) {
		this.mAgent = pAgent;
	}
}