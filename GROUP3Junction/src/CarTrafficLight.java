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


class CarTrafficLight extends PedestrianTrafficLight {
	private int _trafficLightColor = -1;
	private int _degree = 0;

	public CarTrafficLight(int x, int y) {
		this(x, y, 0);
	}

	public CarTrafficLight(int x, int y, int degree) {
		super(x, y);
		this._degree = degree;
		changeColor(0);
	}

	public void changeColor(int newColor) {
		if (this._trafficLightColor != newColor) {
			String stateToFileName = "src/Images/" + Integer.toString(this._degree) + "/"
					+ Integer.toString(newColor) + ".png";
			loadImage(stateToFileName);
			getImageDimensions();
			this._trafficLightColor = newColor;
		}
	}

	public int getLight() {
		return this._trafficLightColor;
	}

	/*
	 * traffic lights are binary encoded by bits:[down,right,up,left]
	 * e.g. 1011 (11 decimal) means that down & up & left are green and will map to 11.png
	 */
	public void updateState(boolean left, boolean straight, boolean right) {
		int a = left ? 1 : 0;
		int b = straight ? 2 : 0;
		int c = right ? 4 : 0;
		changeColor (a + b + c);
	}
}
