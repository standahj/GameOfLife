/**
 * This file is part of a project entitled Week8Samples which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011-2012 Dominic Verity and Macquarie University.
 * 
 * Week8Samples is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Week8Samples is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Week8Samples. (See files COPYING and COPYING.LESSER.) If not,
 * see <http://www.gnu.org/licenses/>.
 */

package edu.mqu.assign2.gol;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 * a comment: this class has been adapted from the Bouncing.java class provided
 * in the sample code for the bouncing head.
 * As per instructions, the cleanup operation to remove everything that relates to
 * original purpose - implement a Bouncing Head - must be removed, I have renamed the class
 * from Bouncing to GameOfLife.
 * 
 * References to sprites has been removed too, actionPerormed() method has been rewritten to
 * enable the event handling in the concrete Animator class.
 * 
 */
@SuppressWarnings("serial")
public class GameOfLife extends JFrame implements ActionListener {

	private static GameOfLife mApplication;

	public static final int ALIVE 			 = -1;
	public static final int DEAD 			 = 0;
	public static final int BUTTON_BAR_WIDTH = 140;

	public static final float GOL_ALIVE_TRESHOLD = 0.5f;
	public static final float ANT_ALIVE_TRESHOLD = 2.0f;

	public static final String GOL_TITLE = "Game Of Life";
	public static final String ANT_TITLE = "Langdon's Ant";
	
	private JPanel mContentPane = null;
	private CellularAutomaton mSpriteAnimator = null;
	private JButton mButton = null;
	private JButton mPauseButton = null;
	private JPanel  mButtonBar = null;

