/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * 	Parabole.java
 * 	par Emmanuel Hainry
 *
 *	Fonction de transition d'un automate cellulaire
 *	traçant une parabole.
 */

package cell;
 
public class Parabole extends TwoDimCellularAutomaton {

	%include {int.tom}
	
	public Matrice init() {
		int[][] temp = {	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 2, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 2, 4, 6, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 2, 8, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
		return new Matrice(temp);
	}

	public int nextGeneration(Matrice Neighbourhood) {
		int NW = Neighbourhood.matrice[0][0];
		int N  = Neighbourhood.matrice[0][1];
		int NE = Neighbourhood.matrice[0][2];
		int W  = Neighbourhood.matrice[1][0];
		int C  = Neighbourhood.matrice[1][1];
		int E  = Neighbourhood.matrice[1][2];
		int SW = Neighbourhood.matrice[2][0];
		int S  = Neighbourhood.matrice[2][1];
		int SE = Neighbourhood.matrice[2][2];
		%match (int NW, int N, int NE, int W, int C, int E, int SW, int S, int SE) {
			7, _, _, 
			8, _, _,
			_, _, _	-> {return 5;}
			_, _, _,
			_, 5, _,
			_, _, _	-> {return 8;}
			_, _, _,
			_, _, _,
			5, _, _	-> {return 9;}
			_, _, _,
			5, _, _,
			_, _, _ 	-> {return 8;}
			_, _, _,
			4, _, _,
			9, _, _	-> {return 6;}
			_, _, _,
			_, _, _,
			_, 6, _	-> {return 2;}
			_, _, _,
			_, _, _,
			6, _, _	-> {return 4;}
			_, _, _,
			6, _, _,
			_, _, _ 	-> {return 7;}
			_, _, _,
			8, _, _,
			_, _, _ 	-> {return 8;}
			_, _, _,
			4, _, _,
			_, _, _ 	-> {return 4;}
			7, _, _,
			_, _, _,
			_, _, _ 	-> {return 7;}
			_, _, _,
			_, _, _,
			9, _, _ 	-> {return 9;}
			_, _, _,
			_, _, _,
			_, _, _	-> {return C;}			
		}
		return C;
	}
}
