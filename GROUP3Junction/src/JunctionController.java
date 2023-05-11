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
import java.util.Map;

import tau.smlab.syntech.controller.executor.ControllerExecutor;
import tau.smlab.syntech.games.controller.jits.BasicJitController;


//wrapper class for spectra's controller
class JunctionController {
	JunctionState junctionState = new JunctionState();
	ControllerExecutor ctrlExec;

	public JunctionController() {
		Map<String,String> inputs = new HashMap<>();
		try {
			for (String env : junctionState.getEnvState().keySet()) {
				inputs.put(env, junctionState.getEnvVariable(env));
			}
			ctrlExec = new ControllerExecutor(new BasicJitController(), "out/jit", "JunctionModule");
			ctrlExec.initState(inputs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateState() {
		Map<String,String> inputs = new HashMap<>();
		try {
			// update environment
			for (String env : junctionState.getEnvState().keySet()) {
				inputs.put(env, junctionState.getEnvVariable(env));
			}
			ctrlExec.updateState(inputs); // get new state from spectra
			// update system
			for (String sys : ctrlExec.getCurrOutputs().keySet())
				junctionState.updateSysVariable(sys, ctrlExec.getCurrValue(sys));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

//		if (ctrlExec.reachedEnvDeadlock()) {
//			System.err.println("reachedEnvDeadlock");
//			return;
//		}
	}
	public void free() {
		ctrlExec.free();
	}

	public void updateEnvVariable(String s, String b) {
		this.junctionState.updateEnvVariable(s, b);
	}

	public boolean getSysVariable(String var) {
		return Boolean.parseBoolean(this.junctionState.getSysVariable(var));
	}

	public String getEnvVariable(String relevantLane) {
		return this.junctionState.getEnvVariable(relevantLane);
	}

	public HashMap<String, String> getSysMap() {
		return this.junctionState.getSysState();
	}
}
