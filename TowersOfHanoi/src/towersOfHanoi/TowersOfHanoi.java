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

package towersOfHanoi;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import tau.smlab.syntech.controller.executor.ControllerExecutor;
import tau.smlab.syntech.games.controller.jits.BasicJitController;

@SuppressWarnings("serial")
public class TowersOfHanoi extends JComponent {

	ControllerExecutor ctrlExec;
	final static int frameHeight = 500; //1000
	final static int frameWidth = 800; //1600
	final static int numberOfTowers = 3;
	static int[] diskPosition;
	static Disk[] disksArray;
	static final String moveDiskToTowerString = "moveDiskToTower";
	static final String moveDiskNumberString = "moveDiskNumber";
	static int moveDiskNumber;
	static int moveDiskToTower;
	static final int towerHeight = frameHeight / 2;
	static final int towerWidth = 50;
	static int[] numOfDisksOnTowerArray;
	static final int towerPosition[] = new int[] { frameWidth / 5, frameWidth / 2, frameWidth * 4 / 5 };
	Thread thread;
	static final Color colorArray[] = new Color[] { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN,
			Color.BLUE, Color.MAGENTA, Color.PINK, Color.GRAY, Color.WHITE };
	
	boolean run = true;
	boolean finished = false;

	public TowersOfHanoi() {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				// instantiate a new controller executor
				// by default, creates a log file of the execution in ./logs folder
				// If you do not see the ./logs folder, refresh the Eclipse package explorer
				try {
					ctrlExec = new ControllerExecutor(new BasicJitController(), "out/jit", "TowersOfHanoi");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Map<String, String> envValues;
				Map<String, String> sysValues;

				diskPosition = new int[ctrlExec.getEnvVars().size()];
				disksArray = new Disk[ctrlExec.getEnvVars().size()];
				numOfDisksOnTowerArray = new int[numberOfTowers];
				numOfDisksOnTowerArray[0] = ctrlExec.getEnvVars().size();

				for (int i = 0; i < ctrlExec.getEnvVars().size(); i++) {
					disksArray[i] = new Disk(i);
				}

				repaint();
				boolean ini = true;
				while (run) {

					try {
						// We pass an empty map to the executor because there is no input from the
						// environment in this simulation
						if (ini) {
							ctrlExec.initState(new HashMap<String,String>());
							ini = false;
						} else {
							ctrlExec.updateState(new HashMap<String,String>());
						}
					} catch (Exception ce) {
						// The above inputs violate the assumptions
						System.err.println(ce.getMessage());
					}

					envValues = ctrlExec.getCurrInputs();
					sysValues = ctrlExec.getCurrOutputs();

					// Set the next outputs (and possibly the missing, undetermined next inputs)
					// according to the next state that has been picked by the executor in the
					// update step

					for (Map.Entry<String, String> entry : envValues.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						diskPosition[Character.getNumericValue(key.charAt(key.indexOf('[') + 1))] = Integer
								.parseInt(value);
					}
					moveDiskNumber = Integer.parseInt(sysValues.get(moveDiskNumberString));
					moveDiskToTower = Integer.parseInt(sysValues.get(moveDiskToTowerString));
					
					numOfDisksOnTowerArray[disksArray[moveDiskNumber].onTower] -= 1;
					
					while (disksArray[moveDiskNumber].onTower != moveDiskToTower) {
						repaint();
						disksArray[moveDiskNumber].move(moveDiskToTower);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					numOfDisksOnTowerArray[moveDiskToTower] += 1;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				finished = true;
			}
		});
		animationThread.start();
		
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
				ctrlExec.free();
				System.out.println("Controller is freed");
			}
		}));
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, frameWidth, frameHeight);

		g.setColor(Color.BLACK);
		// paint towers
		g.fillRect(towerPosition[0], frameHeight - towerHeight, towerWidth, towerHeight);
		g.fillRect(towerPosition[1], frameHeight - towerHeight, towerWidth, towerHeight);
		g.fillRect(towerPosition[2], frameHeight - towerHeight, towerWidth, towerHeight);

		// color disks
		if (disksArray != null) {
			for (int i = 0; i < disksArray.length; i++) {
				g.setColor(colorArray[i]);
				g.fillRect(disksArray[i].diskXPosition, disksArray[i].diskYPosition, disksArray[i].diskSize,
						Disk.diskWidth);
			}
		}
	}

	public static void main(String args[]) throws Exception {
		JFrame f = new JFrame("Spectra Simulation: Towers Of Hanoi");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(frameWidth, frameHeight + 39 + 50);
		TowersOfHanoi gtoh = new TowersOfHanoi();
		f.setContentPane(gtoh);
		f.setVisible(true);
	}
}
