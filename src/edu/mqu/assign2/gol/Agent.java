/**
 * A Strategy pattern.
 * 
 * Interface Agent defines the agent behavior that is used by CellularAtomaton 
 * to determine next generation of each cell, and how to display the cell on screen. 
 * 
 * @author Daniel
 * 
 */
package edu.mqu.assign2.gol;

import java.awt.Graphics;

public interface Agent {

	/**
	 * Context in which the agent executes.
	 * 
	 * @param pEnvironment
	 */
	public void setEnvironment(CellularAutomaton pEnvironment);
	
	/**
	 * Calculate based on algorithm the next generation value for given cell.
	 * 
	 * @param pXCoordinate
	 * @param pYCoordinate
	 * @return
	 */
	public int step(int pXCoordinate, int pYCoordinate);

	/**
	 * Display the given cell based on it's status.
	 * @param pGraphics
	 * @param pXCoordinate
	 * @param pYCoordinate
	 */
	public void paint(Graphics pGraphics, int pXCoordinate, int pYCoordinate);
}
