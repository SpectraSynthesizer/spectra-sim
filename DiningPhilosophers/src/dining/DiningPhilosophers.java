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

package dining;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.geom.*;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import tau.smlab.syntech.controller.executor.ControllerExecutor;
import tau.smlab.syntech.games.controller.jits.BasicJitController;

@SuppressWarnings("serial")
public class DiningPhilosophers extends JComponent {

	ControllerExecutor ctrlExec;
	final static int frameHeight = 800;
	final static int frameWidth = 800;
	
	final static int numberOfPhilosophers = 5;
	
	final static int philoHeight = 120;
	final static int philoWidth = 120;

	static enum Fork {
		FREE, LEFT, RIGHT;
	}
	
	static boolean[] hungry = new boolean[numberOfPhilosophers];
	static Fork[] forks = new Fork[numberOfPhilosophers];
	
	static BufferedImage[] philosophers = new BufferedImage[numberOfPhilosophers];
	static BufferedImage fork;
	static BufferedImage spaghetti;

	static int[] numOfDisksOnTowerArray;
	static final int towerPosition[] = new int[] { frameWidth / 5, frameWidth / 2, frameWidth * 4 / 5 };
	static final Color colorArray[] = new Color[] { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN,
			Color.BLUE, Color.MAGENTA, Color.PINK, Color.GRAY, Color.WHITE };
	
	boolean run = true;
	boolean finished = false;

	public DiningPhilosophers() {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				
				Map<String, String> inputs = new HashMap<>();
				Map<String, String> outputs = new HashMap<>();
				
				for (int i = 0; i < numberOfPhilosophers; i++) {
					inputs.put(String.format("hungry[%d]", i), String.valueOf(hungry[i]));
				}
				
				// instantiate a new controller executor
				// by default, creates a log file of the execution in ./logs folder
				// If you do not see the ./logs folder, refresh the Eclipse package explorer
				try {
					ctrlExec = new ControllerExecutor(new BasicJitController(), "out/jit", "DiningPhilosophers");
					ctrlExec.initState(inputs);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				outputs = ctrlExec.getCurrOutputs();
				for (int i = 0; i < numberOfPhilosophers; i++) {
					forks[i] = Fork.valueOf(DiningPhilosophers.Fork.class, outputs.get(String.format("forks[%d]", i)));
				}

				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				while (run) {
					
					for (int i = 0; i < numberOfPhilosophers; i++) {
						inputs.put(String.format("hungry[%d]", i), String.valueOf(hungry[i]));
					}

					try {
						// Try to update the state of the controller, provided the above user inputs
						// The update step randomly picks a valid next state
						ctrlExec.updateState(inputs);
					} catch (Exception ce) {
						// The above inputs violate the assumptions
						System.err.println(ce.getMessage());
					}

					outputs = ctrlExec.getCurrOutputs();
					for (int i = 0; i < numberOfPhilosophers; i++) {
						forks[i] = Fork.valueOf(DiningPhilosophers.Fork.class, outputs.get(String.format("forks[%d]", i)));
					}
					
					repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				finished = true;
			}
		});
		
