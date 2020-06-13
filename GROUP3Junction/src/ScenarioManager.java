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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/*
 * This class is the one responsible for defining each scenario
 * and all the steps needed to be taken in order to play the specified scenario.
 */
public class ScenarioManager {

	/*
	 * Scenario #1 - 'There are 3 cars on colliding lanes wishing to cross'
	 * This scenario has only one step. 
	 * The step is finished whenever the cars on the colliding lanes crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioOne() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.ONE,stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #1 - final step finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(1, false);
		cars.put(22, false);
		cars.put(32, false);
		scenarioStep1.setCarsLocation(cars);
		scenarioSteps.offer(scenarioStep1);
		return scenarioSteps;
	}

	/*
	 * Scenario #2 - 'There are cars , emergency vehicles and pedestrians wishing to cross'
	 * This scenario has only one step. 
	 * The step is finished whenever the cars, the emergency vehicles and all pedestrians 
	 * crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioTwo() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.TWO, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()==0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #2 - final step finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(22, false);
		cars.put(20, false);
		cars.put(21, true);
		int[][] pedestrians = { { 152, 0, 0 }, { 608, 0, 0 }, { 0, 155, 270 }, { 0, 605, 270 }, { 152, 800, 180 },
				{ 608, 800, 180 }, { 800, 155, 90 }, { 800, 605, 90 } };
		scenarioStep1.setCarsLocation(cars);
		scenarioStep1.setPedsLocation(pedestrians);
		scenarioSteps.offer(scenarioStep1);
		return scenarioSteps;
	}

	/*
	 * Scenario #3 - 'There is rush hour and the junction is full'
	 * This scenario has only one step. 
	 * The step is finished whenever all vehicles and all pedestrians crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioThree() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.THREE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()== 0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #3 - final step finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(0, true);
		cars.put(1, true);
		cars.put(2, false);
		cars.put(10, false);
		cars.put(11, false);
		cars.put(12, false);
		cars.put(20, false);
		cars.put(21, true);
		cars.put(22, false);
		cars.put(30, true);
		cars.put(31, false);
		cars.put(32, false);
		int[][] pedestrians = { { 152, 0, 0 }, { 608, 0, 0 }, { 0, 155, 270 }, { 0, 605, 270 }, { 152, 800, 180 },
				{ 608, 800, 180 }, { 800, 155, 90 }, { 800, 605, 90 } };
		scenarioStep1.setCarsLocation(cars);
		scenarioStep1.setPedsLocation(pedestrians);
		scenarioSteps.offer(scenarioStep1);
		return scenarioSteps;
	}

	/*
	 * Scenario #4 - 'There is a heavy fog with cars and pedestrians'
	 * This scenario has 3 steps. 
	 * Step #1 is finished whenever the junction is empty and there is a fog taking place. 
	 * Step #2 is finished whenever there is still fog and cars came to the junction.
	 * Step #3 is finished whenever there is no more fog and all pedestrians and vehicles
	 * crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioFour() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.FOUR, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()==0 && fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #4 - step1 finished");
					return true;
				}
				return false;
			}
		};
		scenarioStep1.setIsFoggy(true);
		scenarioSteps.offer(scenarioStep1);
		ScenarioStep scenarioStep2 = new ScenarioStep(ScenarioNumber.FOUR, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(0);
				lanes.add(1);
				lanes.add(11);
				lanes.add(22);
				lanes.add(31);
				if(checkCarsInLanes(cars,lanes) && fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #4 - step2 finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(0, true);
		cars.put(1, false);
		cars.put(11, true);
		cars.put(22, false);
		cars.put(31, false);
		int[][] pedestrians = { { 152, 0, 0 }, { 0, 605, 270 }, { 152, 800, 180 }, { 608, 800, 180 },
				{ 800, 605, 90 } };
		scenarioStep2.setCarsLocation(cars);
		scenarioStep2.setPedsLocation(pedestrians);
		scenarioStep2.setIsFoggy(true);
		scenarioStep2.setExtraWait(25);
		scenarioSteps.offer(scenarioStep2);
		ScenarioStep scenarioStep3 = new ScenarioStep(ScenarioNumber.FOUR, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()==0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #4 - final step finished");
					return true;
				}
				return false;
			}
		};
		scenarioSteps.offer(scenarioStep3);
		return scenarioSteps;
	}

	/*
	 * Scenario #5 - 'There is closed road due to road constructions and cars wishing to cross'
	 * This scenario has 3 steps. 
	 * Step #1 is finished whenever there is no fog but the north road is closed. 
	 * Step #2 is finished whenever there is still closed road and there are cars wishing to
	 * cross the junction toward this road.
	 * Step #3 is finished whenever there are no more road constructions, the road was opened and all vehicles and 
	 * pedestrians crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioFive() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.FIVE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(!fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #5 - step1 finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(0, false);
		cars.put(1, false);
		cars.put(11, false);
		cars.put(20, false);
		cars.put(31, false);
		int[][] pedestrians = { { 152, 0, 0 }, { 0, 605, 270 }, { 152, 800, 180 }, { 608, 800, 180 },
				{ 800, 605, 90 } };
		scenarioStep1.setCarsLocation(cars);
		scenarioStep1.setPedsLocation(pedestrians);
		scenarioStep1.setIsClosedRoad(true);
		scenarioSteps.offer(scenarioStep1);
		ScenarioStep scenarioStep2 = new ScenarioStep(ScenarioNumber.FIVE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(10);
				lanes.add(21);
				lanes.add(32);
				if(checkCarsInLanes(cars,lanes) && !fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #5 - step2 finished");
					return true;
				}
				return false;
			}
		};
		cars = new HashMap<>();
		cars.put(10, false);
		cars.put(21, false);
		cars.put(32, false);
		pedestrians = new int[][]{ { 608, 0, 0 }, { 0, 155, 270 } };
		scenarioStep2.setCarsLocation(cars);
		scenarioStep2.setPedsLocation(pedestrians);
		scenarioStep2.setIsClosedRoad(true);
		scenarioStep2.setExtraWait(25);
		scenarioSteps.offer(scenarioStep2);
		ScenarioStep scenarioStep3 = new ScenarioStep(ScenarioNumber.FIVE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()==0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #5 - final step finished");
					return true;
				}
				return false;
			}
		};
		scenarioSteps.offer(scenarioStep3);
		return scenarioSteps;
	}

	/*
	 * Scenario #6 - 'There is an emergency vehicle wishing to cross while there is a heavy fog'
	 * This scenario has 3 steps. 
	 * Step #1 is finished whenever the junction is empty and there is a fog taking place. 
	 * Step #2 is finished whenever there is still fog and emergency vehicles came to the junction and
	 * wishing to cross.
	 * Step #3 is finished whenever there is no more fog and all emergency vehicles
	 * crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioSix() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.SIX, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()== 0 && fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #6 - step1 finished");
					return true;
				}
				return false;
			}
		};
		scenarioStep1.setIsFoggy(true);
		scenarioSteps.offer(scenarioStep1);
		ScenarioStep scenarioStep2 = new ScenarioStep(ScenarioNumber.SIX, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(0);
				lanes.add(1);
				lanes.add(20);
				if(checkCarsInLanes(cars,lanes) && fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #6 - step2 finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(0, true);
		cars.put(1, true);
		cars.put(20, true);
		scenarioStep2.setCarsLocation(cars);
		scenarioStep2.setIsFoggy(true);
		scenarioStep2.setExtraWait(25);
		scenarioSteps.offer(scenarioStep2);
		ScenarioStep scenarioStep3 = new ScenarioStep(ScenarioNumber.SIX, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()== 0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #6 - final step finished");
					return true;
				}
				return false;
			}
		};
		scenarioStep3.setIsFoggy(false);
		scenarioSteps.offer(scenarioStep3);
		return scenarioSteps;
	}

	/*
	 * Scenario #7 - 'There are road constructions with cars, emergency vehicles and pedestrians'
	 * This scenario has 3 steps. 
	 * Step #1 is finished whenever there is a closed road due to road constructions. 
	 * Step #2 is finished whenever the road is still closed and there are cars wishing to 
	 * cross the junction toward this road.
	 * Step #3 is finished whenever there are no more road constructions, the road was opened and all vehicles and 
	 * pedestrians crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioSeven() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.SEVEN, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(!fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #7 - step1 finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(0, true);
		cars.put(1, false);
		cars.put(20, true);
		cars.put(31, false);
		int[][] pedestrians = { { 152, 0, 0 }, { 0, 605, 270 }, { 152, 800, 180 }, { 608, 800, 180 },
				{ 800, 605, 90 } };
		scenarioStep1.setCarsLocation(cars);
		scenarioStep1.setPedsLocation(pedestrians);
		scenarioStep1.setIsClosedRoad(true);
		scenarioSteps.offer(scenarioStep1);
		ScenarioStep scenarioStep2 = new ScenarioStep(ScenarioNumber.SEVEN, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(10);
				lanes.add(32);
				if(checkCarsInLanes(cars,lanes) && !fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #7 - step2 finished");
					return true;
				}
				return false;
			}
		};
		cars = new HashMap<>();
		cars.put(10, true);
		cars.put(11, false);
		cars.put(32, false);
		pedestrians = new int[][]{ { 608, 0, 0 }, { 0, 155, 270 } };
		scenarioStep2.setCarsLocation(cars);
		scenarioStep2.setPedsLocation(pedestrians);
		scenarioStep2.setIsClosedRoad(true);
		scenarioStep2.setExtraWait(25);
		scenarioSteps.offer(scenarioStep2);
		ScenarioStep scenarioStep3 = new ScenarioStep(ScenarioNumber.SEVEN, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()==0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #7 - final step finished");
					return true;
				}
				return false;
			}
		};
		scenarioSteps.offer(scenarioStep3);
		return scenarioSteps;
	}

	/*
	 * Scenario #8 - 'There are more than 3 vehicles crossing at the same time'
	 * This scenario has only one step. 
	 * The step is finished whenever all the vehicles crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioEight() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenario = new ScenarioStep(ScenarioNumber.EIGHT, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()== 0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #8 - final step finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer, Boolean> cars = new HashMap<>();
		cars.put(0, true);
		cars.put(1, true);
		cars.put(10, true);
		cars.put(20, true);
		cars.put(21, true);
		cars.put(30, true);
		scenario.setCarsLocation(cars);
		scenarioSteps.offer(scenario);
		return scenarioSteps;
	}
	
	/*
	 * Scenario #9 - 'There is a heavy fog and then road constructions are started'
	 * This scenario has 5 steps. 
	 * Step #1 is finished whenever the junction is empty of vehicles and there is a heavy fog taking place. 
	 * Step #2 is finished whenever there is still fog, cars came to the junction and road constructions
	 * are waiting to begin.
	 * Step #3 is finished whenever there is no more fog and all pedestrians and vehicles
	 * crossed the junction safely and road constructions are started.
	 * Step #4 is finished whenever there are road constructions and cars wishing to cross closed road.
	 * Step #5 is finished whenever there are no more road constructions and all vehicles 
	 * crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioNine() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.NINE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #9 - step1 finished");
					return true;
				}
				return false;
			}
		};
		int[][] pedestrians = { { 152, 0, 0 }, { 0, 605, 270 }, { 152, 800, 180 }, { 608, 800, 180 },
				{ 800, 605, 90 } };
		scenarioStep1.setPedsLocation(pedestrians);
		scenarioStep1.setIsFoggy(true);
		scenarioSteps.offer(scenarioStep1);
		ScenarioStep scenarioStep2 = new ScenarioStep(ScenarioNumber.NINE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(0);
				lanes.add(10);
				lanes.add(11);
				lanes.add(21);
				lanes.add(22);
				lanes.add(30);
				lanes.add(31);
				if(checkCarsInLanes(cars,lanes) && fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #9 - step2 finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer,Boolean> cars = new HashMap<>();
		cars.put(0, true);
		cars.put(10, false);
		cars.put(11, false);
		cars.put(21, true);
		cars.put(22, false);
		cars.put(30, false);
		cars.put(31, false);
		scenarioStep2.setCarsLocation(cars);
		scenarioStep2.setIsClosedRoad(true);
		scenarioStep2.setIsFoggy(true);
		scenarioStep2.setExtraWait(20);
		scenarioSteps.offer(scenarioStep2);
		ScenarioStep scenarioStep3 = new ScenarioStep(ScenarioNumber.NINE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(!fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #9 - step3 finished");
					return true;
				}
				return false;
			}
		};
		scenarioStep3.setIsClosedRoad(true);
		scenarioSteps.offer(scenarioStep3);
		ScenarioStep scenarioStep4 = new ScenarioStep(ScenarioNumber.NINE, stepNumber++) {
			
			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(10);
				lanes.add(21);
				if(checkCarsInLanes(cars,lanes) && !fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #9 - step4 finished");
					return true;
				}
				return false;
			}
		};
		cars = new HashMap<>();
		cars.put(10, false);
		cars.put(21, true);
		scenarioStep4.setCarsLocation(cars);
		scenarioStep4.setIsClosedRoad(true);
		scenarioStep4.setExtraWait(25);
		scenarioSteps.offer(scenarioStep4);
		ScenarioStep scenarioStep5 = new ScenarioStep(ScenarioNumber.NINE, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()==0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #9 - final step finished");
					return true;
				}
				return false;
			}
		};
		scenarioSteps.offer(scenarioStep5);
		return scenarioSteps;
	}
	
	/*
	 * Scenario #10 - 'There are road constructions and then a heavy fog appears'
	 * This scenario has 3 steps. 
	 * Step #1 is finished whenever the junction is empty of vehicles and there is a closed road 
	 * due to road constructions. 
	 * Step #2 is finished whenever there is still a closed road and a heavy fog has stated.
	 * Step #3 is finished whenever there are no more road constructions and the road is opened, but due to 
	 * heavy fog all vehicles are waiting till fog is finished.
	 * Step #4 is finished whenever there is no more fog and all vehicles crossed the junction safely.
	 */
	private static Queue<ScenarioStep> createScenarioTen() {
		Queue<ScenarioStep> scenarioSteps = new LinkedList<>();
		int stepNumber = 0;
		ScenarioStep scenarioStep1 = new ScenarioStep(ScenarioNumber.TEN, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(!fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #10 - step1 finished");
					return true;
				}
				return false;
			}
		};
		int[][] pedestrians = { { 152, 0, 0 }, { 0, 605, 270 }, { 152, 800, 180 }, { 608, 800, 180 },
				{ 800, 605, 90 } };
		scenarioStep1.setPedsLocation(pedestrians);
		scenarioStep1.setIsClosedRoad(true);
		scenarioSteps.offer(scenarioStep1);
		ScenarioStep scenarioStep2 = new ScenarioStep(ScenarioNumber.TEN, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(10);
				lanes.add(21);
				if(checkCarsInLanes(cars,lanes) && fogDrawn && closedRoadDrawn) {
					System.out.println("scenario #10 - step2 finished");
					return true;
				}
				return false;
			}
		};
		Map<Integer,Boolean> cars = new HashMap<>();
		cars.put(0, true);
		cars.put(10, false);
		cars.put(11, false);
		cars.put(21, true);
		cars.put(22, false);
		cars.put(30, false);
		cars.put(31, false);
		scenarioStep2.setCarsLocation(cars);
		scenarioStep2.setIsClosedRoad(true);
		scenarioStep2.setIsFoggy(true);
		scenarioSteps.offer(scenarioStep2);
		ScenarioStep scenarioStep3 = new ScenarioStep(ScenarioNumber.TEN, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				Set<Integer> lanes = new HashSet<>();
				lanes.add(0);
				lanes.add(10);
				lanes.add(12);
				lanes.add(21);
				lanes.add(31);
				if(checkCarsInLanes(cars,lanes) && fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #10 - step3 finished");
					return true;
				}
				return false;
			}
		};
		cars = new HashMap<>();
		cars.put(0, true);
		cars.put(12, false);
		cars.put(31, false);
		scenarioStep3.setCarsLocation(cars);
		scenarioStep3.setIsFoggy(true);
		scenarioStep3.setExtraWait(25);
		scenarioSteps.offer(scenarioStep3);
		ScenarioStep scenarioStep4 = new ScenarioStep(ScenarioNumber.TEN, stepNumber++) {

			@Override
			public boolean isFinished(List<Car> cars, List<Pedestrian> peds, boolean fogDrawn, boolean closedRoadDrawn) {
				if(cars.size()==0 && peds.size()==0 && !fogDrawn && !closedRoadDrawn) {
					System.out.println("scenario #10 - final step finished");
					return true;
				}
				return false;
			}
		};
		scenarioSteps.offer(scenarioStep4);
		return scenarioSteps;
	}

	/*
	 * returns a queue of steps needed to be taken in order to play the specified scenario
	 */
	public static Queue<ScenarioStep> getScenario(ScenarioNumber scenario) {
		switch (scenario.getNumber()) {
		case 1:
			return createScenarioOne();
		case 2:
			return createScenarioTwo();
		case 3:
			return createScenarioThree();
		case 4:
			return createScenarioFour();
		case 5:
			return createScenarioFive();
		case 6:
			return createScenarioSix();
		case 7:
			return createScenarioSeven();
		case 8:
			return createScenarioEight();
		case 9:
			return createScenarioNine();
		default:
			return createScenarioTen();
		}
	}
	
	/*
	 * checks if there are cars waiting in given list of lanes.
	 */
	private static boolean checkCarsInLanes(List<Car> cars, Set<Integer> lanes) {
		for(Car car : cars) {
			if(lanes.contains(car.getLaneCode()) && car.getCarState()==CarState.WAITING) {
				lanes.remove(car.getLaneCode());
			}
		}
		if(lanes.size() == 0)
			return true;
		return false;
	}
}
