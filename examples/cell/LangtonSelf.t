/*
 * 	LangtonSelf.java
 * 	par Emmanuel Hainry
 *
 * 	Fonction de transition de l'automate auto-réplicateur 
 * 	de Langton.
 */

package Cell;

public class LangtonSelf extends TwoDimCellularAutomaton {
	
	%include{int.tom}
	
	public int nextGeneration(Matrice Neighbourhood) {
		int N  = Neighbourhood.matrice[0][1];
		int W  = Neighbourhood.matrice[1][0];
		int C  = Neighbourhood.matrice[1][1];
		int E  = Neighbourhood.matrice[1][2];
		int S  = Neighbourhood.matrice[2][1];
		return transition(N, W, C, E, S, 0);
	}
	
	public int transition(int N, int W, int C, int E, int S, int i) {
		if (i>3) { 	// if no rule is found after 4 rotations
			return C;
		} else {
			%match (int N, int W, int C, int E, int S) {
		  0,
		1,0,0,
		  0	-> {return 2;}	// cap a path
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
		2,0,0,
		  2	-> {return 0;}
		  0,
		3,0,0,
		  2	-> {return 0;}
		  0,
		6,0,0,
		  2	-> {return 2;}
		  0,
		7,0,0,
		  2	-> {return 2;}
		  0,
		2,0,0,
		  3	-> {return 0;}
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
		2,0,1,
		  1	-> {return 0;}
		  0,
		2,0,2,
		  0	-> {return 0;}
		  0,
		3,0,2,
		  0	-> {return 0;}
		  0,
		5,0,2,
		  0	-> {return 0;}
		  0,
		2,0,2,
		  1	-> {return 5;}
		  0,
		2,0,2,
		  2	-> {return 0;}
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
		1,1,0,
		  0	-> {return 1;}
		  0,
		6,1,0,
		  0	-> {return 1;}
		  0,
		7,1,0,
		  0	-> {return 7;}
		  0,
		1,1,0,
		  1	-> {return 1;}
		  0,
		2,1,0,
		  1	-> {return 1;}
		  0,
		1,1,0,
		  2	-> {return 1;}
		  0,
		4,1,0,
		  2	-> {return 4;}
		  0,
		7,1,0,
		  2	-> {return 7;}
		  0,
		1,1,0,
		  5	-> {return 1;}
		  0,
		1,1,1,
		  0	-> {return 1;}
		  0,
		1,1,1,
		  1	-> {return 1;}
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
		2,1,2,
		  1	-> {return 1;}
		  0,
		1,1,2,
		  2	-> {return 1;}
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
		2,1,1,
		  1	-> {return 1;}
		  1,
		2,1,1,
		  2	-> {return 1;}
		  1,
		4,1,1,
		  2	-> {return 4;}
		  1,
		5,1,1,
		  2	-> {return 1;}
		  1,
		6,1,1,
		  2	-> {return 1;}
		  1,
		7,1,1,
		  2	-> {return 7;}
		  1,
		2,1,1,
		  5	-> {return 2;}
		  1,
		2,1,2,
		  1	-> {return 1;}
		  1,
		2,1,2,
		  2	-> {return 1;}
		  1,
		4,1,2,
		  2	-> {return 4;}
		  1,
		5,1,2,
		  2	-> {return 1;}
		  1,
		7,1,2,
		  2	-> {return 7;}
		  1,
		2,1,2,
		  3	-> {return 1;}
		  1,
		2,1,2,
		  4	-> {return 4;}
		  1,
		2,1,2,
		  6	-> {return 1;}
		  1,
		2,1,2,
		  7	-> {return 7;}
		  1,
		2,1,3,
		  2	-> {return 1;}
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
		1,2,0,
		  0	-> {return 2;}
		  0,
		2,2,0,
		  0	-> {return 2;}
		  0,
		4,2,0,
		  0	-> {return 2;}
		  0,
		7,2,0,
		  0	-> {return 1;}
		  0,
		2,2,0,
		  1	-> {return 2;}
		  0,
		5,2,0,
		  1	-> {return 2;}
		  0,
		1,2,0,
		  2	-> {return 2;}
		  0,
		2,2,0,
		  2	-> {return 2;}
		  0,
		3,2,0,
		  2	-> {return 2;}
		  0,
		4,2,0,
		  2	-> {return 2;}
		  0,
		5,2,0,
		  2	-> {return 0;}
		  0,
		6,2,0,
		  2	-> {return 2;}
		  0,
		7,2,0,
		  2	-> {return 2;}
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
		2,2,0,
		  5	-> {return 2;}
		  0,
		7,2,0,
		  5	-> {return 5;}
		  0,
		2,2,0,
		  7	-> {return 2;}
		  0,
		2,2,1,
		  0	-> {return 2;}
		  0,
		2,2,1,
		  1	-> {return 2;}
		  0,
		2,2,1,
		  2	-> {return 2;}
		  0,
		2,2,1,
		  4	-> {return 2;}
		  0,
		2,2,1,
		  7	-> {return 2;}
		  0,
		2,2,2,
		  0	-> {return 2;}
		  0,
		3,2,2,
		  0	-> {return 2;}
		  0,
		5,2,2,
		  0	-> {return 2;}
		  0,
		7,2,2,
		  0	-> {return 3;}
		  0,
		2,2,2,
		  1	-> {return 2;}
		  0,
		5,2,2,
		  1	-> {return 2;}
		  0,
		1,2,2,
		  2	-> {return 2;}
		  0,
		2,2,2,
		  2	-> {return 2;}
		  0,
		7,2,2,
		  2	-> {return 2;}
		  0,
		2,2,2,
		  3	-> {return 1;}
		  0,
		2,2,2,
		  4	-> {return 2;}
		  0,
		5,2,2,
		  4	-> {return 2;}
		  0,
		2,2,2,
		  5	-> {return 0;}
		  0,
		5,2,2,
		  5	-> {return 2;}
		  0,
		2,2,2,
		  6	-> {return 2;}
		  0,
		2,2,2,
		  7	-> {return 2;}
		  0,
		2,2,3,
		  1	-> {return 2;}
		  0,
		1,2,3,
		  2	-> {return 6;}
		  0,
		2,2,3,
		  2	-> {return 6;}
		  0,
		2,2,3,
		  4	-> {return 2;}
		  0,
		2,2,4,
		  2	-> {return 2;}
		  0,
		2,2,5,
		  1	-> {return 2;}
		  0,
		1,2,5,
		  2	-> {return 2;}
		  0,
		2,2,5,
		  2	-> {return 2;}
		  0,
		2,2,5,
		  5	-> {return 1;}
		  0,
		2,2,5,
		  7	-> {return 5;}
		  0,
		2,2,6,
		  2	-> {return 2;}
		  0,
		2,2,6,
		  7	-> {return 2;}
		  0,
		2,2,7,
		  1	-> {return 2;}
		  0,
		2,2,7,
		  2	-> {return 2;}
		  0,
		2,2,7,
		  4	-> {return 2;}
		  0,
		2,2,7,
		  7	-> {return 2;}
		  1,
		2,2,1,
		  2	-> {return 2;}
		  1,
		6,2,1,
		  2	-> {return 1;}
		  1,
		2,2,2,
		  2	-> {return 2;}
		  1,
		4,2,2,
		  2	-> {return 2;}
		  1,
		6,2,2,
		  2	-> {return 2;}
		  1,
		7,2,2,
		  2	-> {return 2;}
		  1,
		2,2,4,
		  2	-> {return 2;}
		  1,
		2,2,5,
		  2	-> {return 2;}
		  1,
		2,2,6,
		  2	-> {return 2;}
		  1,
		2,2,7,
		  2	-> {return 2;}
		  2,
		7,2,2,
		  2	-> {return 2;}
		  2,
		4,2,2,
		  4	-> {return 2;}
		  2,
		6,2,2,
		  4	-> {return 2;}
		  2,
		6,2,2,
		  7	-> {return 2;}
		  2,
		7,2,2,
		  7	-> {return 2;}
		  0,
		1,3,0,
		  0	-> {return 3;}
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
		  1	-> {return 3;}
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
		1,5,0,
		  2	-> {return 5;}
		  0,
		2,5,0,
		  2	-> {return 5;}
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
		7,7,0,
		  0	-> {return 7;}
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
		  _,
		_,_,_,
		  _	-> {return transition(W, S, C, N, E, i+1);}
			}
		}
	}
}
