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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
class ManualMenuPanel extends JPanel implements ActionListener {
	private JToggleButton _carBtn;
	private JToggleButton _pedBtn;
	private JToggleButton _emergencyBtn;
	private JButton _addBtn;
	private JLabel _addPedCarHeader;
	private JLabel _addFogCloseRoadHeader;
	private JButton _fogBtn;
	private JButton _constructionBtn;
	private JComboBox<String> _selectionList;
	private DefaultComboBoxModel<String> _carList;
	private DefaultComboBoxModel<String> _pedList;
	private int _selection = 0;
	private boolean _isEmergency = false;
	private CreationListener _creationListener;
	private boolean _isFoggy = false;
	private boolean _isClosed = false;
	private MenuListener _menuListener;

	public ManualMenuPanel() {
		// all possibe directions for pedestrians
		String[] pedStrings = { "Random Pedestrian", "North left", "North right", "East up", "East down",
				"West up", "West down", "South left", "South right" };
		// all possibe directions for cars
		String[] carStrings = { "Random Vehicle", "From North to East", "From North to South", "From North to West",
				"From East to North", "From East to West", "From East to South", "From South to East",
				"From South to North", "From South to West", "From West to South", "From West to East",
				"From West to North" };
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.CENTER;
		setPreferredSize(new Dimension(250, 300));
		// event listener for manual panel buttons
		ActionListener buttonsListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton clicked = (JButton) e.getSource();
				_menuListener.updateError(" ");
				if (clicked == _addBtn) { // add vehicle / pedestrian
					_creationListener.createElement(_selection, _isEmergency);
					_selectionList.setSelectedIndex(0);
				} else if (clicked == _fogBtn) { // add fog
					_isFoggy = !_isFoggy;
					_fogBtn.setText((_isFoggy ? "Stop" : "Start") + " Fog");
					_creationListener.FlipFog(_isFoggy);
				} else if (clicked == _constructionBtn) { // add road contruction
					_isClosed = !_isClosed;
					_constructionBtn.setText((_isClosed ? "Open" : "Close") + " Road");
					_creationListener.CloseRoad(_isClosed);
				}

			}
		};
		this._carBtn = new JToggleButton("Car");
		this._carBtn.addActionListener(this);
		this._carBtn.setSelected(true);
		this._emergencyBtn = new JToggleButton("Emergency");
		this._emergencyBtn.addActionListener(this);
		this._pedBtn = new JToggleButton("Pedestrian");
		this._pedBtn.addActionListener(this);
		this._fogBtn = new JButton("Start Fog");
		this._fogBtn.setToolTipText("Close all roads after junction is clear");
		this._fogBtn.addActionListener(buttonsListener);
		this._constructionBtn = new JButton("Close Road");
		this._constructionBtn.setToolTipText("Close northern road for construction after relevant lanes are clear");
		this._constructionBtn.addActionListener(buttonsListener);
		ButtonGroup bg = new ButtonGroup();
		this._addPedCarHeader = new JLabel("Add Vehicle Or Pedestrian:");
		gc.gridwidth = 3;
		add(this._addPedCarHeader, gc);
		gc.gridwidth = 1;
		gc.gridy++;
		gc.insets = new Insets(20, 0, 0, 0);
		bg.add(this._carBtn);
		bg.add(this._pedBtn);
		bg.add(this._emergencyBtn);
		this._addBtn = new JButton("Add");
		this._addBtn.addActionListener(buttonsListener);
		add(_carBtn, gc);
		gc.gridx++;
		add(_emergencyBtn, gc);
		gc.gridx++;
		add(_pedBtn, gc);
		gc.gridx = 0;
		gc.gridy++;

		this._selectionList = new JComboBox<String>(carStrings);
		this._carList = new DefaultComboBoxModel<String>(carStrings);
		this._pedList = new DefaultComboBoxModel<String>(pedStrings);
		ActionListener comboBoxListener = new ActionListener() {

			@Override
			// event listener for scenario dropdown selection
			// string are mapped to distinguished values
			public void actionPerformed(ActionEvent e) {
				String s = (String) _selectionList.getSelectedItem();
				switch (s) {
				case "Random Vehicle":
					_selection = -1;
					break;
				case "From North to East":
					_selection = 2;
					break;
				case "From North to South":
					_selection = 1;
					break;
				case "From North to West":
					_selection = 0;
					break;
				case "From East to North":
					_selection = 10;
					break;
				case "From East to West":
					_selection = 11;
					break;
				case "From East to South":
					_selection = 12;
					break;
				case "From South to East":
					_selection = 20;
					break;
				case "From South to North":
					_selection = 21;
					break;
				case "From South to West":
					_selection = 22;
					break;
				case "From West to South":
					_selection = 30;
					break;
				case "From West to East":
					_selection = 31;
					break;
				case "From West to North":
					_selection = 32;
					break;
				case "Random Pedestrian":
					_selection = 100;
					break;
				case "North left":
					_selection = 101;
					break;
				case "North right":
					_selection = 102;
					break;
				case "East up":
					_selection = 103;
					break;
				case "East down":
					_selection = 104;
					break;
				case "West up":
					_selection = 105;
					break;
				case "West down":
					_selection = 106;
					break;
				case "South left":
					_selection = 107;
					break;
				case "South right":
					_selection = 108;
					break;
				}

			}
		};
		_selectionList.addActionListener(comboBoxListener);
		gc.gridwidth = 3;
		add(this._selectionList, gc);
		gc.gridy++;
		add(this._addBtn, gc);
		gc.gridy++;
		this._addFogCloseRoadHeader = new JLabel("Add Fog Or Close Road:");
		add(this._addFogCloseRoadHeader, gc);
		gc.gridy++;
		add(_fogBtn, gc);
		gc.gridy++;
		add(_constructionBtn, gc);

	}

	@Override
	// event listener for setting the soon to be injected element
	public void actionPerformed(ActionEvent e) {
		JToggleButton clicked = (JToggleButton) e.getSource();
		if (clicked == this._carBtn) {
			this._selectionList.setModel(this._carList);
			this._selectionList.setSelectedIndex(0);
			this._isEmergency = false;
		} else if (clicked == this._pedBtn) {
			this._selectionList.setModel(this._pedList);
			this._selectionList.setSelectedIndex(0);
		} else if (clicked == this._emergencyBtn) {
			this._selectionList.setModel(this._carList);
			this._selectionList.setSelectedIndex(0);
			this._isEmergency = true;
		}

	}

	public void setListener(CreationListener creationListener) {
		this._creationListener = creationListener;

	}

	// enable or disable buttons on this panel
	public void setEnableButton(boolean enable) {
		this._addBtn.setEnabled(enable);
		this._fogBtn.setEnabled(enable);
		this._carBtn.setEnabled(enable);
		this._pedBtn.setEnabled(enable);
		this._emergencyBtn.setEnabled(enable);
		this._selectionList.setEnabled(enable);
		this._constructionBtn.setEnabled(enable);
		this._fogBtn.setText("Start Fog");
		this._constructionBtn.setText("Close Road");

	}

	public void setMenuListener(MenuListener menuListener) {
		this._menuListener = menuListener;

	}

}
