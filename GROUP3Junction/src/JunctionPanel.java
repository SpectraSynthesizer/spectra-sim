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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class JunctionPanel extends BackgroundPanel implements ActionListener {
	private MainFrame _parentFrame; // holds the main frame for communication with the menu panel
	private JunctionController controller = new JunctionController();
	private List<Car> _cars; // List of the vehicles on the screen
	private List<Pedestrian> _pedestrians; // List of the pedestrians on the screen
	private CarTrafficLight[] _carsTrafficLights = new CarTrafficLight[4];
	private PedestrianTrafficLight[] _pedsTrafficLights = new PedestrianTrafficLight[8];
	private JunctionElement _fog; // fog image
	private JunctionElement _closedRoad; // closed road image
	private JunctionElement _loading; // image for waiting sign
	private HashMap<Integer, VehicleOptions> _carsInLanes = new HashMap<>(); // Map that indicates whether a lane is occupied
	private Timer _timer;
	private DemoMode _isDemo = DemoMode.NONE; // demo state
	private int _iteration = 0;
	private int _pauseTime = 0;
	private int _pauseScenario = 20;
	private boolean _isFogAction = false;
	private boolean _isClosedRoadAction = false;
	private Queue<ScenarioStep> _currentScenario;

	public JunctionPanel(MainFrame parentFrame) throws IOException {
		super(ImageIO.read(new File("src/Images/junction800.png")), SCALED);
		this._parentFrame = parentFrame;
		setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
		setPreferredSize(new Dimension(800, 800));
		this._pedestrians = new ArrayList<Pedestrian>();
		this._cars = new ArrayList<Car>();
		this._timer = new Timer(100, this);
		this._fog = new JunctionElement(0, 0);
		this._fog.setVisible(false);
		this._fog.loadImage("src/Images/fog.png");
		this._closedRoad = new JunctionElement(435, 13);
		this._closedRoad.loadImage("src/Images/blocked.png");
		this._loading = new JunctionElement(280, 360);
		this._loading.loadImage("src/Images/ClearWait.png");
		this._loading.setVisible(false);
		initTrafficLights();
		this._timer.start();
	}

	private void initTrafficLights() {
		// car traffic lights
		this._carsTrafficLights[0] = new CarTrafficLight(274, 194, 0); // north
		this._carsTrafficLights[1] = new CarTrafficLight(575, 267, 90); // east
		this._carsTrafficLights[2] = new CarTrafficLight(497, 575, 180); // south
		this._carsTrafficLights[3] = new CarTrafficLight(195, 498, 270); // west
		// pedestrian traffic lights
		this._pedsTrafficLights[0] = new PedestrianTrafficLight(385, 185, 0, true); // top middle
		this._pedsTrafficLights[1] = new PedestrianTrafficLight(588, 185); // top right
		this._pedsTrafficLights[2] = new PedestrianTrafficLight(588, 385, 270, true); // right middle
		this._pedsTrafficLights[3] = new PedestrianTrafficLight(588, 588); // bottom right
		this._pedsTrafficLights[4] = new PedestrianTrafficLight(385, 588, 180, true); // bottom middle
		this._pedsTrafficLights[5] = new PedestrianTrafficLight(185, 588); // bottom left
		this._pedsTrafficLights[6] = new PedestrianTrafficLight(185, 385, 90, true); // left middle
		this._pedsTrafficLights[7] = new PedestrianTrafficLight(185, 185); // top left
	}

	public void createPedestrian(int selection) {
		int fixedSelection = selection - 101;
		int index;
		if (fixedSelection == -1) { // Creating a pedestrian in a random position
			Random r = new Random();
			index = r.nextInt(Definitions.possiblePeds.size());
		} else { // Creating a pedestrian in a user provided position
			index = fixedSelection;
		}
		ArrayList<Integer> selectedPed = Definitions.possiblePeds.get(index);
		this._pedestrians.add(new Pedestrian(selectedPed.get(0), selectedPed.get(1), selectedPed.get(2))); // Add pedestrian to the list
		repaint();
	}

	public void createVehicle(int selection, boolean emergency) {
		ArrayList<Integer> possibleLanes = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 10, 11, 12, 20, 21, 22, 30, 31, 32)); // All possible lanes for cars (Lanecode formatted)
		ArrayList<Integer> carValues;
		if (selection == -1) { // Create a car in random position, keep try until or junction is full
			Collections.shuffle(possibleLanes);
			int index;
			do {
				index = possibleLanes.remove(0);
				carValues = Definitions.possibleCars.get(index);
			} while (!possibleLanes.isEmpty() && !createCar(carValues.get(0), carValues.get(1), carValues.get(2), carValues.get(3), emergency, true));
			if (possibleLanes.isEmpty())
				this._parentFrame.setErrorText("Junction is full - please wait");
		} else { // Create a car in user provided position
			carValues = Definitions.possibleCars.get(selection);
			createCar(carValues.get(0), carValues.get(1), carValues.get(2), carValues.get(3), emergency, false);
		}
		repaint();
	}

	/* Creates the actual car instance, if failed return false */
	public boolean createCar(int x, int y, int deg, int dir, boolean isEmergency, boolean random) {
		Car car = new Car(x, y, deg, CarDirection.getDirection(dir), isEmergency);
		if (checkOrUpdateLane(car, true, true)) {
			if (this._isDemo == DemoMode.NONE && !random && this._currentScenario == null)
				this._parentFrame.setErrorText("Lane is occupied - try again");
			return false;
		}
		if (isEmergency) {
			if (isCollidingEmergency(car)) { // assumption - no emergency on colliding lanes
				if (this._isDemo == DemoMode.NONE && !random && this._currentScenario == null)
					this._parentFrame.setErrorText("Cannot add coliding emergency vehicles");
				return false;
			}
		}
		checkOrUpdateLane(car, true, false); // update that there is a car in this lane
		this._cars.add(car); // Add car to the cars list
		return true;
	}

	/* Checks or update whether there is a car in a lane */
	private boolean checkOrUpdateLane(Car car, boolean val, boolean check) {
		if (check) { // check if there is already car in lane
			return this._carsInLanes.containsKey(car.getLaneCode());
		}
		if (val) { // update the car in the current lane
			this._carsInLanes.put(car.getLaneCode(), car.isEmergency());
		} else {
			this._carsInLanes.remove(car.getLaneCode());
		}
		return false;
	}

	/* Checks if there are emergency vehicles in colliding lanes */
	private boolean isCollidingEmergency(Car car) {
		int relevantLane = car.getLaneCode();
		if (relevantLane % 10 == 0) // for right turns - they never collide
			return false;
		for (int lane : Definitions.emergencyCollidingList.get(relevantLane)) {
			if (this._carsInLanes.getOrDefault(lane, VehicleOptions.NONE) == VehicleOptions.EMERGENCY)
				return true;
		}
		return false;
	}

	/* custom painting method for drawing relevant elements on the screen */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);

		Toolkit.getDefaultToolkit().sync();
	}

	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		for (Pedestrian pedestrian : this._pedestrians) { // paint the updated pedestrians
			g2d.drawImage(pedestrian.getImage(), pedestrian.getX(), pedestrian.getY(), this);
		}
		for (Car car : this._cars) { // paint the updated cars
			g2d.drawImage(car.getImage(), car.getX(), car.getY(), this);
		}
		for (CarTrafficLight trafficLight : this._carsTrafficLights) { // paint updated cars traffic lights
			g2d.drawImage(trafficLight.getImage(), trafficLight.getX(), trafficLight.getY(), this);
		}
		for (PedestrianTrafficLight trafficLight : this._pedsTrafficLights) { // paint updated pedestrians traffic lights
			g2d.drawImage(trafficLight.getImage(), trafficLight.getX(), trafficLight.getY(), this);
		}
		if (displayRoadConstructions()) { // paint road construction image if needed
			g2d.drawImage(this._closedRoad.getImage(), this._closedRoad.getX(), this._closedRoad.getY(), this);
		}
		if (Boolean.parseBoolean(controller.getEnvVariable("foggy"))) { // paint fog image if needed
			g2d.drawImage(this._fog.getImage(), this._fog.getX(), this._fog.getY(), this);
		}
		if (this._loading.isVisible()) { // paint "waiting for junction to clear" image
			g2d.drawImage(this._loading.getImage(), this._loading.getX(), this._loading.getY(), this);
		}
	}

	/* this method is being called every 25ms and updates the elements state */
	@Override
	public void actionPerformed(ActionEvent e) {
		this._pauseScenario++;
		this._pauseTime = this._pauseTime == 10 ? 0 : this._pauseTime + 1;
		updateDemo();
		updateScenario();
		updateCars();
		updatePedestrians();
		updateFog();
		repaint();
		// get a new state from the controller if no cars or pedestrians are crossing
		if (!isCarsCrossing() && !isPedsCrossing()) {
			if (this._cars.size() + this._pedestrians.size() != 0 || this._pauseTime == 10)
				getNewState();
		}

	}


	/* controlling current scenario if needed */
	public void updateScenario() {
		if (this._pauseScenario >= (20 + (_currentScenario == null ? 0 : _currentScenario.peek().getExtraWait()))) { // adds an extra time between scenario steps
			updateScenarioStep();
			_pauseScenario = 0;
		}
	}

	public void updateScenarioStep() {
		if (this._currentScenario == null)
			return;
		ScenarioStep currentStep = this._currentScenario.peek();
		if (!currentStep.HasStarted()) {
			if ((currentStep.getStepNumber() + this._cars.size() + this._pedestrians.size() == 0) || currentStep.getStepNumber() > 0) {
				if (currentStep.getStepNumber() == 0) { // scenario started
					fastForwardMode(false);
					this._parentFrame.updateButtonAndStatus(false, "Running scenario " + currentStep.getScenarioNumber().getNumber() + "..."); // disable buttons
				}
				this._loading.setVisible(false); // remove loading image
				// draw scenario step to screen
				for (Map.Entry<Integer, Boolean> vehicle : currentStep.getCarsLocation().entrySet()) {
					ArrayList<Integer> carValues = Definitions.possibleCars.get(vehicle.getKey());
					createCar(carValues.get(0), carValues.get(1), carValues.get(2), carValues.get(3), vehicle.getValue(), false);
				}
				int[][] ped = currentStep.getPedsLocation();
				for (int i = 0; i < ped.length; i++) {
					this._pedestrians.add(new Pedestrian(ped[i][0], ped[i][1], ped[i][2]));
				}
				setFog(currentStep.getIsFoggy());
				setRoadConstructions(currentStep.getIsClosedRoad());
				currentStep.setHasStarted(true);
			} else { // if junction isn't clear, display loading image and fast forward
				fastForwardMode(true);
				setFog(false);
				setRoadConstructions(false);
				this._parentFrame.updateButtonAndStatus(false, Definitions.CLEAR_MSG);
				this._loading.setVisible(true);
			}

		} else if (currentStep.isFinished(_cars, _pedestrians, isFogAction(), isClosedRoadAction())) { // step finished, get the next step
			this._currentScenario.poll();
			if (this._currentScenario.isEmpty()) { // no more steps
				this._currentScenario = null;
				this._parentFrame.updateButtonAndStatus(true, Definitions.REGULAR_MSG);
			}
		}
	}

	private void updateDemo() { // insert 3 random elements for demo purposes
		if (this._isDemo == DemoMode.RUNNING) {
			if (this._iteration == 0) { // insert element every 10 seconds
				if (this._pauseTime < 5) {
					insertRandomElement();
					insertRandomElement();
					insertRandomElement();
				}
			}
			this._iteration = this._iteration == 1000 ? 0 : this._iteration + 25;
		} else if (this._isDemo == DemoMode.WAITING) { // wait for junction to clear
			if (this._cars.size() + this._pedestrians.size() == 0) {
				this._pauseTime = 0;
				this._isDemo = DemoMode.RUNNING;
				this._parentFrame.updateButtonAndStatus(false, "Running Demo..."); // disable menu buttons and update info
				this._loading.setVisible(false);
				this._parentFrame.enableDemoBtn(true);
			}
		}

	}

	private void insertRandomElement() {
		double chance = Math.random();
		if (chance < 0.2) { // create emergency;
			createVehicle(-1, true);
		} else if (chance > 0.7) {
			createPedestrian(100);
		} else {
			createVehicle(-1, false);
		}
	}

	public void setRoadConstructions(boolean closed) {
		controller.updateEnvVariable("roadConstructions", String.valueOf(closed)); // update spectra for env road construction
		if (this._currentScenario == null) {
			if (closed && !displayRoadConstructions() && !constructionsLanesAreClear(true, true))
				this._parentFrame.updateButtonAndStatus(false, "Closing road after relevant lanes are clear...");
			else
				this._parentFrame.updateButtonAndStatus(false, Definitions.REGULAR_MSG);
		}
	}
	
	private boolean constructionsLanesAreClear(boolean init, boolean ignoreState) {
		boolean anyCarInRoadClosing = init;
		HashSet<Integer> constructionLanes = new HashSet<Integer>(Arrays.asList(10, 21, 32));
		for (Car car : this._cars) {
			if (constructionLanes.contains(car.getLaneCode()) && (ignoreState || car.getCarState() == CarState.FINISHED))
				anyCarInRoadClosing = false;
		}
		return anyCarInRoadClosing;
	}

	private boolean displayRoadConstructions() { // check whether we need to display the construction image
		boolean isClosedRoadAction = isClosedRoadAction();
		if (!isClosedRoadAction) {
			_isClosedRoadAction = false;
			return false;
		}
		boolean anyCarInRoadClosing = constructionsLanesAreClear(isClosedRoadAction, false);
		if (_isClosedRoadAction != anyCarInRoadClosing) {
			_isClosedRoadAction = anyCarInRoadClosing;
			if (anyCarInRoadClosing && this._currentScenario == null)
				this._parentFrame.updateButtonAndStatus(false, Definitions.REGULAR_MSG);
		}
		return anyCarInRoadClosing;
	}

	/* checks whether a pedestrian is crossing */
	private boolean isPedsCrossing() {
		for (Pedestrian p : _pedestrians) {
			if (p.getPedestrianState() == PedestrianState.CROSSING_FIRST || p.getPedestrianState() == PedestrianState.CROSSING_SECOND)
				return true;
		}
		return false;
	}

	/* checks whether a car is crossing */
	private boolean isCarsCrossing() {
		for (Car car : _cars) {
			if (car.getCarState() == CarState.CROSSING)
				return true;
		}
		return false;
	}

	/* update the GUI traffic lights state with the state that we got from the spectra controller */
	private void getNewState() {
		controller.updateState();
		_carsTrafficLights[0].updateState(controller.getSysVariable("greenNorthVehicles[2]"), controller.getSysVariable("greenNorthVehicles[1]"), controller.getSysVariable("greenNorthVehicles[0]"));
		_carsTrafficLights[1].updateState(controller.getSysVariable("greenEastVehicles[2]"), controller.getSysVariable("greenEastVehicles[1]"), controller.getSysVariable("greenEastVehicles[0]"));
		_carsTrafficLights[2].updateState(controller.getSysVariable("greenSouthVehicles[2]"), controller.getSysVariable("greenSouthVehicles[1]"), controller.getSysVariable("greenSouthVehicles[0]"));
		_carsTrafficLights[3].updateState(controller.getSysVariable("greenWestVehicles[2]"), controller.getSysVariable("greenWestVehicles[1]"), controller.getSysVariable("greenWestVehicles[0]"));
		_pedsTrafficLights[0].updateState(false, false, controller.getSysVariable("greenNorthPedestrians[0]"), controller.getSysVariable("greenNorthPedestrians[1]"));
		_pedsTrafficLights[1].updateState(false, controller.getSysVariable("greenEastPedestrians[0]"), controller.getSysVariable("greenNorthPedestrians[1]"), false);
		_pedsTrafficLights[2].updateState(controller.getSysVariable("greenEastPedestrians[0]"), controller.getSysVariable("greenEastPedestrians[1]"), false, false);
		_pedsTrafficLights[3].updateState(controller.getSysVariable("greenEastPedestrians[1]"), false, controller.getSysVariable("greenSouthPedestrians[1]"), false);
		_pedsTrafficLights[4].updateState(false, false, controller.getSysVariable("greenSouthPedestrians[0]"), controller.getSysVariable("greenSouthPedestrians[1]"));
		_pedsTrafficLights[5].updateState(controller.getSysVariable("greenWestPedestrians[1]"), false, false, controller.getSysVariable("greenSouthPedestrians[0]"));
		_pedsTrafficLights[6].updateState(controller.getSysVariable("greenWestPedestrians[0]"), controller.getSysVariable("greenWestPedestrians[1]"), false, false);
		_pedsTrafficLights[7].updateState(false, controller.getSysVariable("greenWestPedestrians[0]"), false, controller.getSysVariable("greenNorthPedestrians[0]"));
	}

	private void updateCars() {
		for (int i = 0; i < this._cars.size(); i++) {
			Car car = this._cars.get(i);
			if (car.isVisible()) { // car is inside the screen
				if (!car.needsToStop(controller.getSysVariable(car.getRelevantSpectraVariable(false)))) { // don't need to stop
					if (car.getCarState() == CarState.WAITING) {
						car.setCarState(CarState.CROSSING); // update car state
					} else if (car.getCarState() == CarState.CROSSING) {
						if (car.hasCrossed()) { // if car is out of the center of a junction, update the controller env to false and mark lane as closed
							checkOrUpdateLane(car, false, false);
							controller.updateEnvVariable(car.getRelevantSpectraVariable(true), "NONE");
						}
					}
					car.move(); // increase car position on screen
				} else { // stop for red light
					controller.updateEnvVariable(car.getRelevantSpectraVariable(true), car.isEmergency().name());
				}
			} else { // car out of screen, remove from list
				this._cars.remove(i);
			}

		}
	}

	private void updatePedestrians() {
		for (int i = 0; i < this._pedestrians.size(); i++) {
			Pedestrian ped = this._pedestrians.get(i);
			if (ped.isVisible()) { // if pedestrian on screen
				if (!ped.needsToStop(controller.getSysVariable(ped.getRelevantSpectraVariable(false)))) {
					if (ped.getPedestrianState() == PedestrianState.WAITING_FIRST) { // crossed first section of road
						controller.updateEnvVariable(ped.getRelevantSpectraVariable(true), "false"); // update spectra env to false
						ped.setPedestrianState(PedestrianState.CROSSING_FIRST);
					} else if (ped.getPedestrianState() == PedestrianState.WAITING_SECOND) { // crossing second section of road
						ped.setPedestrianState(PedestrianState.CROSSING_SECOND);
					} else if (ped.getPedestrianState() == PedestrianState.CROSSING_SECOND) {
						if (ped.hasCrossed()) { // if car is out of the center of a junction, update the controller env to false and mark lane as closed
							controller.updateEnvVariable(ped.getRelevantSpectraVariable(true), "false");
						}
					}
					ped.move(); // increase position on screen
				} else { // stopped on red light, update spectra variable
					controller.updateEnvVariable(ped.getRelevantSpectraVariable(true), "true");
				}
			} else { // pedestrian is out of the screen, remove from list and update spectra
				this._pedestrians.remove(i);
			}
		}
	}

	/* doubles the speed of the elements */
	private void fastForwardMode(boolean enableFastForward) {
		for (Car car : this._cars)
			car.changeSpeed(enableFastForward);
		for (Pedestrian ped : this._pedestrians)
			ped.changeSpeed(enableFastForward);

	}

	public void playDemo(boolean isDemo) {
		if (isDemo) { // waiting for junction to clear
			fastForwardMode(true);
			setFog(false);
			setRoadConstructions(false);
			this._parentFrame.updateButtonAndStatus(false, Definitions.CLEAR_MSG);
			this._loading.setVisible(true);
			this._isDemo = DemoMode.WAITING;
			this._parentFrame.enableDemoBtn(false);
		} else { // disable demo mode
			this._isDemo = DemoMode.NONE;
			this._parentFrame.updateButtonAndStatus(true, Definitions.REGULAR_MSG);
		}
	}

	public void createScenario(int scenario) {
		if (scenario == 0) {
			return;
		}
		this._currentScenario = ScenarioManager.getScenario(ScenarioNumber.getScenarioNumber(scenario));
	}

	public void setFog(boolean isFoggy) {
		this._fog.setVisible(isFoggy);
		controller.updateEnvVariable("foggy", String.valueOf(isFoggy)); // update spectra env variable
		if (this._currentScenario == null) {
			if (isFoggy && this._cars.size() != 0)
				this._parentFrame.updateButtonAndStatus(false, "Fog policy will start when junction is clear...");
			else
				this._parentFrame.updateButtonAndStatus(false, Definitions.REGULAR_MSG);
		}
	}

	public void updateFog() {
		if (this._fog.isVisible()) { // controls the fog movement
			this._fog._x--;
			if (this._fog.getX() == -1600)
				this._fog._x = 0;
			boolean isFogAction = isFogAction();
			if (_isFogAction != isFogAction) {
				_isFogAction = isFogAction;
				if (isFogAction && this._currentScenario == null)
					this._parentFrame.updateButtonAndStatus(false, Definitions.REGULAR_MSG);
			}
		}
	}

	private boolean isFogAction() {
		return controller.getSysVariable("fogAction"); // get fog system variable from spectra
	}

	private boolean isClosedRoadAction() {
		return controller.getSysVariable("closedRoadAction"); // get closed road system variable from spectra
	}
	
	public void free() {
		this.controller.free();
	}
}
