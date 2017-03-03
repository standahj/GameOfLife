package edu.mqu.assign2.gol.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.mqu.assign2.gol.CellularAutomaton;
import edu.mqu.assign2.gol.GameOfLifeAgent;
import edu.mqu.assign2.gol.GameOfLife;

public class CellularAutomatonTest {

	private static final float GOL_THRESHOLD = 0.5f;
	private static final float ANT_THRESHOLD = 2.0f;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCellularAutomaton() {
		CellularAutomaton vCA = new CellularAutomaton(GOL_THRESHOLD);
		assertEquals("board width must be equal to GameOfLifeAgent.BOARD_WIDTH", CellularAutomaton.BOARD_WIDTH, vCA.getGameBoard().length);
		assertEquals("board height must be equal to GameOfLifeAgent.BOARD_HEIGHT", CellularAutomaton.BOARD_HEIGHT, vCA.getGameBoard()[0].length);
	}

	@Test
	public void testInitialise() {
		CellularAutomaton vCA = new CellularAutomaton(GOL_THRESHOLD);
		GameOfLife vGol = new GameOfLife();
		vGol.loadPatternFromFile("GliderGun.txt");
		vCA.setInitialPattern(vGol.getInitialPattern());
		assertNotNull("Initialization Pattern must be loaded (not null) prior calling initialise()", vCA.getInitialPattern());
		vCA.initialise(GOL_THRESHOLD);
		int[][] va = vCA.getGameBoard();
		assertEquals("test if GliderGun has been loaded OK",-1, va[11][16]);
		assertEquals("test if GliderGun has been loaded OK",-1, va[12][16]);
		assertEquals("test if GliderGun has been loaded OK", 0, va[13][16]);
		assertEquals("test if GliderGun has been loaded OK", 0, va[14][16]);
		assertEquals("test if GliderGun has been loaded OK",-1, va[21][16]);
		assertEquals("test if GliderGun has been loaded OK", 0, va[22][16]);
	}

	@Test
	public void testLoadPatternFromFile() {
		GameOfLife vGol = new GameOfLife();
		vGol.loadPatternFromFile("GliderGun.txt");
		assertNotNull("Initialization Pattern must be loaded (not null) prior calling initialise()", vGol.getInitialPattern());
		int[][] va = vGol.getInitialPattern();
		// test that the game board contains expected pattern
		assertEquals("test if GliderGun has been loaded OK",-1, va[1][5]);
		assertEquals("test if GliderGun has been loaded OK",-1, va[2][5]);
		assertEquals("test if GliderGun has been loaded OK", 0, va[3][5]);
		assertEquals("test if GliderGun has been loaded OK", 0, va[4][5]);
		assertEquals("test if GliderGun has been loaded OK",-1, va[11][5]);
		assertEquals("test if GliderGun has been loaded OK", 0, va[12][5]);
	}

	@Test
	public void testStep() {
		CellularAutomaton vCA = new CellularAutomaton(GOL_THRESHOLD);
		GameOfLife vGol = new GameOfLife();
		vGol.loadPatternFromFile("Blinker.txt");
		vCA.setInitialPattern(vGol.getInitialPattern());
		assertNotNull("Initialization Pattern must be loaded (not null) prior calling initialise()", vCA.getInitialPattern());
		vCA.initialise(GOL_THRESHOLD);
		int[][] va = vCA.getGameBoard();
		assertEquals("test if Blinker has been deployed OK", 0, va[0][1]);
		assertEquals("test if Blinker has been deployed OK",-1, va[1][1]);
		assertEquals("test if Blinker has been deployed OK", 0, va[2][1]);
		vCA.step();
		va=vCA.getGameBoard();
		assertEquals("test if Blinker after step  OK",-1, va[0][1]);
		assertEquals("test if Blinker after step  OK",-1, va[1][1]);
		assertEquals("test if Blinker after step  OK",-1, va[2][1]);
	}

}
