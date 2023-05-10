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
	final int x = 8;
	final int y = 8;
	final int dim = 100;
	int[] monkey = new int[] { 0, 0 };
	int[] banana = new int[] { -1, -1 };
	int[] bed = new int[] { 0, 0 };
	int[] tv = new int[] { 2, 2 };
	int[] shower = new int[] { 2, 0 };
	BufferedImage m;

	ControllerExecutor executor;
	Map<String,String> inputs = new HashMap<String, String>();

	public void run() throws Exception {

		executor = new ControllerExecutor(new BasicJitController(), "out/jit", "MonkeyWithBanana");
		m = ImageIO.read(new File("img/monkey.jpg"));

		Random rand = new Random();
		banana[0] = rand.nextInt(8);
		banana[1] = rand.nextInt(8);
		
		inputs.put("banana[0]", Integer.toString(banana[0]));
		inputs.put("banana[1]", Integer.toString(banana[1]));
		executor.initState(inputs);
		
		Map<String, String> sysValues = executor.getCurrOutputs();
		
		monkey[0] = Integer.parseInt(sysValues.get("monkey[0]"));
		monkey[1] = Integer.parseInt(sysValues.get("monkey[1]"));
		
		paint(this.getGraphics());
		Thread.sleep(1000);
		
		while (true) {
			
			if (monkey[0] == banana[0] & monkey[1] == banana[1]) {
				banana[0] = rand.nextInt(8);
				banana[1] = rand.nextInt(8);
			}
			
			inputs.put("banana[0]", Integer.toString(banana[0]));
			inputs.put("banana[1]", Integer.toString(banana[1]));
			
			executor.updateState(inputs);
			
			sysValues = executor.getCurrOutputs();
			
			monkey[0] = Integer.parseInt(sysValues.get("monkey[0]"));
			monkey[1] = Integer.parseInt(sysValues.get("monkey[1]"));
			
			paint(this.getGraphics());
			Thread.sleep(1000);
		}
	}

	@Override
	public void paint(Graphics g) {
		int row;
		int col;

		for (row = 0; row < y; row++) {
			for (col = 0; col < x; col++) {
				if ((row % 2) == (col % 2))
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.LIGHT_GRAY);

				g.fillRect(col * dim, row * dim, dim, dim);
			}
		}

		g.setColor(Color.BLACK);
		g.drawString("BED", bed[0] * dim + 40, bed[1] * dim + 50);
		g.drawString("SHOWER", shower[0] * dim + 20, shower[1] * dim + 50);
		g.drawString("TV", tv[0] * dim + 40, tv[1] * dim + 50);

		if (banana[0] != -1) {
			g.setColor(Color.YELLOW);
			g.fillRect(banana[0] * dim, banana[1] * dim, dim, dim);
			g.setColor(Color.BLACK);
			g.drawString("BANANA", banana[0] * dim + 30, banana[1] * dim + 50);
		}
		if (m != null) {
			g.drawImage(m, monkey[0] * dim, monkey[1] * dim, null);
		} else {
			g.setColor(Color.BLUE);
			g.fillRect(monkey[0] * dim, monkey[1] * dim, dim, dim);
			g.setColor(Color.WHITE);
			g.drawString("MONKEY", monkey[0] * dim + 20, monkey[1] * dim + 50);
		}
	}

	public static void main(String args[]) throws Exception {
		Board check = new Board();
		check.setTitle("monkey");
		check.setSize(check.x * check.dim, check.y * check.dim);
		check.setDefaultCloseOperation(EXIT_ON_CLOSE);
		check.setVisible(true);
		check.run();
	}
}