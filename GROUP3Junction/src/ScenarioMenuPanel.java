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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class ScenarioMenuPanel extends JPanel implements ActionListener { // scenario menu side panel representation

	JComboBox<String> _selectionList;
	private JButton _runBtn;
	private JButton _demoBtn;
	private boolean _isDemo;
	private JLabel _runScenario;
	private JLabel _randomDemoHeader;
	private int _selection = 0; // dropdown selection index
	private CreationListener _creationListener;
	private MenuListener _menuListener;

	public ScenarioMenuPanel() {
		String[] scenarioStrings = { "Choose Scenario", "Scenario 1", "Scenario 2", "Scenario 3", "Scenario 4",
				"Scenario 5", "Scenario 6", "Scenario 7", "Scenario 8", "Scenario 9", "Scenario 10" };
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.CENTER;
		this._runBtn = new JButton("Run");
		this._runBtn.addActionListener(this);
		this._runBtn.setEnabled(false);
		this._demoBtn = new JButton("Run Demo");
		this._demoBtn.setToolTipText("Run/stop randomiezed environment behaviour");
		this._demoBtn.addActionListener(this);
		this._selectionList = new JComboBox<String>(scenarioStrings);
		// add tooltip for dropdown for scenario descriptions
		ComboboxToolTipRenderer renderer = new ComboboxToolTipRenderer();
		List<String> tooltips = new ArrayList<String>();
		tooltips.add("");
		for (int i = 1; i <= 10; i++) {
			tooltips.add(ScenarioNumber.getScenarioNumber(i).toString());
		}
		renderer.setTooltips(tooltips);
		this._selectionList.setRenderer(renderer);
		// event listener for scenario dropdown selection
		ActionListener comboBoxListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) _selectionList.getSelectedItem();
				_runBtn.setEnabled(true);
				switch (s) {
				case "Choose Scenario":
					_runBtn.setEnabled(false);
					_selection = 0;
					break;
				case "Scenario 1":
					_selection = 1;
					break;
				case "Scenario 2":
					_selection = 2;
					break;
				case "Scenario 3":
					_selection = 3;
					break;
				case "Scenario 4":
					_selection = 4;
					break;
				case "Scenario 5":
					_selection = 5;
					break;
				case "Scenario 6":
					_selection = 6;
					break;
				case "Scenario 7":
					_selection = 7;
					break;
				case "Scenario 8":
					_selection = 8;
					break;
				case "Scenario 9":
					_selection = 9;
					break;
				case "Scenario 10":
					_selection = 10;
					break;
				}
			}
		};

		gc.insets = new Insets(20, 0, 0, 0);
		this._runScenario = new JLabel("Run Scenario Or Demo:");
		add(this._runScenario, gc);
		gc.gridy++;
		_selectionList.addActionListener(comboBoxListener);
		add(this._selectionList, gc);
		gc.gridy++;
		add(this._runBtn, gc);
		gc.gridy++;
		this._randomDemoHeader = new JLabel("Full Randomized Demo:");
		add(this._randomDemoHeader, gc);
		gc.gridy++;
		add(this._demoBtn, gc);

	}

	@Override
	// event listener for scenario panel buttons
	public void actionPerformed(ActionEvent e) {
		_menuListener.updateError(" ");
		if ((JButton) e.getSource() == this._runBtn) { // run scenario
			this._creationListener.createScenario(this._selection);
			_menuListener.toggleEnableButtons(false);
		} else if ((JButton) e.getSource() == this._demoBtn) { // run demo
			this._isDemo = !this._isDemo;
			_demoBtn.setText((this._isDemo ? "Stop" : "Start") + " Demo");
			_creationListener.PlayDemo(this._isDemo);
			_menuListener.toggleEnableButtons(!this._isDemo);
		}
	}

	public void setListener(CreationListener creationListener) {
		this._creationListener = creationListener;
	}

	public void setMenuListener(MenuListener menuListener) {
		this._menuListener = menuListener;
	}

	// enable or disable buttons on this panel
	public void setEnableButton(boolean enable) {
		if (this._selectionList.getSelectedIndex() != 0) {
			this._runBtn.setEnabled(enable);
		}
		this._selectionList.setEnabled(enable);
		if (!this._isDemo)
			this._demoBtn.setEnabled(enable);
	}

	public void enableDemoBtn(boolean enable) {
		this._demoBtn.setEnabled(enable);
	}
	

}
