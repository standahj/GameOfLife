/**
 * Assignment 2, part 1 - Game of Life implementation based on instructions
 * and using bouncing_head sample implementation as base.
 * 
 * @author Daniel 
 *  
 */
package edu.mqu.assign2.gol;

import java.awt.Color;
import java.awt.Graphics;

/**
 * <class>GameOfLifeAgent</class> class implements basic version of Game of Life.
 * 
 * Concrete strategy implementation for the Game Of Life.
 * 
 * Note: For the geometry management / initialization pattern design, the coordinate
 * system is organized with point [0,0] is left-top corner, [N,N] is bottom-right corner,
 * x counts fields on horizontal level, y count fields on vertical axis.
 * 
 * @author Daniel
 *
 */
public class GameOfLifeAgent implements Agent {
	
	private CellularAutomaton mEnvironment;
	
	/**
	 * Default constructor
	 */
	public GameOfLifeAgent() {
	}

	public void setEnvironment(CellularAutomaton pEnvironment) {
		mEnvironment = pEnvironment;
	}
	
	/**
	 * Calculate the state of the cell identified by coordinates in the next generation.
	 */
	public int step(int pXCoordinate, int pYCoordinate) {
		int vLiveNeigbors = getLiveNeighborCellCount(pXCoordinate, pYCoordinate);
		// next gen cell is GameOfLife.ALIVE if it has 3 GameOfLife.ALIVE neighbors this gen, or has 2 neighbors and is GameOfLife.ALIVE this gen
		return vLiveNeigbors == 3 || 
			  (vLiveNeigbors == 2 && mEnvironment.getGameBoard()[pXCoordinate][pYCoordinate] == GameOfLife.ALIVE) ? GameOfLife.ALIVE : GameOfLife.DEAD;
	}
	/**
	 * Paint the current cell identified by coordinates.
	 */
	public void paint(Graphics pGraphics, int pXCoordinate, int pYCoordinate) {
		pGraphics.setColor(mEnvironment.getGameBoard()[pXCoordinate][pYCoordinate] == GameOfLife.ALIVE ? Color.BLUE : Color.WHITE);
		int vCellWidth  = mEnvironment.getCellWidth();
		int vCellHeight = mEnvironment.getCellHeight();
		int vOffset     = mEnvironment.getOffset();
		pGraphics.fillRect(pXCoordinate*vCellWidth+vOffset,
						   pYCoordinate*vCellHeight+vOffset,
						   vCellWidth,vCellHeight);		
	}

	/**
	 * Counts the Live cells that neighbor with the cell identified by coordinates.
	 *  
	 * @param pXCoordinate
	 * @param pYCoordinate
	 * @return  live cell neighbor count
	 */
	private int getLiveNeighborCellCount(int pXCoordinate, int pYCoordinate) {
		int vCount = 0;
		// need to check up to 8 neighbor cells, simplify the neighbor selection by nested loops.
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				// Cells outside board are considered GameOfLife.DEAD, thus do not attempt to test those,
				// it will trigger exception IndexOutOfBounds 
				if (pXCoordinate+x >= 0 && pXCoordinate+x < mEnvironment.getGameBoard().length &&
					pYCoordinate+y >= 0 && pYCoordinate+y < mEnvironment.getGameBoard()[0].length) {
					// Small detail - do not count the cell itself, just neighbors
					if (x != 0 || y != 0) {
						vCount += mEnvironment.getGameBoard()[pXCoordinate+x][pYCoordinate+y];
					}
				}
			}
		}
		return Math.abs(vCount); // LIVE value is negative (-1) thus the count will be negative too,
								 // which is counter-intuitive, so let's convert it to positive number.
	}


}
