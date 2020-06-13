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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
// general menu side panel representation
class MenuPanel extends JPanel {
	private ManualMenuPanel _manual;
	private ScenarioMenuPanel _scenario;
	private JLabel _header;
	private JLabel _infoLabel;
	private JLabel _infoMoreLabel;
	private JLabel _infoErrorLabel;
	private JLabel _errorLabel;

	public MenuPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.CENTER;
		setPreferredSize(new Dimension(300, 800));
		setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
		this._manual = new ManualMenuPanel();
		this._scenario = new ScenarioMenuPanel();
		this._header = new JLabel("Junction Simulator");
		this._header.setFont(new Font("Arial", Font.BOLD, 20));
		gc.insets = new Insets(0, 20, 0, 20);
		add(this._header, gc);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridy++;
		this._infoLabel = new JLabel("Simulator Status:");
		this._infoMoreLabel = new JLabel(Definitions.REGULAR_MSG);
		this._infoMoreLabel.setForeground(Color.BLUE);
		this._errorLabel = new JLabel("Errors:");
		this._infoErrorLabel = new JLabel(" ");
		this._infoErrorLabel.setForeground(Color.RED);
		add(this._manual, gc);
		gc.gridy++;
		add(this._scenario, gc);
		gc.gridy++;
		add(this._infoLabel, gc);
		gc.gridy++;
		add(this._infoMoreLabel, gc);
		gc.gridy++;
		add(this._errorLabel, gc);
		gc.gridy++;
		add(this._infoErrorLabel, gc);
		// event listener to child's events
		MenuListener menuListener = new MenuListener() {

			@Override
			public void updateInfo(String info) {
				_infoMoreLabel.setText(info);

			}

			@Override
			public void toggleEnableButtons(boolean enable) {
				_manual.setEnableButton(enable);
				_scenario.setEnableButton(enable);
			}

			@Override
			public void updateError(String error) {
				_infoErrorLabel.setText(error);
			}
		};
		this._scenario.setMenuListener(menuListener);
		this._manual.setMenuListener(menuListener);
	}

	public void setCreationListener(CreationListener creationListener) {
		this._manual.setListener(creationListener);
		this._scenario.setListener(creationListener);
	}

	public void updateInfoTextAndButton(boolean setEnable, String infoText) {
		if (setEnable) {
			_manual.setEnableButton(true);
			_scenario.setEnableButton(true);
		}
		this._infoMoreLabel.setText(infoText);
	}

	public void setErrorText(String string) {
		this._infoErrorLabel.setText(string);
	}

	public void enableDemoBtn(boolean enable) {
		_scenario.enableDemoBtn(enable);
		
	}
}
