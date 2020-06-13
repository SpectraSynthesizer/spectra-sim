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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * a class for most definitions of this project 
 */
final class Definitions {
	public static final String REGULAR_MSG = "Manual mode, waiting for input..."; 
	public static final String CLEAR_MSG = "Waiting for junction to clear..."; 
	public static HashMap<Integer, ArrayList<Integer>> emergencyCollidingList = new HashMap<Integer, ArrayList<Integer>>();
	public static HashMap<Integer, ArrayList<Integer>> possibleCars = new HashMap<Integer, ArrayList<Integer>>();
	public static HashMap<Integer, ArrayList<Integer>> possiblePeds = new HashMap<Integer, ArrayList<Integer>>();
	static {
		// map colliding emergency lanes
		emergencyCollidingList.put(1, new ArrayList<Integer>(Arrays.asList(11, 31, 32, 22)));
		emergencyCollidingList.put(2, new ArrayList<Integer>(Arrays.asList(11, 12, 32, 21)));
		emergencyCollidingList.put(11, new ArrayList<Integer>(Arrays.asList(21, 32, 1, 2)));
		emergencyCollidingList.put(12, new ArrayList<Integer>(Arrays.asList(21, 22, 31, 2)));
		emergencyCollidingList.put(31, new ArrayList<Integer>(Arrays.asList(21, 22, 12, 1)));
		emergencyCollidingList.put(32, new ArrayList<Integer>(Arrays.asList(22, 11, 1, 2)));
		emergencyCollidingList.put(21, new ArrayList<Integer>(Arrays.asList(31, 12, 11, 2)));
		emergencyCollidingList.put(22, new ArrayList<Integer>(Arrays.asList(31, 32, 12, 1)));

		// map possible cars
		possibleCars.put(0, new ArrayList<Integer>(Arrays.asList(200, 0, 0, 0)));
		possibleCars.put(1, new ArrayList<Integer>(Arrays.asList(260, 0, 0, 1)));
		possibleCars.put(2, new ArrayList<Integer>(Arrays.asList(320, 0, 0, 2)));
		possibleCars.put(10, new ArrayList<Integer>(Arrays.asList(800, 200, 90, 0)));
		possibleCars.put(11, new ArrayList<Integer>(Arrays.asList(800, 258, 90, 1)));
		possibleCars.put(12, new ArrayList<Integer>(Arrays.asList(800, 318, 90, 2)));
		possibleCars.put(20, new ArrayList<Integer>(Arrays.asList(547, 800, 180, 0)));
		possibleCars.put(21, new ArrayList<Integer>(Arrays.asList(490, 800, 180, 1)));
		possibleCars.put(22, new ArrayList<Integer>(Arrays.asList(430, 800, 180, 2)));
		possibleCars.put(30, new ArrayList<Integer>(Arrays.asList(0, 547, 270, 0)));
		possibleCars.put(31, new ArrayList<Integer>(Arrays.asList(0, 490, 270, 1)));
		possibleCars.put(32, new ArrayList<Integer>(Arrays.asList(0, 430, 270, 2)));
		
		//map possible pedestrians
		possiblePeds.put(0, new ArrayList<Integer>(Arrays.asList( 152, 0, 0 )));
		possiblePeds.put(1, new ArrayList<Integer>(Arrays.asList( 608, 0, 0 )));
		possiblePeds.put(2, new ArrayList<Integer>(Arrays.asList( 800, 155, 90 )));
		possiblePeds.put(3, new ArrayList<Integer>(Arrays.asList( 800, 605, 90 )));
		possiblePeds.put(4, new ArrayList<Integer>(Arrays.asList( 0, 155, 270 )));
		possiblePeds.put(5, new ArrayList<Integer>(Arrays.asList( 0, 605, 270 )));
		possiblePeds.put(6, new ArrayList<Integer>(Arrays.asList(152, 800, 180)));
		possiblePeds.put(7, new ArrayList<Integer>(Arrays.asList(608, 800, 180)));
	}
}

enum CarDirection {
	STRAIGHT(1), LEFT(2), RIGHT(0);
	private final int direction;

	CarDirection(int direction) {
		this.direction = direction;
	}

	public int getValue() {
		return direction;
	}

	public static CarDirection getDirection(int val) {
		switch (val) {
		case 0:
			return RIGHT;
		case 2:
			return LEFT;
		default:
			return STRAIGHT;
		}
	}
}

enum CarState {
	MOVING, WAITING, CROSSING, FINISHED;
}

enum PedestrianState {
	MOVING, WAITING_FIRST, CROSSING_FIRST, WAITING_SECOND, CROSSING_SECOND, FINISHED;
}

enum VehicleOptions {
	NONE, CAR, EMERGENCY
}

enum DemoMode {
	NONE, WAITING, RUNNING
}

enum ScenarioNumber{ 
	ONE(1,"There are 3 cars on colliding lanes wishing to cross"),
	TWO(2,"There are cars , emergency vehicles and pedestrians wishing to cross"),
	THREE(3,"There is rush hour and the junction is full"),
	FOUR(4,"There is a heavy fog with cars and pedestrians"),
	FIVE(5,"There is closed road due to road constructions and cars wishing to cross"),
	SIX(6,"There is an emergency vehicle wishing to cross while there is a heavy fog"),
	SEVEN(7,"There are road constructions with cars, emergency vehicles and pedestrians"),
	EIGHT(8,"There are more than 3 vehicles crossing at the same time"),
	NINE(9,"There is a heavy fog and then road constructions are started"),
	TEN(10,"There are road constructions and then a heavy fog appears");
	
	private String description;
	private int number;
	
	ScenarioNumber(int number,String description){
		this.number = number;
		this.description = description;
	}
	
	public String toString() {
		return this.description;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public static ScenarioNumber getScenarioNumber(int scenario) {
		switch(scenario) {
			case 1:
				return ONE;
			case 2:
				return TWO;
			case 3:
				return THREE;
			case 4:
				return FOUR;
			case 5:
				return FIVE;
			case 6:
				return SIX;
			case 7:
				return SEVEN;
			case 8:
				return EIGHT;
			case 9:
				return NINE;
			default:
				return TEN;
		}
	}
}

