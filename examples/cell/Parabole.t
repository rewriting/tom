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
	}
}
