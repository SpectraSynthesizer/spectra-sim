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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import tau.smlab.syntech.controller.executor.ControllerExecutor;
import tau.smlab.syntech.controller.jit.BasicJitController;

@SuppressWarnings("serial")
public class Board extends JFrame {
	static final int numberOfBuckets = 5;
	final int bucketsToEmpty = 2;
	static final int addedWaterUnits = 5;
	final int capacity = 9;
	static int[] buckets = new int[numberOfBuckets];
	static int[] unitsToFill = new int[numberOfBuckets];;
	static int firstToEmpty = 0;
	static boolean isCinderellaTurn = true;
	BufferedImage imgBucket;
	BufferedImage imgCinderella;
	BufferedImage imgStepmother;
	ControllerExecutor executor;
	static Random rand = new Random();
	Map<String, String> inputs = new HashMap<String, String>();
	boolean run = true;
	boolean finished = false;

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
		
		executor = new ControllerExecutor(new BasicJitController(), "out");
		imgBucket = ImageIO.read(new File("img/bucket.jpg"));
		imgCinderella = ImageIO.read(new File("img/cinderella.jpg"));
		imgStepmother = ImageIO.read(new File("img/stepmother.jpg"));
		
		generateRandomWaterFills(inputs);
		isCinderellaTurn = false;
		paint(this.getGraphics());
		Thread.sleep(3000);

		executor.initState(inputs);
		
		handleCinderella(executor.getCurrOutputs());
		isCinderellaTurn = true;
		paint(this.getGraphics());
		Thread.sleep(3000);
		

		while (run) {

			generateRandomWaterFills(inputs);
			isCinderellaTurn = false;
			paint(this.getGraphics());
			Thread.sleep(3000);

			executor.updateState(inputs);
			
			handleCinderella(executor.getCurrOutputs());
			isCinderellaTurn = true;
			paint(this.getGraphics());
			Thread.sleep(3000);
		}
		
		finished = true;
	}
	
	private static void handleCinderella(Map<String, String> outputs) {
		buckets[0] = Integer.parseInt(outputs.get("buckets[0]"));
		buckets[1] = Integer.parseInt(outputs.get("buckets[1]"));
		buckets[2] = Integer.parseInt(outputs.get("buckets[2]"));
		buckets[3] = Integer.parseInt(outputs.get("buckets[3]"));
		buckets[4] = Integer.parseInt(outputs.get("buckets[4]"));
		firstToEmpty = Integer.parseInt(outputs.get("firstToEmpty"));
	}

	private static void generateRandomWaterFills(Map<String, String> inputs) {
		int i = 0;
		int index = 0;

		for (i = 0; i < numberOfBuckets; i++) {
			unitsToFill[i] = 0;
		}
		for (i = 0; i < addedWaterUnits; i++) {
			index = rand.nextInt(numberOfBuckets);
			unitsToFill[index]++;
		}
		
		inputs.put("unitsToFill[0]", new Integer(unitsToFill[0]).toString());
		inputs.put("unitsToFill[1]", new Integer(unitsToFill[1]).toString());
		inputs.put("unitsToFill[2]", new Integer(unitsToFill[2]).toString());
		inputs.put("unitsToFill[3]", new Integer(unitsToFill[3]).toString());
		inputs.put("unitsToFill[4]", new Integer(unitsToFill[4]).toString());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(imgBucket, 400, 100, null);
		g.drawImage(imgBucket, 200, 250, null);
		g.drawImage(imgBucket, 600, 250, null);
		g.drawImage(imgBucket, 270, 450, null);
		g.drawImage(imgBucket, 530, 450, null);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.yellow);
		g.drawString(String.valueOf(buckets[0]) + "/" + String.valueOf(capacity), 450, 150);
		g.drawString(String.valueOf(buckets[4]) + "/" + String.valueOf(capacity), 250, 300);
		g.drawString(String.valueOf(buckets[1]) + "/" + String.valueOf(capacity), 650, 300);
		g.drawString(String.valueOf(buckets[3]) + "/" + String.valueOf(capacity), 320, 500);
		g.drawString(String.valueOf(buckets[2]) + "/" + String.valueOf(capacity), 580, 500);
		g.setColor(Color.red);

		if (isCinderellaTurn) {
			g.drawImage(imgCinderella, 800, 0, null);
			switch (firstToEmpty) {
			case 0:
				g.drawString("=0", 530, 150);
				g.drawString("=0", 730, 300);
				break;
			case 1:
				g.drawString("=0", 730, 300);
				g.drawString("=0", 660, 500);
				break;
			case 2:
				g.drawString("=0", 660, 500);
				g.drawString("=0", 400, 500);
				break;
			case 3:
				g.drawString("=0", 400, 500);
				g.drawString("=0", 330, 300);
				break;
			case 4:
				g.drawString("=0", 330, 300);
				g.drawString("=0", 530, 150);
			}

			for (int i = firstToEmpty; i < firstToEmpty + bucketsToEmpty; i++) {
				buckets[i % numberOfBuckets] = 0;
			}
		} else {
			g.drawImage(imgStepmother, 0, 0, null);
			g.drawString("+" + String.valueOf(unitsToFill[0]), 530, 200);
			g.drawString("+" + String.valueOf(unitsToFill[4]), 330, 300);
			g.drawString("+" + String.valueOf(unitsToFill[1]), 730, 300);
			g.drawString("+" + String.valueOf(unitsToFill[3]), 400, 500);
			g.drawString("+" + String.valueOf(unitsToFill[2]), 660, 500);
		}
	}

	public static void main(String args[]) throws Exception {
		Board check = new Board();
		check.setTitle("Cinderella");
		check.setSize(950, 1000);
		check.setDefaultCloseOperation(EXIT_ON_CLOSE);
		check.setVisible(true);
		check.run();
	}
}