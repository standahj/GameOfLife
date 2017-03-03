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

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * <p>The concrete instances of the Animator class maintain and update the current
 * state of our animation. </p>
 * 
 * <p>This base class extends JPanel, to provide a component upon 
 * which the animation is drawn. It also implements Runnable
 * in order to provide a thread which periodically updates
 * the animation and calls <code>repaint()</code> to persuade the event
 * dispatch thread to paint the next frame.</p>
 * 
 * <p>As part of the Assignment 2 I have modified this class to be an abstract base
 * class for the GameOfLife and Ant games that provides common functionality and
 * frame upon which the each individual game is built.</p>
 *   
 * @author Dominic Verity and Daniel 
 *
 */
public abstract class Animator extends JPanel implements Runnable, Animatable {

	private static final long serialVersionUID = 1L;

	public static int 		FRAME_PAUSE = 200;
	
	protected boolean 		mShow = true;
	protected boolean 		mRun  = true;
	/**
	 * Constructor, sets the canvas & window property 
	 */
	public Animator() {
	}
		
	/**
	 * Updates the state of the game for each iteration.
	 */
	public abstract void step();
	
	/**
	 * The paintComponent() method which is called from the event
	 * dispatch thread whenever the GUI wants to repaint an
	 * Animator component.
	 */
	public void paintComponent(Graphics pGraphics) {
		paint(pGraphics);
	}
	
	/** Paint the current state of the animation onto the graphics canvas.*/
	public abstract void paint(Graphics pGraphics);

	/** Get the width of this animation in pixels.*/
	public abstract int getWidth();

	/** Get the height of this animation in pixels.*/
	public abstract int getHeight();

	/**
	 * The run() method of an Animator thread simply loops
	 * calling step() to update the positions of all cells
	 * and then calling repaint() to inform the event dispatch
	 * thread that it needs to paint the next frame.
	 * 
	 * We pause between each frame for FRAME_PAUSE milliseconds,
	 * so decreasing FRAME_PAUSE we can increase the frame rate (and
	 * thus the speed) of the animation.
	 */
	public void run() {
		while (mRun) {
			if (mShow) {
				if (mRun)
					step();
				if (mRun)
					repaint();
			}
			try {
				if (mRun)
					Thread.sleep(FRAME_PAUSE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Allows user suspend game progress.
	 */
	public void suspend() {
		mShow = false;
	}
	
	/**
	 * Allows user to resume game progress.
	 */
	public void resume() {
		mShow = true;
	}
	
	public boolean toggleRun() {
		mShow = !mShow;
		return mShow;
	}
	
	/**
	 * instructs the thread to exit the loop.
	 */
	public void terminate() {
		mRun = false;
	}
}
