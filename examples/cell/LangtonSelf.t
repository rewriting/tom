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
 * 	LangtonSelf.java
 * 	par Emmanuel Hainry et Blaise Potard
 *
 * 	Fonction de transition de l'automate auto-replicateur 
 * 	de Langton.
 * 	Remarque : les regles sont definies a rotation pres, 
 *	donc on commence par calculer un representant "canonique" 
 *	du membre gauche et seules les regles appliquées aux 
 *	representants canoniques sont ecrites.
 */
 
package cell;

public class LangtonSelf extends TwoDimCellularAutomaton {

	%include {int.tom}	

	public Matrice init() {
		int[][] temp = { {0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0}, 
				{2, 1, 7, 0, 1, 4, 0, 1, 4, 2, 0, 0, 0, 0, 0},
				{2, 0, 2, 2, 2, 2, 2, 2, 0, 2, 0, 0, 0, 0, 0},
				{2, 7, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				{2, 1, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				{2, 0, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				{2, 7, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				{2, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 0},
				{2, 0, 7, 1, 0, 7, 1, 0, 7, 1, 1, 1, 1, 1, 2},
				{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} };
		int[][] bigone = new int[60][60];
		int i, j;
		for (i=0; i< 11; i++) {
			for (j=0; j<15; j++) {
				bigone[33+i][22+j] = temp[i][j];
			}
		}
		Matrice config = new Matrice(bigone);
		//Matrice config = new Matrice(temp);
		return config;
	}

	public int nextGeneration(Matrice Neighbourhood) {
		int N  = Neighbourhood.matrice[0][1];
		int W  = Neighbourhood.matrice[1][0];
		int C  = Neighbourhood.matrice[1][1];
		int E  = Neighbourhood.matrice[1][2];
		int S  = Neighbourhood.matrice[2][1];
		return transition(canonique(N, W, C, E, S));
	}

	public int canonique(int N, int W, int C, int E, int S) {
	// renvoie le representant de NWCES modulo rotation 
	// tel que NESW soit minimal
		int[] tableau = {N, E, S, W, N, E, S};
		int min = 33333;// MAXINT=33333
		int res = 12345;// variable res might not have been initialized
		for (int i=0; i<4; i++) {
			int total = 1000*tableau[i]+100*tableau[i+1]+ 10*tableau[i+2]+tableau[i+3];
			if (total < min) {
        min = total;
        res = 10000 * tableau[i]
          + 1000 * tableau[i+3]
          + 100 * C
          + 10 * tableau[i+1]
          + tableau[i+2];
      }
    }
		return res;
	}
	
	public int transition(int code) {
		int S = code % 10;
		int E = (code / 10) % 10;
		int C = (code /100) % 10;
 		int W = (code/1000) % 10;
		int N = (code/10000)% 10;
		%match (int N, int W, int C, int E, int S) {
		  0,
		1,0,0,
		  0	-> {return 2;}	
		  0,
		6,0,0,
		  0	-> {return 3;}
		  0,
		7,0,0,
		  0	-> {return 1;}
		  0,
		_,0,0,
		  0	-> {return 0;}
		  0,
		1,0,0,
		  1	-> {return 2;}
		  0,
		2,0,0,
		  1	-> {return 2;}
		  0,
		3,0,0,
		  1	-> {return 2;}
		  0,
		1,0,0,
		  2	-> {return 2;}
		  0,
		6,0,0,
		  2	-> {return 2;}
		  0,
		7,0,0,
		  2	-> {return 2;}
		  0,
		2,0,0,
		  5	-> {return 5;}
		  0,
		2,0,0,
		  6	-> {return 2;}
		  0,
		2,0,0,
		  7	-> {return 2;}
		  0,
		2,0,1,
		  0	-> {return 2;}
		  0,
		2,0,2,
		  1	-> {return 5;}
		  0,
		2,0,2,
		  3	-> {return 2;}
		  0,
		2,0,5,
		  2	-> {return 2;}
		  1,
		2,0,2,
		  3	-> {return 1;}
		  1,
		2,0,2,
		  4	-> {return 1;}
		  1,
		2,0,2,
		  5	-> {return 5;}
		  1,
		2,0,2,
		  6	-> {return 1;}
		  1,
		2,0,2,
		  7	-> {return 1;}
		  1,
		5,0,2,
		  7	-> {return 1;}
		  1,
		2,0,4,
		  2	-> {return 1;}
		  1,
		2,0,4,
		  3	-> {return 1;}
		  1,
		2,0,4,
		  4	-> {return 1;}
		  1,
		2,0,4,
		  7	-> {return 1;}
		  1,
		5,0,6,
		  2	-> {return 1;}
		  1,
		2,0,7,
		  2	-> {return 1;}
		  1,
		5,0,7,
		  2	-> {return 5;}
		  1,
		2,0,7,
		  5	-> {return 1;}
		  1,
		2,0,7,
		  6	-> {return 1;}
		  1,
		2,0,7,
		  7	-> {return 1;}
		  2,
		7,0,5,
		  2	-> {return 1;}
		  0,
		7,1,0,
		  0	-> {return 7;}
		  0,
		4,1,0,
		  2	-> {return 4;}
		  0,
		7,1,0,
		  2	-> {return 7;}
		  0,
		4,1,1,
		  2	-> {return 4;}
		  0,
		7,1,1,
		  2	-> {return 7;}
		  0,
		2,1,2,
		  0	-> {return 6;}
		  0,
		4,1,2,
		  2	-> {return 4;}
		  0,
		6,1,2,
		  2	-> {return 3;}
		  0,
		7,1,2,
		  2	-> {return 7;}
		  0,
		2,1,2,
		  3	-> {return 7;}
		  0,
		2,1,2,
		  4	-> {return 4;}
		  0,
		2,1,2,
		  6	-> {return 6;}
		  0,
		4,1,2,
		  6	-> {return 4;}
		  0,
		7,1,2,
		  6	-> {return 7;}
		  0,
		1,1,2,
		  7	-> {return 0;}
		  0,
		2,1,2,
		  7	-> {return 7;}
		  0,
		2,1,5,
		  4	-> {return 7;}
		  1,
		4,1,1,
		  2	-> {return 4;}
		  1,
		7,1,1,
		  2	-> {return 7;}
		  1,
		2,1,1,
		  5	-> {return 2;}
		  1,
		4,1,2,
		  2	-> {return 4;}
		  1,
		7,1,2,
		  2	-> {return 7;}
		  1,
		2,1,2,
		  4	-> {return 4;}
		  1,
		2,1,2,
		  7	-> {return 7;}
		  2,
		4,1,2,
		  2	-> {return 4;}
		  2,
		7,1,2,
		  2	-> {return 7;}
		  2,
		3,1,2,
		  4	-> {return 4;}
		  2,
		4,1,2,
		  5	-> {return 7;}
		  2,
		4,1,3,
		  2	-> {return 4;}
		  2,
		7,1,3,
		  2	-> {return 7;}
		  2,
		5,1,4,
		  2	-> {return 5;}
		  2,
		6,1,4,
		  2	-> {return 7;}
		  2,
		7,1,5,
		  2	-> {return 5;}
		  0,
		7,2,0,
		  0	-> {return 1;}
		  0,
		5,2,0,
		  2	-> {return 0;}
		  0,
		2,2,0,
		  3	-> {return 6;}
		  0,
		2,2,0,
		  4	-> {return 3;}
		  0,
		1,2,0,
		  5	-> {return 7;}
		  0,
		7,2,0,
		  5	-> {return 5;}
		  0,
		7,2,2,
		  0	-> {return 3;}
		  0,
		2,2,2,
		  3	-> {return 1;}
		  0,
		2,2,2,
		  5	-> {return 0;}
		  0,
		1,2,3,
		  2	-> {return 6;}
		  0,
		2,2,3,
		  2	-> {return 6;}
		  0,
		2,2,5,
		  5	-> {return 1;}
		  0,
		2,2,5,
		  7	-> {return 5;}
		  1,
		6,2,1,
		  2	-> {return 1;}
		  0,
		2,3,0,
		  0	-> {return 2;}
		  0,
		4,3,0,
		  0	-> {return 1;}
		  0,
		7,3,0,
		  0	-> {return 6;}
		  0,
		2,3,0,
		  4	-> {return 1;}
		  0,
		2,3,0,
		  6	-> {return 2;}
		  0,
		2,3,1,
		  0	-> {return 1;}
		  0,
		2,3,1,
		  2	-> {return 0;}
		  0,
		1,3,2,
		  5	-> {return 1;}
		  0,
		2,4,1,
		  1	-> {return 0;}
		  0,
		2,4,1,
		  2	-> {return 0;}
		  0,
		5,4,1,
		  2	-> {return 0;}
		  0,
		2,4,2,
		  1	-> {return 0;}
		  0,
		2,4,2,
		  2	-> {return 1;}
		  0,
		2,4,2,
		  3	-> {return 6;}
		  0,
		2,4,2,
		  5	-> {return 0;}
		  0,
		2,4,3,
		  2	-> {return 1;}
		  0,
		2,5,0,
		  0	-> {return 2;}
		  0,
		3,5,0,
		  2	-> {return 2;}
		  0,
		7,5,0,
		  2	-> {return 2;}
		  0,
		2,5,0,
		  5	-> {return 0;}
		  0,
		2,5,2,
		  0	-> {return 2;}
		  0,
		2,5,2,
		  1	-> {return 2;}
		  0,
		5,5,2,
		  1	-> {return 2;}
		  0,
		2,5,2,
		  2	-> {return 0;}
		  0,
		4,5,2,
		  2	-> {return 4;}
		  0,
		2,5,2,
		  7	-> {return 2;}
		  1,
		2,5,2,
		  1	-> {return 2;}
		  1,
		2,5,2,
		  2	-> {return 0;}
		  1,
		2,5,2,
		  4	-> {return 2;}
		  1,
		2,5,2,
		  7	-> {return 2;}
		  0,
		1,6,0,
		  0	-> {return 1;}
		  0,
		2,6,0,
		  0	-> {return 1;}
		  0,
		2,6,2,
		  1	-> {return 0;}
		  1,
		2,6,2,
		  1	-> {return 5;}
		  1,
		3,6,2,
		  1	-> {return 1;}
		  1,
		2,6,2,
		  2	-> {return 5;}
		  0,
		2,7,1,
		  1	-> {return 0;}
		  0,
		2,7,1,
		  2	-> {return 0;}
		  0,
		5,7,1,
		  2	-> {return 0;}
		  0,
		2,7,2,
		  1	-> {return 0;}
		  0,
		2,7,2,
		  2	-> {return 1;}
		  0,
		5,7,2,
		  2	-> {return 1;}
		  0,
		2,7,2,
		  3	-> {return 1;}
		  0,
		2,7,2,
		  5	-> {return 5;}
		  0,
		2,7,2,
		  7	-> {return 0;}
		//_,_,_,_,_ -> {return C;}
		}
		return C;
	}
}
