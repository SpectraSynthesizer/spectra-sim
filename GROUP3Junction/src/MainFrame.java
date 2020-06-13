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

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
class MainFrame extends JFrame {

	private JunctionPanel _junctionPanel;
	private MenuPanel _menuPanel;

	public MainFrame(String appName) {
		super(appName);
		ImageIcon icon = new ImageIcon("src/Images/icon.png");
		this.setIconImage(icon.getImage());
		setLayout(new BorderLayout());
		try {
			this._junctionPanel = new JunctionPanel(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this._menuPanel = new MenuPanel();
		this._menuPanel.setCreationListener(new CreationListener() {

			@Override
			public void createElement(int selection, boolean emergency) {
				if (selection < 100)
					_junctionPanel.createVehicle(selection, emergency);
				else
					_junctionPanel.createPedestrian(selection);

			}

			@Override
			public void createScenario(int selection) {
				_junctionPanel.createScenario(selection);
			}

			@Override
			public void PlayDemo(boolean isDemo) {
				_junctionPanel.playDemo(isDemo);

			}

			@Override
			public void FlipFog(boolean isDemo) {
				_junctionPanel.setFog(isDemo);
			}

			@Override
			public void CloseRoad(boolean isClosed) {
				_junctionPanel.setRoadConstructions(isClosed);
			}
		});

		add(this._menuPanel, BorderLayout.EAST);
		add(this._junctionPanel, BorderLayout.WEST);

		// setPreferredSize(new Dimension(800, 600));
		setSize(1100, 800);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				_junctionPanel.free();
				
			}
		}));
	}
	
	public void updateButtonAndStatus(boolean setEnable,String infoText) {
		this._menuPanel.updateInfoTextAndButton(setEnable,infoText);
	}

	public void setErrorText(String string) {
		this._menuPanel.setErrorText(string);
		
	}

	public void enableDemoBtn(boolean enable) {
		this._menuPanel.enableDemoBtn(enable);
	}
	

}
