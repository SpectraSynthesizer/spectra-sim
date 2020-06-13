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

package towersOfHanoi;

import java.awt.Rectangle;

class Disk extends Rectangle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int frameWidth = TowersOfHanoi.frameWidth;
	private static int frameHeight = TowersOfHanoi.frameHeight;

	int onTower;
	int diskSize;
	int diskXPosition;
	int diskYPosition;
	boolean isUp;
	boolean isMoved;
	static final int diskWidth = 30; //50

	public Disk(int diskNumber) {
		diskSize = 150 - (TowersOfHanoi.disksArray.length - diskNumber * 20); //300
		onTower = 0;
		diskXPosition = TowersOfHanoi.towerPosition[0] - diskSize / 2 + diskWidth / 2;
		diskYPosition = frameHeight - diskWidth * (TowersOfHanoi.disksArray.length - diskNumber);
		isUp = false;
		isMoved = false;
	}

	public void move(int towerNumber) {
		if (onTower != towerNumber && !isUp) {
			diskYPosition -= 5;
			if (diskYPosition <= (frameHeight / 3)) {
				isUp = true;
			}
		}

		else if (isUp && !isMoved) {
			if (towerNumber > onTower) {
				diskXPosition += 5;
				if (diskXPosition >= (TowersOfHanoi.towerPosition[towerNumber] - diskSize / 2 + diskWidth / 2)) {
					isMoved = true;
				}
			} else {
				diskXPosition -= 5;
				if (diskXPosition <= (TowersOfHanoi.towerPosition[towerNumber] - diskSize / 2 + diskWidth / 2)) {
					isMoved = true;
				}
			}
		} else {
			diskYPosition += 5;
			if (diskYPosition >= (frameHeight - diskWidth*(TowersOfHanoi.numOfDisksOnTowerArray[towerNumber]+1))) {
				isMoved = false;
				isUp = false;
				onTower = towerNumber;
			}
		}
	}

}