	private int[][]  mInitialPattern = null;
	private String[] mPatterns 	  = new String[] {"GliderGun.txt", "RPentomino.txt", "Blinker.txt"};
	private int 	 mPatternsIdx = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		mApplication = new GameOfLife();
			
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mApplication.initApp("Game of Life", null, CellularAutomaton.BOARD_WIDTH, CellularAutomaton.BOARD_HEIGHT, GOL_ALIVE_TRESHOLD, null);
			}
		});
	}

	/**
	 * Setup the GUI by calling {@link GameOfLife#setup()} and
	 * set the application running.
	 *
	 * @param pTitle  window title
	 * @param pAgent  agent to be used by <class>CellularAutomaton</class>. This determines the game type.
	 * @param pGameWidth  Game width in cells.
	 * @param pGameHeight Game height in cells.
	 * @param pAliveThreshold  Threshold to use for marking the cell Alive (Black) during the initial setup when no initial pattern is provided.
	 * 			{@link CellularAutomaton#initialise()} uses random generator with range <0,1), thus setting the
	 * 			threshold above 1.0f ensures all cells are Dead (White) initially. 
	 * @param pInitialPattern the pattern to initialize the game board. If NULL, the use random initialization, see <b>pAliveThreshold</b> parameter.
	 */
	private void initApp(String pTitle, Agent pAgent, int pGameWidth, int pGameHeight, float pAliveThreshold, int[][] pInitialPattern) {
		setup(pTitle, pAgent==null?new GameOfLifeAgent():pAgent, pGameWidth, pGameHeight, pAliveThreshold, pInitialPattern);
		pack();
		setVisible(true);
		new Thread(mSpriteAnimator).start();
	}

	
	/**
	 * Create the GUI components for this application
	 * and lay them out on the content pane of a top-level 
	 * window.
	 * 
	 * @param pTitle  window title
	 * @param pAgent  agent to be used by <class>CellularAutomaton</class>. This determines the game type.
	 * @param pGameWidth  Game width in cells.
	 * @param pGameHeight Game height in cells.
	 * @param pAliveThreshold  Threshold to use for marking the cell Alive (Black) during the initial setup.
	 * 			{@link CellularAutomaton#initialise()} uses random generator with range <0,1), thus setting the
	 * 			threshold above 1.0f ensures all cells are Dead (White) initially. 
	 */
	private void setup(String pTitle, Agent pAgent, int pGameWidth, int pGameHeight, float pAliveThreshold, int[][] pInitialPattern) {
		setTitle(pTitle);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mSpriteAnimator = new CellularAutomaton(pGameWidth, pGameHeight, pAliveThreshold, pInitialPattern);
		pAgent.setEnvironment(mSpriteAnimator);
		mSpriteAnimator.setAgent(pAgent);

		setPreferredSize(new Dimension(mSpriteAnimator.getWidth()+BUTTON_BAR_WIDTH, mSpriteAnimator.getHeight()));

		mButtonBar = new JPanel(); // container for control buttons
		mButtonBar.setPreferredSize(new Dimension(BUTTON_BAR_WIDTH, mSpriteAnimator.getHeight()));
		// Control buttons definition
		mButton = new JButton();
		mButton.setText("Change Init");
		mButton.addActionListener(this);
		mButton.setActionCommand("NewGame");  // add action command for easy identification
		mPauseButton = new JButton();
		mPauseButton.setText("Pause");
		mPauseButton.addActionListener(this);
		mPauseButton.setActionCommand("Pause");

		JButton vFasterButton = new JButton();
		vFasterButton.setText("Faster");
		vFasterButton.addActionListener(this);
		vFasterButton.setActionCommand("Faster");
		JButton vSlowerButton = new JButton();
		vSlowerButton.setText("Slower");
		vSlowerButton.addActionListener(this);
		vSlowerButton.setActionCommand("Slower");

		JButton vBiggerButton = new JButton();
		vBiggerButton.setText("Bigger Cell");
		vBiggerButton.setActionCommand("Bigger");
		vBiggerButton.addActionListener(this);
		JButton vSmallerButton = new JButton();
		vSmallerButton.setText("Smaller Cell");
		vSmallerButton.setActionCommand("Smaller");
		vSmallerButton.addActionListener(this);
		
		JButton vGOLButton = new JButton();
		vGOLButton.setText("Game Of Life");
		vGOLButton.setActionCommand("GOL");
		vGOLButton.addActionListener(this);
		JButton vAntButton = new JButton();
		vAntButton.setText("Ant");
		vAntButton.setActionCommand("Ant");
		vAntButton.addActionListener(this);

		mContentPane = (JPanel)getContentPane();
		mContentPane.setLayout(new BorderLayout());
		mContentPane.add(mSpriteAnimator, BorderLayout.CENTER);
		mContentPane.add(mButtonBar, BorderLayout.EAST);
		mButtonBar.setLayout(new FlowLayout());
		mButtonBar.add(mButton);
		mButtonBar.add(vFasterButton);
		mButtonBar.add(vSlowerButton);
		mButtonBar.add(mPauseButton);
		mButtonBar.add(vBiggerButton);
		mButtonBar.add(vSmallerButton);
		mButtonBar.add(vGOLButton);
		mButtonBar.add(vAntButton);
	}

	/* (non-Javadoc)
	 * Passes the action event to a concrete game instance. 
	 * Game may responds to a button push by reseting, pausing etc....
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String vActionCommand = e.getActionCommand();
		if ("NewGame".equals(vActionCommand)) {
			mSpriteAnimator.suspend();
			loadPatternFromFile(mPatterns[mPatternsIdx++]);
			mPatternsIdx = mPatternsIdx % mPatterns.length;
			mSpriteAnimator.setInitialPattern(mInitialPattern);
			mSpriteAnimator.initialise(GOL_ALIVE_TRESHOLD); // the threshold value is ignored when there's initial pattern available
			mSpriteAnimator.resume();
		} else if ("Faster".equals(vActionCommand)) {
			if (Animator.FRAME_PAUSE > 1)  // If allowed to go to 0, then it cannot slow down....
				Animator.FRAME_PAUSE = Animator.FRAME_PAUSE / 2;
		} else if ("Slower".equals(vActionCommand)) {
			Animator.FRAME_PAUSE = Animator.FRAME_PAUSE * 2;
		} else if ("Pause".equals(vActionCommand)) {
			mSpriteAnimator.toggleRun();
		} else if ("Bigger".equals(vActionCommand)) {
			int vCurrentWidth = mSpriteAnimator.getGameBoard().length;
			int vCurrentHeight = mSpriteAnimator.getGameBoard()[0].length;
			if (vCurrentWidth > 25) {
				mSpriteAnimator.terminate();
				Agent vAgent   = mSpriteAnimator.getAgent();
				int[][] vInitialPattern = mSpriteAnimator.getInitialPattern();
				vCurrentWidth  = vCurrentWidth / 2;
				vCurrentHeight = vCurrentHeight / 2;
				if (AntAgent.class.isInstance(vAgent)) { 
					initApp(ANT_TITLE, new AntAgent(), vCurrentWidth, vCurrentHeight, ANT_ALIVE_TRESHOLD, vInitialPattern);
				}else {
					initApp(GOL_TITLE, new GameOfLifeAgent(), vCurrentWidth, vCurrentHeight, GOL_ALIVE_TRESHOLD, vInitialPattern);
				}
			}
		} else if ("Smaller".equals(vActionCommand)) {
			int vCurrentWidth = mSpriteAnimator.getGameBoard().length;
			int vCurrentHeight = mSpriteAnimator.getGameBoard()[0].length;
			if (vCurrentWidth < CellularAutomaton.BOARD_WIDTH) {
				mSpriteAnimator.terminate();
				Agent vAgent   = mSpriteAnimator.getAgent();
				int[][] vInitialPattern = mSpriteAnimator.getInitialPattern();
				vCurrentWidth  = vCurrentWidth * 2;
				vCurrentHeight = vCurrentHeight * 2;
				if (AntAgent.class.isInstance(vAgent)) { 
					initApp(ANT_TITLE, new AntAgent(), vCurrentWidth, vCurrentHeight, ANT_ALIVE_TRESHOLD, vInitialPattern);
				}else {
					initApp(GOL_TITLE, new GameOfLifeAgent(), vCurrentWidth, vCurrentHeight, GOL_ALIVE_TRESHOLD, vInitialPattern);
				}
			}
		} else if ("GOL".equals(vActionCommand)) {
			mSpriteAnimator.terminate();
			int vCurrentWidth = mSpriteAnimator.getGameBoard().length;
			int vCurrentHeight = mSpriteAnimator.getGameBoard()[0].length;
			int[][] vInitialPattern = mSpriteAnimator.getInitialPattern();
			initApp(GOL_TITLE, new GameOfLifeAgent(), vCurrentWidth, vCurrentHeight, GOL_ALIVE_TRESHOLD, vInitialPattern);
		} else if ("Ant".equals(vActionCommand)) {
			mSpriteAnimator.terminate();
			int vCurrentWidth = mSpriteAnimator.getGameBoard().length;
			int vCurrentHeight = mSpriteAnimator.getGameBoard()[0].length;
			int[][] vInitialPattern = mSpriteAnimator.getInitialPattern();
			initApp(ANT_TITLE, new AntAgent(), vCurrentWidth, vCurrentHeight, ANT_ALIVE_TRESHOLD, vInitialPattern);
		} 
		if ("Pause".equals(e.getActionCommand())) {
			if ("Pause".equals(mPauseButton.getText())) {
				mPauseButton.setText("Resume");
			} else {
				mPauseButton.setText("Pause");
			}
		}
	}

	/**
	 * Loads a pattern definition from file.
	 * Decodes and sets it as the initialization pattern.
	 * Pattern is defined as set of strings containing O and X describing the pattern, e.g.
	 * R-pentomino pattern is described with 5 strings as
	 * OOOOO
	 * OOXXO
	 * OXXOO
	 * OOXOO
	 * OOOOO
	 * 
	 * @param pFileName
	 */
	public void loadPatternFromFile(String pFileName) {
		try {
			// Step #1 - read strings (lines) from file
			InputStream vInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(pFileName);
			BufferedReader vBufferedReader = new BufferedReader(new InputStreamReader(vInputStream));
			ArrayList<String> vLines = new ArrayList<String>();
			String vLine = vBufferedReader.readLine();
			while (vLine != null) {
				vLines.add(vLine);
				vLine = vBufferedReader.readLine();
			}
			vInputStream.close();
			// Step #2 - convert strings of O and X to 2D int array.
			int vXDimension = vLines.get(0).length();
			int vYDimension = vLines.size();
			mInitialPattern = new int[vXDimension][vYDimension];
			for (int x = 0; x < vXDimension; x++) {
				for (int y = 0; y <vYDimension; y++) {
					try {
						mInitialPattern[x][y] = vLines.get(y).charAt(x)== 'O' ||
										vLines.get(y).charAt(x)== '0' ||
										vLines.get(y).charAt(x)== 'o' ? DEAD :ALIVE;
					} catch (Exception e) {
						// probably index out of range exception, it does not really matter, set it as DEAD cell 
						mInitialPattern[x][y] = DEAD;
					}
				}
			}
		} catch (IOException e) {
			// nothing to but ignore this entry
			System.out.println(e.getMessage());
		}
	}
	
	public int[][] getInitialPattern() {
		return mInitialPattern;
	}

}