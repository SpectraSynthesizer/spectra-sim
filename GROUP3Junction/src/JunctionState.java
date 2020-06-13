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

// class that holds current junction's state
class JunctionState {
	HashMap<String, String> stateSysMap = new HashMap<>();
	HashMap<String, String> stateEnvMap = new HashMap<>();

	public JunctionState() {
		// car sensors
		stateEnvMap.put("vehiclesNorth[0]", "NONE");
		stateEnvMap.put("vehiclesNorth[1]", "NONE");
		stateEnvMap.put("vehiclesNorth[2]", "NONE");
		stateEnvMap.put("vehiclesSouth[0]", "NONE");
		stateEnvMap.put("vehiclesSouth[1]", "NONE");
		stateEnvMap.put("vehiclesSouth[2]", "NONE");
		stateEnvMap.put("vehiclesEast[0]", "NONE");
		stateEnvMap.put("vehiclesEast[1]", "NONE");
		stateEnvMap.put("vehiclesEast[2]", "NONE");
		stateEnvMap.put("vehiclesWest[0]", "NONE");
		stateEnvMap.put("vehiclesWest[1]", "NONE");
		stateEnvMap.put("vehiclesWest[2]", "NONE");
		// pedestrians buttons
		stateEnvMap.put("pedestriansNorthPressed[0]", "false");
		stateEnvMap.put("pedestriansNorthPressed[1]", "false");
		stateEnvMap.put("pedestriansSouthPressed[0]", "false");
		stateEnvMap.put("pedestriansSouthPressed[1]", "false");
		stateEnvMap.put("pedestriansEastPressed[0]", "false");
		stateEnvMap.put("pedestriansEastPressed[1]", "false");
		stateEnvMap.put("pedestriansWestPressed[0]", "false");
		stateEnvMap.put("pedestriansWestPressed[1]", "false");
		// cars lights
		stateSysMap.put("greenNorthVehicles[0]", "false");
		stateSysMap.put("greenNorthVehicles[1]", "false");
		stateSysMap.put("greenNorthVehicles[2]", "false");
		stateSysMap.put("greenSouthVehicles[0]", "false");
		stateSysMap.put("greenSouthVehicles[1]", "false");
		stateSysMap.put("greenSouthVehicles[2]", "false");
		stateSysMap.put("greenEastVehicles[0]", "false");
		stateSysMap.put("greenEastVehicles[1]", "false");
		stateSysMap.put("greenEastVehicles[2]", "false");
		stateSysMap.put("greenWestVehicles[0]", "false");
		stateSysMap.put("greenWestVehicles[1]", "false");
		stateSysMap.put("greenWestVehicles[2]", "false");
		// pedestrian lights
		stateSysMap.put("greenNorthPedestrians[0]", "false");
		stateSysMap.put("greenNorthPedestrians[1]", "false");
		stateSysMap.put("greenSouthPedestrians[0]", "false");
		stateSysMap.put("greenSouthPedestrians[1]", "false");
		stateSysMap.put("greenEastPedestrians[0]", "false");
		stateSysMap.put("greenEastPedestrians[1]", "false");
		stateSysMap.put("greenWestPedestrians[0]", "false");
		stateSysMap.put("greenWestPedestrians[1]", "false");
		// fog & closed roads
		// env
		stateEnvMap.put("foggy", "false");
		stateEnvMap.put("roadConstructions", "false");
		// sys
		stateSysMap.put("fogAction", "false");
		stateSysMap.put("closedRoadAction", "false");

	}

	public HashMap<String, String> getEnvState() {
		return stateEnvMap;
	}

	public HashMap<String, String> getSysState() {
		return stateSysMap;
	}

	public void updateEnvVariable(String s, String b) {
		if (this.stateEnvMap.containsKey(s))
			this.stateEnvMap.put(s, b);
	}

	public void updateSysVariable(String s, String b) {
		if (this.stateSysMap.containsKey(s))
			this.stateSysMap.put(s, b);
	}

	public String getEnvVariable(String var) {
		if (this.stateEnvMap.containsKey(var))
			return this.stateEnvMap.get(var);
		return "false";
	}

	public String getSysVariable(String var) {
		if (this.stateSysMap.containsKey(var))
			return this.stateSysMap.get(var);
		return "false";
	}
}
