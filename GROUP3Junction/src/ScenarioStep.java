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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This object represents a single step of a specific scenario 
 * describing the state of each element of the junction.
 */
abstract class ScenarioStep {

	private ScenarioNumber scenario; // scenario ID
	private Map<Integer, Boolean> carsLocation; // cars location in current step
	private int[][] pedsLocation; // peds location in current step
	private boolean isFoggy = false; // is there foggy?
	private boolean isClosedRoad = false; // is there road constructions?
	private boolean hasStarted = false; // did the step start?
	private int extraWait = 0; // does this step need some extra show time?
	private int stepNumber = 0; // the current step number

	public ScenarioStep(ScenarioNumber scenario, int stepNumber) {
		this.scenario = scenario;
		this.carsLocation = new HashMap<>();
		this.pedsLocation = new int[][] {};
		this.isFoggy = false;
		this.isClosedRoad = false;
		this.stepNumber = stepNumber;
	}

	public ScenarioStep(ScenarioNumber scenario, Map<Integer, Boolean> carsLocation, int[][] pedsLocation, boolean isFoggy,
			boolean isClosedRoad) {
		this.scenario = scenario;
		this.carsLocation = carsLocation;
		this.pedsLocation = pedsLocation;
		this.isFoggy = isFoggy;
		this.isClosedRoad = isClosedRoad;
	}

	public void setCarsLocation(Map<Integer, Boolean> carsLocation) {
		this.carsLocation = carsLocation;
	}

	public void setPedsLocation(int[][] pedsLocation) {
		this.pedsLocation = pedsLocation;
	}

	public void setIsFoggy(boolean isFoggy) {
		this.isFoggy = isFoggy;
	}

	public void setIsClosedRoad(boolean isClosedRoad) {
		this.isClosedRoad = isClosedRoad;
	}
	
	public void setExtraWait(int time) {
		if(time > 0)
			this.extraWait = time;
	}
	
	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	public ScenarioNumber getScenarioNumber() {
		return this.scenario;
	}

	public Map<Integer, Boolean> getCarsLocation() {
		return this.carsLocation;
	}

	public int[][] getPedsLocation() {
		return this.pedsLocation;
	}

	public boolean getIsFoggy() {
		return this.isFoggy;
	}

	public boolean getIsClosedRoad() {
		return this.isClosedRoad;
	}

	public String getDescription() {
		return this.scenario.toString();
	}
	
	public int getExtraWait() {
		return this.extraWait;
	}
	
	public int getStepNumber() {
		return stepNumber;
	}

	public boolean HasStarted() {
		return hasStarted;
	}
	
	/*
	 * Because each step has different target it wants to achieve, 
	 * each step defines it's own finishing state in order to let the scheduler know 
	 * when this step is finished and it can run the next step of the played scenario.
	 */
	public abstract boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn);
}
