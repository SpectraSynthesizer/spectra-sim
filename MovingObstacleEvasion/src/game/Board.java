/*
Copyright (c) since 2015, Tel Aviv University and Software Modeling Lab

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of Tel Aviv University and Software Modeling Lab nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Tel Aviv University and Software Modeling Lab 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/

package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import tau.smlab.syntech.controller.executor.ControllerExecutor;
import tau.smlab.syntech.games.controller.jits.BasicJitController;

@SuppressWarnings("serial")
public class Board extends JFrame {
	boolean isObstacleSmart = true;
	final int dim = 8;
	final int numOfGlitches = 1;
	final int dimLen = 100;
	int[] robot = new int[] { 1, 1  };
	int[] obstacle = new int[] { dim - 1, dim - 1 };
	BufferedImage m;
	ControllerExecutor executor;
	Random rand = new Random();
	Map<String, String> inputs = new HashMap<String, String>();
	
	boolean isObstacleTurn = true;
	boolean obsWait = true;
	int glitches = 0;
	boolean isGlitch = false;
	
	boolean run = true;
	boolean finished = false;

	public int moveObstacle(int loc) {
		int new_loc = loc + rand.nextInt(3) - 1;
		if (new_loc < 0) {
			new_loc = 0;
		}
		if (new_loc > dim - 1) {
			new_loc = dim - 1;
		}
		return new_loc;
	}

	public int moveObstacleSmart(int obsLoc, int robotLoc) {
		int new_loc = obsLoc;
		if (obsLoc < robotLoc) {
			new_loc = obsLoc + 1;
		} else if (obsLoc > robotLoc) {
			new_loc = obsLoc - 1;
		}
		return new_loc;
	}

	public void run() throws Exception {
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				run = false;
				while (!finished) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				executor.free();
				System.out.println("Controller is freed");
			}
		}));
		
		executor = new ControllerExecutor(new BasicJitController(), "out/jit", "MovingObstacle");
		m = ImageIO.read(new File("img/robot.jpg"));

		inputs.put("obstacle[0]", Integer.toString(obstacle[0]));
		inputs.put("obstacle[1]", Integer.toString(obstacle[1]));
		inputs.put("isObstacleTurn", Boolean.toString(isObstacleTurn));
		inputs.put("obsWait", Boolean.toString(obsWait));
		inputs.put("glitches", Integer.toString(glitches));
		inputs.put("isGlitch", Boolean.toString(isGlitch));
		executor.initState(inputs);
		
		handleOutputs();

		while (run) {
			inputs.put("obstacle[0]", Integer.toString(obstacle[0]));
			inputs.put("obstacle[1]", Integer.toString(obstacle[1]));
			inputs.put("isObstacleTurn", Boolean.toString(isObstacleTurn));
			inputs.put("obsWait", Boolean.toString(obsWait));
			inputs.put("glitches", Integer.toString(glitches));
			inputs.put("isGlitch", Boolean.toString(isGlitch));

			executor.updateState(inputs);

			handleOutputs();
		}
		
		finished = true;
	}
	
	private void handleOutputs() throws InterruptedException {
		Map<String, String> sysValues = executor.getCurrOutputs();

		robot[0] = Integer.parseInt(sysValues.get("robot[0]"));
		robot[1] = Integer.parseInt(sysValues.get("robot[1]"));

		paint(this.getGraphics());
		Thread.sleep(1000);

		boolean curIsObstacleTurn = isObstacleTurn;
		boolean curObsWait = obsWait;

		if (curIsObstacleTurn) {
			if (isObstacleSmart) {
				obstacle[0] = moveObstacleSmart(obstacle[0], robot[0]);
				obstacle[1] = moveObstacleSmart(obstacle[1], robot[1]);
			} else {
				obstacle[0] = moveObstacle(obstacle[0]);
				obstacle[1] = moveObstacle(obstacle[1]);
			}
			isObstacleTurn = false;
			if (isGlitch) {
				obsWait = false;
			}
		}
		if (!curIsObstacleTurn && curObsWait) {
			obsWait = false;
		}
		if (!curIsObstacleTurn && !curObsWait) {
			isObstacleTurn = true;
			obsWait = true;
		}

		if (isGlitch) {
			glitches += 1;
		}

		if (isObstacleTurn && glitches < numOfGlitches) {
			isGlitch = rand.nextBoolean();
		} else {
			isGlitch = false;
		}
	}

	@Override
	public void paint(Graphics g) {
		int row;
		int col;

		for (row = 0; row < dim; row++) {
			for (col = 0; col < dim; col++) {
				if ((row % 2) == (col % 2))
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.LIGHT_GRAY);

				g.fillRect(col * dimLen, row * dimLen, dimLen, dimLen);
			}
		}

		g.setColor(Color.BLACK);
		g.fillRect((obstacle[0]-1) * dimLen, (obstacle[1]-1) * dimLen, dimLen, dimLen);
		g.fillRect(obstacle[0] * dimLen, (obstacle[1]-1) * dimLen, dimLen, dimLen);
		g.fillRect((obstacle[0]-1) * dimLen, (obstacle[1]) * dimLen, dimLen, dimLen);
		g.fillRect((obstacle[0]) * dimLen, (obstacle[1]) * dimLen, dimLen, dimLen);

		if (m != null) {
			g.drawImage(m, (robot[0]-1) * dimLen, (robot[1]-1) * dimLen, null);
		} else {
			g.setColor(Color.BLUE);
			g.fillRect((robot[0]-1) * dimLen, (robot[1]-1) * dimLen, dimLen, dimLen);
		}
	}

	public static void main(String args[]) throws Exception {
		Board check = new Board();
		check.setTitle("Moving Obstacle Evasion");
		check.setSize(check.dim * check.dimLen, check.dim * check.dimLen);
		check.setDefaultCloseOperation(EXIT_ON_CLOSE);
		check.setVisible(true);
		check.run();
	}
}