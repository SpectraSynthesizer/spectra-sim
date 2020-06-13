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

package smartHome;

public class listMoves {
	private move head;
	private move tail;
	private move current;
	private int length;
	
	public listMoves(){
		this.head = null;
		this.tail = null;
		this.current = null;
		this.length = 0;
	}
	
	public move getHead() 
	{
		return this.head;
	}
	
	public move getTail() 
	{
		return this.tail;
	}
	
	public void add(move m) 
	{
		if(this.current == null)
			this.current = m;
		
		if(this.head == null)
		{
			this.head = m;
			this.tail = m;
		}
		
		else
		{
			this.tail.setNextMove(m);
			this.tail = m;
		}
		this.length++;
	}
	
	public int[] getNextTurnValues()
	{
		if(this.current == null)
			return null;
		int[] result = new int[4];
		result[0] = this.current.getMother();
		result[1] = this.current.getFather();
		result[2] = this.current.getBaby();
		result[3] = this.current.getThief();
		this.current = this.current.getNextMove();
		return result;
	}
	
	public int[] getTailTurnValues()
	{
		if(this.tail == null)
			return null;
		int[] result = new int[4];
		result[0] = this.tail.getMother();
		result[1] = this.tail.getFather();
		result[2] = this.tail.getBaby();
		result[3] = this.tail.getThief();
		return result;
	}
	
	public void print()
	{
		move current = this.current;
		this.current = this.head;
		int[] result = getNextTurnValues();
		while(result != null)
		{
			System.out.println("mother = " + result[0] +", father = "+ result[1] +", baby = "+ result[2] +", thief = " + result[3]);
			result = getNextTurnValues();
		}
		this.current = current;
	}

	public int length()
	{
		return this.length;
	}
}
