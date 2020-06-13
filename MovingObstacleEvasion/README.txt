Moving Obstacle Evasion Simulation
=================================================================================

Moving obstacle evasion is played on a two dimensional grid with two players, an obstacle and a robot. The robot’s goal is to escape the obstacle.
For every two steps the robot makes, the obstacle has a single step. Besides that, the obstacle has a finite number of glitches it can use. When using a glitch, the obstacle can avoid waiting two turns to move and move two turns in a row.

To use the simulation, do the following steps:
1. Choose parameter values for the problem - the grid dimensions, the number of glitches and whether the obstacle is smart (meaning it always moves toward the robot). Edit the "dim", "numOfGlitches" and "isSmartObstacle" parameters of Board.java in the src folder to match the values you chose.
2. In MovingObstacle.spectra, edit the defines named DIM_SIZE and NUM_OF_GLITCHES to match the parameter values you chose and synthesize a symbolic controller for the specification.
3. Run the java simulation.