		// Load images
		try {
			for (int i = 0; i < numberOfPhilosophers; i++) {
				philosophers[i] = ImageIO.read(new File(String.format("img/philo%d.jpg", i)));
			}
			fork = ImageIO.read(new File("img/fork.png"));
			spaghetti = ImageIO.read(new File("img/spaghetti.png"));
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		animationThread.start();
		repaint();
		
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

		g.setColor(Color.CYAN);
		g.fillRect(0, 0, frameWidth, frameHeight);
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(110, 110, frameWidth - 220, frameHeight - 220);
		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 16));
		g.drawChars("Click on the image of a philosopher to request food.".toCharArray(), 0, 52, 4, 20);
		g.drawChars("Click again to release forks but only after having eaten.".toCharArray(), 0, 57, 4, 40);
		
		g.drawImage(spaghetti, 285, 285, 250, 250, null);
		
		if (hungry[0]) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("HUNGRY".toCharArray(), 0, 6, 365, 30);
		}
		if (Fork.RIGHT.equals(forks[0]) && Fork.LEFT.equals(forks[1])) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("EATING".toCharArray(), 0, 6, 365, 180);
		}
		
		Ellipse2D.Float circle = new Ellipse2D.Float();
		circle.setFrame(350, 40, philoWidth, philoHeight);
		g.setClip(circle);
		g.drawImage(philosophers[0], 350, 40, philoWidth, philoHeight, null);
		
		if (hungry[1]) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("HUNGRY".toCharArray(), 0, 6, 635, 240);
		}
		if (Fork.RIGHT.equals(forks[1]) && Fork.LEFT.equals(forks[2])) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("EATING".toCharArray(), 0, 6, 635, 390);
		}
		
		circle = new Ellipse2D.Float();
		circle.setFrame(620, 250, philoWidth, philoHeight);
		g.setClip(circle);
		g.drawImage(philosophers[1], 620, 250, philoWidth, philoHeight, null);
		
		if (hungry[2]) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("HUNGRY".toCharArray(), 0, 6, 515, 590);
		}
		if (Fork.RIGHT.equals(forks[2]) && Fork.LEFT.equals(forks[3])) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("EATING".toCharArray(), 0, 6, 515, 740);
		}
		
		circle = new Ellipse2D.Float();
		circle.setFrame(500, 600, philoWidth, philoHeight);
		g.setClip(circle);
		g.drawImage(philosophers[2], 500, 600, philoWidth, philoHeight, null);
		
		if (hungry[3]) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("HUNGRY".toCharArray(), 0, 6, 175, 590);
		}
		if (Fork.RIGHT.equals(forks[3]) && Fork.LEFT.equals(forks[4])) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("EATING".toCharArray(), 0, 6, 175, 740);
		}
		
		circle = new Ellipse2D.Float();
		circle.setFrame(160, 600, philoWidth, philoHeight);
		g.setClip(circle);
		g.drawImage(philosophers[3], 160, 600, philoWidth, philoHeight, null);
		
		if (hungry[4]) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("HUNGRY".toCharArray(), 0, 6, 75, 240);
		}
		if (Fork.RIGHT.equals(forks[4]) && Fork.LEFT.equals(forks[0])) {
			g.setClip(null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 24));
			g.drawChars("EATING".toCharArray(), 0, 6, 75, 390);
		}
		
		circle = new Ellipse2D.Float();
		circle.setFrame(60, 250, philoWidth, philoHeight);
		g.setClip(circle);
		g.drawImage(philosophers[4], 60, 250, philoWidth, philoHeight, null);
		
		g.setClip(null);
		
		if (Fork.LEFT.equals(forks[0])) {
			g.drawImage(fork, 160, 260, 70, 100, null);
		} else if (Fork.RIGHT.equals(forks[0])) {
			g.drawImage(fork, 320, 140, 70, 100, null);
		} else {
			g.drawImage(fork, 240, 180, 70, 100, null);
		}
		
		if (Fork.LEFT.equals(forks[1])) {
			g.drawImage(fork, 420, 140, 70, 100, null);
		} else if (Fork.RIGHT.equals(forks[1])) {
			g.drawImage(fork, 580, 260, 70, 100, null);
		} else {
			g.drawImage(fork, 500, 180, 70, 100, null);
		}
		
		if (Fork.LEFT.equals(forks[2])) {
			g.drawImage(fork, 640, 360, 70, 100, null);
		} else if (Fork.RIGHT.equals(forks[2])) {
			g.drawImage(fork, 570, 500, 70, 100, null);
		} else {
			g.drawImage(fork, 590, 420, 70, 100, null);
		}
		
		if (Fork.LEFT.equals(forks[3])) {
			g.drawImage(fork, 450, 560, 70, 100, null);
		} else if (Fork.RIGHT.equals(forks[3])) {
			g.drawImage(fork, 270, 560, 70, 100, null);
		} else {
			g.drawImage(fork, 360, 560, 70, 100, null);
		}
		
		if (Fork.LEFT.equals(forks[4])) {
			g.drawImage(fork, 170, 500, 70, 100, null);
		} else if (Fork.RIGHT.equals(forks[4])) {
			g.drawImage(fork, 100, 360, 70, 100, null);
		} else {
			g.drawImage(fork, 150, 420, 70, 100, null);
		}

	}

	public static void main(String args[]) throws Exception {
		JFrame f = new JFrame("Spectra Simulation: Dining Philosophers");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(frameWidth, frameHeight);
		DiningPhilosophers dph = new DiningPhilosophers();
		f.setContentPane(dph);
		f.setVisible(true);
		f.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {

				int x = e.getX();
				int y = e.getY();
				
				if (x > 350 && x < 350 + philoWidth && y > 40 && y < 40 + philoHeight) {
					
					if (Fork.RIGHT.equals(forks[0]) && Fork.LEFT.equals(forks[1])) {
						hungry[0] = false;
					} else {
						hungry[0] = true;
					}
				}
				
				if (x > 620 && x < 620 + philoWidth && y > 250 && y < 250 + philoHeight) {
					
					if (Fork.RIGHT.equals(forks[1]) && Fork.LEFT.equals(forks[2])) {
						hungry[1] = false;
					} else {
						hungry[1] = true;
					}
				}
				
				if (x > 500 && x < 500 + philoWidth && y > 600 && y < 600 + philoHeight) {
					
					if (Fork.RIGHT.equals(forks[2]) && Fork.LEFT.equals(forks[3])) {
						hungry[2] = false;
					} else {
						hungry[2] = true;
					}
				}
				
				if (x > 160 && x < 160 + philoWidth && y > 600 && y < 600 + philoHeight) {
					
					if (Fork.RIGHT.equals(forks[3]) && Fork.LEFT.equals(forks[4])) {
						hungry[3] = false;
					} else {
						hungry[3] = true;
					}
				}
				
				if (x > 60 && x < 60 + philoWidth && y > 250 && y < 250 + philoHeight) {
					
					if (Fork.RIGHT.equals(forks[4]) && Fork.LEFT.equals(forks[0])) {
						hungry[4] = false;
					} else {
						hungry[4] = true;
					}
				}
			}
		});
	}
}
